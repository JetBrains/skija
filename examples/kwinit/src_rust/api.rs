use std::{fmt::Debug, ops::DerefMut};
use std::cell::RefCell;
use std::collections::HashMap;
use std::panic::{catch_unwind, PanicInfo, UnwindSafe};
use std::sync::{Condvar, Mutex, MutexGuard};

use clipboard::{ClipboardContext, ClipboardProvider};
use glutin::{dpi::LogicalSize, event_loop::{ControlFlow, EventLoop, EventLoopProxy, EventLoopWindowTarget}, platform::desktop::EventLoopExtDesktop};
use glutin::dpi::{LogicalPosition, PhysicalSize, Position, Size};
use glutin::event::Event;
#[cfg(target_os = "macos")]
use glutin::platform::macos::WindowExtMacOS;
#[cfg(target_os = "macos")]
use objc::runtime::{YES, NO};
use glutin::window::{WindowBuilder, WindowId};
use jni::JNIEnv;
use jni::objects::{JMethodID, JObject, JString, JValue, AutoLocal};
use jni::signature::{JavaType, Primitive, TypeSignature};
use jni::sys::{jboolean, jdouble, jfloat, jlong, jstring};
use lazy_static::lazy_static;
use notify_rust::Notification;
use serde::Deserialize;

use crate::app_menu::{ApplicationMenu, init_app_menu, update_app_menu};
use crate::kwevents::{KWEvent, KWindowId, to_kwindow_event};
use crate::platform;
use crate::platform::{set_app_dock_badge, set_app_dock_icon, set_app_dock_progress};
#[cfg(target_os = "windows")]
use crate::platform::remove_system_title_bar;
use crate::windowed_context::{create_windowed_context_wrapper, WindowedContextWrapper};

#[derive(Default)]
struct GlobalState {
    event_loop_proxy: Option<EventLoopProxy<i64>>
}

lazy_static! {
    static ref GLOBAL_STATE_CVAR: Condvar = Condvar::new();
    static ref GLOBAL_STATE: Mutex<GlobalState> = Mutex::new(GlobalState::default());
    static ref CLIPBOARD: Mutex<ClipboardContext> = Mutex::new(ClipboardProvider::new().unwrap());
}

struct KWindow {
    context: WindowedContextWrapper,
    _id: KWindowId
}

#[derive(Deserialize, Debug)]
struct WindowAttributes {
  inner_size: LogicalSize<f32>,
  position: LogicalPosition<f32>,
  title: String,
  macos_traffic_light_center: Option<LogicalPosition<f64>>
}

impl KWindow {
    fn new(event_loop_window_target: &EventLoopWindowTarget<i64>,
           attributes: WindowAttributes,
           window_id: KWindowId) -> Self
    {

        let window_builder = WindowBuilder::new()
            .with_title(attributes.title)
            .with_resizable(true)
            .with_inner_size(attributes.inner_size)
            .with_visible(false);

        let context_builder = glutin::ContextBuilder::new()
            .with_vsync(true)
            .with_gl(glutin::GlRequest::GlThenGles {
                opengl_version: (3, 2),
                opengles_version: (3, 0),
            });

        let context = create_windowed_context_wrapper(window_builder, context_builder, event_loop_window_target);
        let window = context.window();
        #[cfg(target_os = "windows")] unsafe { remove_system_title_bar(window); }
        window.set_visible(true);
        window.set_outer_position(attributes.position);

        #[cfg(target_os = "macos")] {
            unsafe {
                use cocoa::appkit::*;
                use cocoa::foundation::{NSPoint, NSRect, NSSize};
                use objc::*;

                let nswindow = window.ns_window() as *mut objc::runtime::Object;
                NSWindow::setTitleVisibility_(nswindow, NSWindowTitleVisibility::NSWindowTitleHidden);
                NSWindow::setTitlebarAppearsTransparent_(nswindow, YES);
             
                // always show `fullscreen` green traffic light button instead of `maximize/zoom` button
                let behavior_mask = NSWindow::collectionBehavior(nswindow);
                NSWindow::setCollectionBehavior_(nswindow, behavior_mask | NSWindowCollectionBehavior::NSWindowCollectionBehaviorFullScreenPrimary);
                let mask = NSWindow::styleMask(nswindow);
                NSWindow::setStyleMask_(nswindow, mask | NSWindowStyleMask::NSFullSizeContentViewWindowMask);
                NSWindow::setMovable_(nswindow, NO);
            }
        }

        KWindow { 
            context, 
            _id: window_id
        }
    }
}

#[derive(Default)]
struct WindowsMap {
    windows: HashMap<KWindowId, KWindow>,
    window_id_mapping: HashMap<WindowId, KWindowId>,
    next_id: i64
}

thread_local! {
    static PANIC_MESSAGE: RefCell<Option<String>> = RefCell::new(None);
    static WINDOWS_MAP: RefCell<Option<WindowsMap>> = RefCell::new(None);
    static EVENT_LOOP_WINDOW_TARGET: RefCell<Option<*const EventLoopWindowTarget<i64>>> = RefCell::new(None);
    static STOP_APPLICATION_FLAG: RefCell<bool> = RefCell::new(false);
    static APP_MENU_STATE: RefCell<Option<platform::AppMenuState>> = RefCell::new(None);
}

fn global_state() -> MutexGuard<'static, GlobalState> {
    let mut global_state = GLOBAL_STATE.lock().unwrap();
    loop {
        if global_state.event_loop_proxy.is_some() {
            return global_state;
        } else {
            global_state = GLOBAL_STATE_CVAR.wait(global_state).unwrap();
        }
    }
}

fn panic_hook(info: &PanicInfo<'_>) {
    let payload = match info.payload().downcast_ref::<&'static str>() {
        Some(s) => *s,
        None => match info.payload().downcast_ref::<String>() {
            Some(s) => &s[..],
            None => "",
        }
    };

    let location = info.location().unwrap();

    let thread = std::thread::current();
    let name = thread.name().unwrap_or("<unnamed>");

    let backtrace = backtrace::Backtrace::new();

    let message = format!("thread '{}' panicked at '{}', {}\n {:?}", name, payload, location, backtrace);

    PANIC_MESSAGE.with(|panic_message| {
        panic_message.replace(Some(message));
    });
}

fn with_panic_logger<F: FnOnce(&JNIEnv) -> R + UnwindSafe, R: Debug>(env: &JNIEnv, f: F) -> Option<R> {
    let result = catch_unwind(|| f(env));
    if result.is_err() {
        let message = PANIC_MESSAGE.with(|panic_message| {
            panic_message.replace(None)
        });
        let _ = env.throw_new("noria/kwinit/KWInitException", message.unwrap_or("".to_string()));
    }
    result.ok()
}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_noria_kwinit_impl_ExternalAPI_createWindow(env: JNIEnv, _obj: JObject, attributes: JString) -> i64 {
    let res = with_panic_logger(&env, |_env| {
        WINDOWS_MAP.with(|windows_map_ref| {
            let mut x = windows_map_ref.borrow_mut();
            let windows_map = x.deref_mut().as_mut().expect("accessing event loop from non-main thread ot before event loop has started");
            let window_id = KWindowId(windows_map.next_id);
            windows_map.next_id += 1;
            EVENT_LOOP_WINDOW_TARGET.with(|event_loop_window_target| {
                let event_loop_window_target_ptr = event_loop_window_target.borrow_mut().deref_mut().expect("accessing event loop from non-main thread ot before event loop has started");
                let event_loop_window_target_ref = unsafe { &*event_loop_window_target_ptr };
                let attrs = serde_json::from_str(env.get_string(attributes).expect("Failed to retrieve window attributes string")
                  .to_str().expect("Non Utf8 string with window attributes")).expect("Deserialization error");
                let mut new_window = KWindow::new(event_loop_window_target_ref, attrs, window_id);
                new_window.context.make_current();
                windows_map.window_id_mapping.insert(new_window.context.window().id(), window_id);
                windows_map.windows.insert(window_id, new_window);
                window_id.0
            })
        })
    }).unwrap_or(0);
    res
}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_noria_kwinit_impl_ExternalAPI_makeCurrent(env: JNIEnv, _obj: JObject, window_id: i64) {
    with_panic_logger(&env, |_env| {
        WINDOWS_MAP.with(|windows_map_cell| {
            let mut windows_map_ref = windows_map_cell.borrow_mut();
            let windows_map = windows_map_ref.as_mut().expect("accessing event loop from non-main thread ot before event loop has started");
            let window = windows_map.windows.get_mut(&KWindowId(window_id)).expect("no window with such id");
            window.context.make_current()
        })
    });
}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_noria_kwinit_impl_ExternalAPI_getScaleFactor(env: JNIEnv, _obj: JObject, window_id: i64) -> f64 {
    with_panic_logger(&env, |_env| {
        WINDOWS_MAP.with(|windows_map_cell| {
            let windows_map_ref = windows_map_cell.borrow();
            let windows_map = windows_map_ref.as_ref().expect("accessing event loop from non-main thread ot before event loop has started");
            let scale_factor = windows_map.windows.get(&KWindowId(window_id))
              .expect("window not found")
              .context.window().scale_factor();
            return scale_factor
        })
    }).unwrap_or(1.0)
}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_noria_kwinit_impl_ExternalAPI_getInnerSize(env: JNIEnv,
                                                                       _obj: JObject,
                                                                       window_id: jlong) -> jstring {
    with_panic_logger(&env, |_env| {
        WINDOWS_MAP.with(|windows_map_cell| {
            let mut windows_map_ref = windows_map_cell.borrow_mut();
            let windows_map = windows_map_ref.as_mut().expect("accessing event loop from non-main thread ot before event loop has started");
            let kwindow = windows_map.windows.get_mut(&KWindowId(window_id)).expect("No such window");
            let scale_factor = kwindow.context.window().scale_factor();
            let inner_size: LogicalSize<f32> = kwindow.context.window()
              .inner_size()
              .to_logical(scale_factor);
            let logicalSizeJson = serde_json::to_string(&inner_size).expect("Failed to serialize LogicalSize");
            return env.new_string(logicalSizeJson).unwrap().into_inner()
        })
    }).unwrap_or(JObject::null().into_inner())
}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_noria_kwinit_impl_ExternalAPI_setInnerSize(env: JNIEnv,
                                                                       _obj: JObject,
                                                                       window_id: jlong,
                                                                       width: jfloat,
                                                                       height: jfloat) {
    with_panic_logger(&env, |_env| {
        WINDOWS_MAP.with(|windows_map_cell| {
            let mut windows_map_ref = windows_map_cell.borrow_mut();
            let windows_map = windows_map_ref.as_mut().expect("accessing event loop from non-main thread ot before event loop has started");
            if let Some(kwindow) = windows_map.windows.get_mut(&KWindowId(window_id)) {
                kwindow.context.window().set_inner_size(Size::Logical(LogicalSize::new(width as f64, height as f64)));
            };
        })
    });
}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_noria_kwinit_impl_ExternalAPI_destroyWindow(env: JNIEnv, _obj: JObject, window_id: jlong) {
    with_panic_logger(&env, |_env| {
        WINDOWS_MAP.with(|windows_map_cell| {
            let mut windows_map_ref = windows_map_cell.borrow_mut();
            let windows_map = windows_map_ref.as_mut().expect("accessing event loop from non-main thread ot before event loop has started");
            windows_map.windows.remove(&KWindowId(window_id)).and_then(|kwindow| {
                windows_map.window_id_mapping.remove(&kwindow.context.window().id())
            });
        });
    });
}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_noria_kwinit_impl_ExternalAPI_requestRedraw(env: JNIEnv, _obj: JObject, window_id: i64) {
    with_panic_logger(&env, |_env| {
        WINDOWS_MAP.with(|windows_map_cell| {
            let mut windows_map_ref = windows_map_cell.borrow_mut();
            let windows_map = windows_map_ref.as_mut().expect("accessing event loop from non-main thread ot before event loop has started");
            if let Some(kwindow) = windows_map.windows.get_mut(&KWindowId(window_id)) {
                log::info!("Request redraw {:?}", window_id);
                let window = kwindow.context.window();
                window.request_redraw();
            };
        });
    });
}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_noria_kwinit_impl_ExternalAPI_resize(env: JNIEnv,
                                                                 _obj: JObject,
                                                                 window_id: i64,
                                                                 width: jlong,
                                                                 height: jlong) {
  with_panic_logger(&env, |_env| {
    WINDOWS_MAP.with(|windows_map_cell| {
      let mut windows_map_ref = windows_map_cell.borrow_mut();
      let windows_map = windows_map_ref.as_mut().expect("accessing event loop from non-main thread ot before event loop has started");
      if let Some(kwindow) = windows_map.windows.get_mut(&KWindowId(window_id)) {
        kwindow.context.resize(PhysicalSize::new(width as u32, height as u32));
      };
    });
  });
}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_noria_kwinit_impl_ExternalAPI_setCursorIcon(env: JNIEnv,
                                                                        _obj: JObject,
                                                                        window_id: jlong,
                                                                        cursorIcon: JString) {
    with_panic_logger(&env, |_env| {
        WINDOWS_MAP.with(|windows_map_cell| {
            let mut windows_map_ref = windows_map_cell.borrow_mut();
            let windows_map = windows_map_ref.as_mut().expect("accessing event loop from non-main thread ot before event loop has started");
            if let Some(kwindow) = windows_map.windows.get_mut(&KWindowId(window_id)) {
                let cursor_icon = serde_json::from_str(env.get_string(cursorIcon).expect("Failed to decode string")
                  .to_str().expect("Utf8 error")).expect("Failed to parse cursorId");
                kwindow.context.window().set_cursor_icon(cursor_icon);
            };
        });
    });
}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_noria_kwinit_impl_ExternalAPI_fireUserEvent(env: JNIEnv, _obj: JObject, cookie: i64) -> bool {
    with_panic_logger(&env, |_env| {
        let s = global_state();
        let proxy = s.event_loop_proxy.as_ref().expect("event loop must have already be started!!");
        proxy.send_event(cookie).is_ok()
    }).unwrap_or(false)
}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_noria_kwinit_impl_ExternalAPI_getClipboardContent(env: JNIEnv,
                                                                              _obj: JObject) -> jstring {
    with_panic_logger(&env, |env| {
        let mut clipboard = CLIPBOARD.lock().expect("Poisoned clipboard mutex");
        match clipboard.get_contents() {
            Ok(content) => {
                env.new_string(content).unwrap().into_inner()
            }
            Err(err) => {
                log::warn!("Get clipboard content error: {}", err);
                JObject::null().into_inner()
            }
        }
    }).unwrap_or(JObject::null().into_inner())
}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_noria_kwinit_impl_ExternalAPI_setClipboardContent(env: JNIEnv,
                                                                              _obj: JObject,
                                                                              content: JString) {
    with_panic_logger(&env, |env| {
        let mut clipboard = CLIPBOARD.lock().expect("Poisoned clipboard mutex");
        if let Err(err) = clipboard.set_contents(env.get_string(content).unwrap().into()) {
            log::warn!("Set clipboard content error: {}", err);
        }
    });
}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_noria_kwinit_impl_ExternalAPI_getOuterPosition(env: JNIEnv,
                                                                           _obj: JObject,
                                                                           window_id: jlong) -> jstring {
    with_panic_logger(&env, |_env| {
        WINDOWS_MAP.with(|windows_map_cell| {
            let mut windows_map_ref = windows_map_cell.borrow_mut();
            let windows_map = windows_map_ref.as_mut().expect("accessing event loop from non-main thread ot before event loop has started");
            let kwindow = windows_map.windows.get_mut(&KWindowId(window_id)).expect("No such window");
            let scale_factor = kwindow.context.window().scale_factor();
            let outer_position: LogicalPosition<f32> = kwindow.context.window()
              .outer_position().expect("Outer position is not supported")
              .to_logical(scale_factor);
            let outerPositionJson = serde_json::to_string(&outer_position).expect("Failed to serialize LogicalPoint");
            return env.new_string(outerPositionJson).unwrap().into_inner()
        })
    }).unwrap_or(JObject::null().into_inner())
}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_noria_kwinit_impl_ExternalAPI_setOuterPosition(env: JNIEnv,
                                                                           _obj: JObject,
                                                                           window_id: jlong,
                                                                           x: jfloat,
                                                                           y: jfloat) {
    with_panic_logger(&env, |_env| {
        WINDOWS_MAP.with(|windows_map_cell| {
            let mut windows_map_ref = windows_map_cell.borrow_mut();
            let windows_map = windows_map_ref.as_mut().expect("accessing event loop from non-main thread ot before event loop has started");
            if let Some(kwindow) = windows_map.windows.get_mut(&KWindowId(window_id)) {
                kwindow.context.window().set_outer_position(Position::Logical(LogicalPosition::new(x as f64, y as f64)));
            };
        })
    });
}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_noria_kwinit_impl_ExternalAPI_setMaximized(env: JNIEnv,
                                                                         _obj: JObject,
                                                                         window_id: jlong,
                                                                         value: bool) {
    with_panic_logger(&env, |_env| {
        WINDOWS_MAP.with(|windows_map_cell| {
            let mut windows_map_ref = windows_map_cell.borrow_mut();
            let windows_map = windows_map_ref.as_mut().expect("accessing event loop from non-main thread ot before event loop has started");
            if let Some(kwindow) = windows_map.windows.get_mut(&KWindowId(window_id)) {
                kwindow.context.window().set_maximized(value);
            };
        })
    });
}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_noria_kwinit_impl_ExternalAPI_setMinimized(env: JNIEnv,
                                                                         _obj: JObject,
                                                                         window_id: jlong,
                                                                         value: bool) {
    with_panic_logger(&env, |_env| {
        WINDOWS_MAP.with(|windows_map_cell| {
            let mut windows_map_ref = windows_map_cell.borrow_mut();
            let windows_map = windows_map_ref.as_mut().expect("accessing event loop from non-main thread ot before event loop has started");
            if let Some(kwindow) = windows_map.windows.get_mut(&KWindowId(window_id)) {
                kwindow.context.window().set_minimized(value);
            };
        })
    });
}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_noria_kwinit_impl_ExternalAPI_getFullscreen(env: JNIEnv,
                                                                        _obj: JObject,
                                                                        window_id: jlong) -> jboolean {
    with_panic_logger(&env, |_env| {
        WINDOWS_MAP.with(|windows_map_cell| {
            let mut windows_map_ref = windows_map_cell.borrow_mut();
            let windows_map = windows_map_ref.as_mut().expect("accessing event loop from non-main thread ot before event loop has started");
            windows_map.windows.get(&KWindowId(window_id))
              .expect("window not found")
              .context.window().fullscreen().is_some()
        })
    }).unwrap_or(false) as u8
}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_noria_kwinit_impl_ExternalAPI_setIMEPosition(env: JNIEnv,
                                                                         _obj: JObject,
                                                                         window_id: jlong,
                                                                         x: jfloat,
                                                                         y: jfloat) {
    with_panic_logger(&env, |_env| {
        WINDOWS_MAP.with(|windows_map_cell| {
            let mut windows_map_ref = windows_map_cell.borrow_mut();
            let windows_map = windows_map_ref.as_mut().expect("accessing event loop from non-main thread ot before event loop has started");
            if let Some(kwindow) = windows_map.windows.get_mut(&KWindowId(window_id)) {
                kwindow.context.window().set_ime_position(Position::Logical(LogicalPosition::new(x as f64, y as f64)));
            };
        });
    });
}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_noria_kwinit_impl_ExternalAPI_updateAppMenu(env: JNIEnv,
                                                                        _obj: JObject,
                                                                        app_menu_json: JString) {
    with_panic_logger(&env, |_env| {
        let app_menu: ApplicationMenu = serde_json::from_str(env.get_string(app_menu_json).expect("Failed to retrieve window attributes string")
          .to_str().expect("Non Utf8 string with window attributes")).expect("Deserialization error");
        APP_MENU_STATE.with(|app_menu_state| {
            let mut app_menu_state_ref = app_menu_state.borrow_mut();
            let app_menu_state = app_menu_state_ref.as_mut().expect("Updating application menu from non-main thread ot before event loop has started");
            update_app_menu(&app_menu, app_menu_state);
        });
    });
}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_noria_kwinit_impl_ExternalAPI_setAppDockIcon(env: JNIEnv,
                                                                         _obj: JObject,
                                                                         window_id: jlong,
                                                                         app_icon_path: JString) {
    with_panic_logger(&env, |_env| {
        WINDOWS_MAP.with(|windows_map_cell| {
            let mut windows_map_ref = windows_map_cell.borrow_mut();
            let windows_map = windows_map_ref.as_mut().expect("accessing event loop from non-main thread ot before event loop has started");
            if let Some(kwindow) = windows_map.windows.get_mut(&KWindowId(window_id)) {
                set_app_dock_icon(kwindow.context.window(),
                                  env.get_string(app_icon_path).expect("Failed to retrieve app icon file path string")
                                    .to_str().expect("Non Utf8 string with window attributes"));
            };
        })
    });
}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_noria_kwinit_impl_ExternalAPI_setAppDockBadge(env: JNIEnv,
                                                                          _obj: JObject,
                                                                          text: JString) {
    with_panic_logger(&env, |_env| {
        set_app_dock_badge(env.get_string(text).expect("Failed to retrieve app dock badge text string")
          .to_str().expect("Non Utf8 string with window attributes"));
    });
}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_noria_kwinit_impl_ExternalAPI_setAppDockProgress(env: JNIEnv,
                                                                             _obj: JObject,
                                                                             progress: jdouble) {
    with_panic_logger(&env, |_env| {
        set_app_dock_progress(progress);
    });
}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_noria_kwinit_impl_ExternalAPI_openUrl(env: JNIEnv,
                                                                  _obj: JObject,
                                                                  url: JString) {
    with_panic_logger(&env, |_env| {
        let java_str_url = env.get_string(url).expect("Failed to retrieve url");
        let url = java_str_url.to_str().unwrap();
        webbrowser::open(url)
          .expect(format!("Failed to open {}", url).as_str());
    });
}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_noria_kwinit_impl_ExternalAPI_stopApplication(env: JNIEnv,
                                                                          _obj: JObject) {
    with_panic_logger(&env, |_env| {
        // TODO assert is main thread
        STOP_APPLICATION_FLAG.with(|stop_application_flag| {
            stop_application_flag.replace(true);
        });
    });
}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_noria_kwinit_impl_ExternalAPI_showNotification(env: JNIEnv,
                                                                           _obj: JObject,
                                                                           title: JString,
                                                                           message: JString) {
    let title_str = env.get_string(title).expect("Failed to retrieve title");
    let message_str = env.get_string(message).expect("Failed to retrieve title");
    with_panic_logger(&env, |_env| {
        let _ =  Notification::new()
            .summary(title_str.to_str().unwrap())
            .body(message_str.to_str().unwrap())
            .show();
    });
}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_noria_kwinit_impl_ExternalAPI_macosMoveStandardWindowButtons(env: JNIEnv,
                                                                                         _obj: JObject,
                                                                                         window_id: jlong,
                                                                                         x: jfloat,
                                                                                         y: jfloat) {
    #[cfg(target_os = "macos")] {
        use cocoa::appkit::*;
        use cocoa::base::nil;
        use cocoa::foundation::{NSPoint, NSRect, NSSize};
        use cocoa::appkit::NSWindowButton::*;
        use objc::*;
        with_panic_logger(&env, |_env| {
            WINDOWS_MAP.with(|windows_map_cell| {
                let mut windows_map_ref = windows_map_cell.borrow_mut();
                let windows_map = windows_map_ref.as_mut().expect("accessing event loop from non-main thread ot before event loop has started");
                if let Some(kwindow) = windows_map.windows.get_mut(&KWindowId(window_id)) {
                    unsafe {
                        let nswindow = kwindow.context.window().ns_window() as *mut objc::runtime::Object;
                        let content_view = NSWindow::contentView(nswindow);

                        let close = NSWindow::standardWindowButton_(nswindow, NSWindowCloseButton);
                        let miniaturize = NSWindow::standardWindowButton_(nswindow, NSWindowMiniaturizeButton);
                        let zoom = NSWindow::standardWindowButton_(nswindow, NSWindowZoomButton);

                        // kill old buttons parent so that hovering over old position does not activates them 
                        let super_super_view = NSView::superview(NSView::superview(close));
                        if content_view != super_super_view {
                            let _: () = msg_send![super_super_view, removeFromSuperview];
                        }

                        // buttons can’t be positioned individually
                        // so we take their old offset and account for it in our new view
                        let miniaturize_frame = NSView::frame(miniaturize);
                        let zoom_frame = NSView::frame(zoom);
                        let semaphore_frame = NSRect {
                            origin: NSPoint {
                                x: x as f64 - miniaturize_frame.origin.x - miniaturize_frame.size.width / 2.0,
                                y: NSView::frame(nswindow).size.height - y as f64 - miniaturize_frame.origin.y - miniaturize_frame.size.height / 2.0,
                            },
                            size: NSSize {
                                width: zoom_frame.origin.x + zoom_frame.size.width,
                                height: zoom_frame.origin.y + zoom_frame.size.height,
                            }
                        };
                        let semaphore = NSView::initWithFrame_(NSView::alloc(nil), semaphore_frame);
                        // glue our view to the top left corner
                        NSView::setAutoresizingMask_(semaphore, NSViewMaxXMargin | NSViewMinYMargin);
                        NSView::setWantsLayer(semaphore, YES);
                        NSView::addSubview_(semaphore, close);
                        NSView::addSubview_(semaphore, miniaturize);
                        NSView::addSubview_(semaphore, zoom);
                        NSView::addSubview_(content_view, semaphore);

                        // https://stackoverflow.com/a/36292700
                        // briefly change window size and back so that hover areas resets, only needed after exiting fullscreen
                        let window_frame = NSWindow::frame(nswindow);
                        let modified_window_frame = NSRect {origin: window_frame.origin, size: NSSize {width: window_frame.size.width + 1.0, height: window_frame.size.height + 1.0}};
                        let _: () = msg_send![nswindow, setFrame:modified_window_frame display:NO animate:NO];
                        let _: () = msg_send![nswindow, setFrame:window_frame display:NO animate:YES];
                    }
                };
            })
        })
    };
}

fn create_event_loop() -> EventLoop<i64> {
    #[cfg(target_os = "linux")] {
        use glutin::platform::unix::EventLoopExtUnix;
        EventLoop::<i64>::new_any_thread()
    }
    #[cfg(target_os = "windows")] {
        use glutin::platform::windows::EventLoopExtWindows;
        EventLoop::<i64>::new_any_thread()
    }
    #[cfg(all(not(target_os = "windows"), not(target_os = "linux")))] {
        EventLoop::<i64>::with_user_event()
    }
}

pub struct EventsCallback<'a> {
    pub env: &'a JNIEnv<'a>,
    pub method_id: JMethodID<'a>,
    pub callback: JObject<'a>
}

#[no_mangle]
#[allow(non_snake_case)]
pub extern "system" fn Java_noria_kwinit_impl_ExternalAPI_runEventLoop(env: JNIEnv, _obj: JObject, events_callback: JObject, redraw_callback: JObject) {
    env_logger::try_init().unwrap();
    std::panic::set_hook(Box::new(panic_hook));
    with_panic_logger(&env, |env| {
        let consume_method_id = env.get_method_id("java/util/function/Consumer", 
        "accept", 
        TypeSignature {
            args: vec![JavaType::Object("java/lang/Object".to_string())],
            ret: JavaType::Primitive(Primitive::Void),
        }.to_string()).unwrap();

        let consume_long_method_id = env.get_method_id(
            "java/util/function/LongConsumer", 
            "accept", 
            TypeSignature {
                args: vec![JavaType::Primitive(Primitive::Long)],
                ret: JavaType::Primitive(Primitive::Void),
            }.to_string()).unwrap();
        let mut event_loop = create_event_loop();
        let event_loop_proxy = event_loop.create_proxy();
        APP_MENU_STATE.with(|app_menu_state| {
            let callback = EventsCallback {
                env,
                method_id: consume_method_id,
                callback: events_callback
            };
            app_menu_state.borrow_mut().replace(init_app_menu(callback));
        });

        {
            let state: &mut GlobalState = &mut GLOBAL_STATE.lock().unwrap();
            state.event_loop_proxy = Some(event_loop_proxy);
            GLOBAL_STATE_CVAR.notify_all();
        }
        
        WINDOWS_MAP.with(|windows_map| {
            {
                *windows_map.borrow_mut().deref_mut() = Some(WindowsMap::default());
            }
            event_loop.run_return(move |event, window_target, control_flow| { 
                if let Some(kwevent) = match event {
                    Event::NewEvents(_) => {
                        // prepare for events handling
                        None
                    }
    
                    Event::UserEvent(i) => {
                        Some(KWEvent::UserEvent {cookie: i})
                    }
    
                    Event::WindowEvent { window_id, event } => {
                        let x = windows_map.borrow();
                        let windows_map = x.as_ref().unwrap();
                        if let Some(kwindow_id) = windows_map.window_id_mapping.get(&window_id) {
                            if let Some(kwindow) = windows_map.windows.get(kwindow_id) {
                                let scale_factor = kwindow.context.window().scale_factor();
                                if let Some(kwindow_event) = to_kwindow_event(scale_factor, event) {
                                    Some(KWEvent::WindowEvent {
                                        window_id: kwindow_id.clone(),
                                        event: kwindow_event
                                    })
                                } else { None }
                            } else { None }
                        } else { None }
                    }
    
                    Event::OpenUrlsEvent(urls) => {
                        Some(KWEvent::OpenUrlsEvent{ urls })
                    }
    
                    Event::OpenFilesEvent(files) => {
                        Some(KWEvent::OpenFilesEvent{ files })
                    }

                    Event::Suspended => {
                        Some(KWEvent::Suspended)
                    }
    
                    Event::Resumed => {
                        Some(KWEvent::Resumed)
                    }
    
                    Event::LoopDestroyed => {
                        Some(KWEvent::LoopDestroyed)
                    }

                    Event::Quit => {
                        Some(KWEvent::Quit)
                    }
    
                    Event::DeviceEvent { .. } => {
                        None
                        //ignore
                    }
    
                    Event::MainEventsCleared => {
                        None
                    }

                    Event::RedrawRequested(window_id) => {
                        if let Some(kwindow_id) = {
                            if let Ok(mut x) = windows_map.try_borrow_mut() {
                                if let Some(windows_map) = x.as_mut() {
                                    if let Some(kwindow_id) = windows_map.window_id_mapping.get(&window_id) {
                                        if let Some(window) = windows_map.windows.get_mut(&kwindow_id) {
                                            window.context.make_current();
                                        }
                                        Some(*kwindow_id)
                                    } else { None }
                                } else { None }
                            } else { None } 
                        } {

                            env.call_method_unchecked(redraw_callback,
                                consume_long_method_id,
                                JavaType::Primitive (Primitive::Void),
                                &[JValue::Long(kwindow_id.0)]).unwrap();

                        }
                        if let Ok(mut x) = windows_map.try_borrow_mut() {
                            if let Some(windows_map) = x.deref_mut().as_mut() {
                                if let Some(kwindow_id) = windows_map.window_id_mapping.get(&window_id) {
                                    if let Some(window) = windows_map.windows.get_mut(&kwindow_id) {
                                        window.context.swap_buffers();
                                    }
                                }
                            }
                        }
                        None
                    }
    
                    Event::RedrawEventsCleared => {
                        None
                        // Is it good place for swap buffer call?
                    }
                } {

                    with_event_loop_window_target(window_target, || {
                        let str = serde_json::to_string(&vec![kwevent]).unwrap();
                        let jstr = AutoLocal::new(env, *env.new_string(str).unwrap());
                        env.call_method_unchecked(events_callback,
                            consume_method_id,
                            JavaType::Primitive (Primitive::Void),
                            &[JValue::Object(jstr.as_obj())]).unwrap();
                    });

                    *control_flow = STOP_APPLICATION_FLAG.with(|stop_application_flag| {
                        if *stop_application_flag.borrow() {
                            ControlFlow::Exit
                        } else {
                            ControlFlow::Wait
                        }
                    });
                }
            })
        })
    });
}

fn with_event_loop_window_target<F, R>(window_target: &EventLoopWindowTarget<i64>, f: F) -> R
    where F: FnOnce() -> R
    {
        
    EVENT_LOOP_WINDOW_TARGET.with(|event_loop_window_target| {
        {
            let mut event_loop_window_target_ref = event_loop_window_target.borrow_mut();
            *event_loop_window_target_ref.deref_mut() = Some(window_target as *const EventLoopWindowTarget<i64>);
        }

        let r = f();

        {
            let mut event_loop_window_target_ref = event_loop_window_target.borrow_mut();
            *event_loop_window_target_ref.deref_mut() = None;
        }

        r
    })
}
