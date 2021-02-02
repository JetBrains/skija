#![cfg(target_os = "macos")]
#![allow(non_upper_case_globals)]

use std::os::raw::c_void;

use cocoa::appkit::*;
use cocoa::base::{id, Nil};
use cocoa::base::nil;
use cocoa::foundation::{NSPoint, NSRect, NSSize, NSString, NSUInteger};
use glutin::event::VirtualKeyCode;
use glutin::window::Window;
use objc::*;
use objc::declare::ClassDecl;
use objc::runtime::{NO, Object, object_getClass, Sel, YES};

use crate::app_menu::ApplicationMenu;
use jni::signature::{JavaType, Primitive};
use jni::objects::JValue;
use crate::api::EventsCallback;
use crate::kwevents::KWEvent;

pub struct AppMenuState {
    target: *const Object,
}

const NSEventModifierFlagShift: i64 = 1 << 17;
const NSEventModifierFlagControl: i64 = 1 << 18;
const NSEventModifierFlagOption: i64 = 1 << 19;
const NSEventModifierFlagCommand: i64 = 1 << 20;

fn keycode_to_menu_key(keycode: VirtualKeyCode) -> &'static str {
    match keycode {
        VirtualKeyCode::Grave => "`",
        VirtualKeyCode::Key0 => "0",
        VirtualKeyCode::Key1 => "1",
        VirtualKeyCode::Key2 => "2",
        VirtualKeyCode::Key3 => "3",
        VirtualKeyCode::Key4 => "4",
        VirtualKeyCode::Key5 => "5",
        VirtualKeyCode::Key6 => "6",
        VirtualKeyCode::Key7 => "7",
        VirtualKeyCode::Key8 => "8",
        VirtualKeyCode::Key9 => "9",
        VirtualKeyCode::Minus => "-",
        VirtualKeyCode::Equals => "=",

        VirtualKeyCode::Q => "q",
        VirtualKeyCode::W => "w",
        VirtualKeyCode::E => "e",
        VirtualKeyCode::R => "r",
        VirtualKeyCode::T => "t",
        VirtualKeyCode::Y => "y",
        VirtualKeyCode::U => "u",
        VirtualKeyCode::I => "i",
        VirtualKeyCode::O => "o",
        VirtualKeyCode::P => "p",
        VirtualKeyCode::LBracket => "[",
        VirtualKeyCode::RBracket => "]",

        VirtualKeyCode::A => "a",
        VirtualKeyCode::S => "s",
        VirtualKeyCode::D => "d",
        VirtualKeyCode::F => "f",
        VirtualKeyCode::G => "g",
        VirtualKeyCode::H => "h",
        VirtualKeyCode::J => "j",
        VirtualKeyCode::K => "l",
        VirtualKeyCode::L => "l",
        VirtualKeyCode::Semicolon => ";",
        VirtualKeyCode::Apostrophe => "'",
        VirtualKeyCode::Backslash => "\\",

        VirtualKeyCode::Z => "z",
        VirtualKeyCode::X => "x",
        VirtualKeyCode::C => "c",
        VirtualKeyCode::V => "v",
        VirtualKeyCode::B => "b",
        VirtualKeyCode::N => "n",
        VirtualKeyCode::M => "m",
        VirtualKeyCode::Comma => ",",
        VirtualKeyCode::Period => ".",
        VirtualKeyCode::Slash => "/",
        _ => ""
    }
}

pub fn set_app_dock_icon(_window: &Window, filepath: &str) {
    unsafe {
        let ns_filepath = NSString::alloc(nil).init_str(filepath);
        let ns_image = NSImage::initWithContentsOfFile_(NSImage::alloc(nil), ns_filepath);
        NSApplication::setApplicationIconImage_(NSApp(), ns_image);
    }
}

pub fn set_app_dock_badge(text: &str) {
    unsafe {
        let ns_string = NSString::alloc(nil).init_str(text);
        NSDockTile::setBadgeLabel_(getDockTile(), ns_string);
    }
}

pub fn set_app_dock_progress(progress: f64) {
    unsafe {
        let ns_image = msg_send![NSApp(), applicationIconImage];
        let dock_tile: id = getDockTile();
        let content_view: id = msg_send![dock_tile, contentView];
        let subviews: id = msg_send![content_view, subviews];
        let existing_view: id = if subviews != nil { msg_send![subviews, lastObject] } else { nil };
        let existing_view_class = if existing_view != nil { object_getClass(existing_view) } else { Nil };
        let progress_indicator: id;
        if existing_view_class == class!(NSProgressIndicator) {
            progress_indicator = msg_send![subviews, lastObject]
        } else {
            let image_view: id = NSImageView::alloc(nil);
            let _: id = msg_send![image_view, init];
            NSImageView::setImage_(image_view, ns_image);
            let _: () = msg_send![dock_tile, setContentView:image_view];

            let size: NSSize = msg_send![dock_tile, size];
            let bounds = NSRect::new(NSPoint::new(3.0, 0.0), NSSize::new(size.width - 6.0, 20.0));
            progress_indicator = NSProgressIndicator::initWithFrame_(NSProgressIndicator::alloc(nil), bounds);
            let _: () = msg_send![progress_indicator, setStyle: NSProgressIndicatorBarStyle];
            let _: () = msg_send![progress_indicator, setBezeled: YES];
            let _: () = msg_send![progress_indicator, setIndeterminate: NO];
            let _: () = msg_send![progress_indicator, setMinValue: 0.0];
            let _: () = msg_send![progress_indicator, setMaxValue: 100.0];
            let _: () = msg_send![content_view, addSubview: progress_indicator];
        }

        if progress >= 0.0 && progress <= 100.0 {
            let _: () = msg_send![progress_indicator, setDoubleValue: progress];
            let _: () = msg_send![progress_indicator, setHidden: NO];
        } else {
            let _: () = msg_send![progress_indicator, setHidden: YES];
        }
        // TODO: actually calling [dock_tile display] should be enough, but it doesn't work
        NSApplication::setApplicationIconImage_(NSApp(), ns_image);
    }
}

pub fn update_app_menu(menu: &ApplicationMenu, menu_state: &mut AppMenuState) {
    unsafe fn make_menu(
        parent_menu: id,
        menu: &ApplicationMenu,
        target: *const Object,
    ) {
        match menu {
            ApplicationMenu::Main { items } => {
                let main_menu: id = NSMenu::new(nil);
                main_menu.setAutoenablesItems(NO);

                for item in items {
                    make_menu(main_menu, item, target);
                }
                NSApplication::setMainMenu_(NSApp(), main_menu);
            }
            ApplicationMenu::Sub { name, items } => {
                let sub_menu = NSMenu::new(nil).initWithTitle_(NSString::alloc(nil).init_str(name));
                sub_menu.setAutoenablesItems(NO);

                // append item to parent
                let sub_item: id = parent_menu.addItemWithTitle_action_keyEquivalent(
                    NSString::alloc(nil).init_str(name),
                    sel!(submenu:),
                    NSString::alloc(nil).init_str("")
                );
                sub_item.setSubmenu_(sub_menu);

                for item in items {
                    make_menu(sub_menu, item, target);
                }
            }
            ApplicationMenu::Item { name, command, enabled } => {
                let shortcut = match command.keycode {
                    Some(keycode) => keycode_to_menu_key(keycode),
                    None => ""
                };
                let sub_item: id = parent_menu.addItemWithTitle_action_keyEquivalent(
                    NSString::alloc(nil).init_str(name),
                    sel!(menuAction:),
                    NSString::alloc(nil).init_str(shortcut)
                );
                let modifier = match command.modifier {
                    Some(modifier) =>
                        (if modifier.alt() {NSEventModifierFlagOption} else {0}) |
                            (if modifier.shift() {NSEventModifierFlagShift} else {0}) |
                            (if modifier.ctrl() {NSEventModifierFlagControl} else {0}) |
                            (if modifier.logo() {NSEventModifierFlagCommand} else {0})
                    ,
                    None => 0
                };
                let () = msg_send![sub_item, setKeyEquivalentModifierMask: modifier];

                let id_holder: id = msg_send![class!(NSNumber), numberWithInteger: command.callback.cookie];
                let () = msg_send![sub_item, setRepresentedObject: id_holder];
                let () = msg_send![sub_item, setTarget: target];
                let () = msg_send![sub_item, setEnabled: if *enabled { YES } else { NO }];
            }
            ApplicationMenu::Line => {
                let sep_item: id = NSMenuItem::separatorItem(nil);
                parent_menu.addItem_(sep_item);
            }
        }
    }
    unsafe {
        make_menu(nil, menu, menu_state.target);
    }
}

const ACTIONS_HANDLER: &str = "actions_handler";

fn define_menu_target() -> id {
    extern fn menu_action(this: &Object, _sel: Sel, item: id) {
        unsafe {
            let description: id = msg_send![item, representedObject];
            let cookie: i64 = msg_send![description, integerValue];
            let handler: *mut c_void = *this.get_ivar(ACTIONS_HANDLER);
            let callback = &*(handler as *const EventsCallback);

            let kwevent = KWEvent::UserEvent {cookie};
            let str = serde_json::to_string(&vec![kwevent]).unwrap();
            let mut slice = str.as_bytes().to_vec();
            let byte_buffer = callback.env.new_direct_byte_buffer(&mut slice).unwrap().into();
            callback.env.call_method_unchecked(callback.callback,
                                      callback.method_id,
                                      JavaType::Primitive (Primitive::Void),
                                      &[JValue::Object(byte_buffer)]).unwrap();
        }
    };

    let superclass = class!(NSObject);
    let mut decl = ClassDecl::new("MenuTarget", superclass).unwrap();
    unsafe {
        decl.add_method(sel!(menuAction:), menu_action as extern fn(&Object, Sel, id));
    }
    decl.add_ivar::<*mut c_void>(ACTIONS_HANDLER);
    let target_class = decl.register();
    return unsafe {
        msg_send![target_class, new]
    };
}

pub fn init_app_menu(callback: EventsCallback) -> AppMenuState {
    let target: id = define_menu_target();
    let proxy = Box::leak(Box::new(callback));
    unsafe {
        (*target).set_ivar(ACTIONS_HANDLER, proxy as *mut _ as *mut c_void);
    }
    return AppMenuState {
        target,
    };
}

#[allow(non_snake_case)]
unsafe fn getDockTile() -> id {
    return msg_send![NSApp(), dockTile];
}

#[allow(non_snake_case)]
pub trait NSDockTile: Sized {
    unsafe fn setBadgeLabel_(self, badge_label: id /* NSString */);

    unsafe fn display_(self);
}

#[allow(non_snake_case)]
impl NSDockTile for id {
    unsafe fn setBadgeLabel_(self, badge_label: id /* NSString */) {
        let () = msg_send![self, setBadgeLabel:badge_label];
    }

    unsafe fn display_(self) {
        let () = msg_send![self, display];
    }
}

const NSProgressIndicatorBarStyle: NSUInteger = 0;
// const NSProgressIndicatorSpinningStyle: NSUInteger = 1;

#[allow(non_snake_case)]
pub trait NSProgressIndicator: Sized {
    unsafe fn alloc(_: Self) -> id {
        msg_send![class!(NSProgressIndicator), alloc]
    }
    unsafe fn initWithFrame_(self, rect: NSRect) -> id;
}

#[allow(non_snake_case)]
impl NSProgressIndicator for id {
    unsafe fn initWithFrame_(self, rect: NSRect) -> id {
        msg_send![self, initWithFrame:rect]
    }
}
