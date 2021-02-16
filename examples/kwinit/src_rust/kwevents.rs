use glutin::dpi::{LogicalPosition, LogicalSize};
use serde::{Deserialize, Serialize};

#[derive(Clone, Debug, Copy, PartialEq, Eq, Hash, Serialize)]
pub struct KWindowId(pub i64);

#[derive(Clone, Debug, Copy, PartialEq, Eq, Hash, Serialize)]
pub struct KWDeviceId(pub i64);

#[derive(Debug, PartialEq, Serialize)]
#[serde(tag = "type")]
pub enum KWEvent {
    WindowEvent {
        window_id: KWindowId,
        event: KWindowEvent
    },
    UserEvent {
        cookie: i64
    },
    OpenFilesEvent {
        files: Vec<String>
    },
    OpenUrlsEvent {
        urls: Vec<String>
    },
    Suspended,
    Resumed,
    LoopDestroyed,
    Quit
}

#[derive(Debug, PartialEq, Serialize)]
pub struct KWKeyboardInput {
    scancode: u32,
    state: glutin::event::ElementState,
    virtual_key_code: Option<glutin::event::VirtualKeyCode>,
    is_synthetic: bool
}

#[derive(Debug, PartialEq, Serialize)]
#[serde(tag = "type")]
pub enum KWindowEvent {
    Resized {
        size: LogicalSize<f32>
    },
    Moved {
        position: LogicalPosition<f32>
    },
    CloseRequested,
    Destroyed,
    DroppedFile {
        file: String
    },
    HoveredFile {
        file: String
    },
    HoveredFileCancelled,
    ReceivedCharacter {
        codepoint: i32
    },
    Focused {
        focused: bool
    },
    ChangedFullscreen {
        fullscreen: bool
    },
    KeyboardInput {
        device_id: KWDeviceId,
        input: KWKeyboardInput,
    },
    ModifiersChanged {
        state: glutin::event::ModifiersState
    },
    CursorMoved {
        device_id: KWDeviceId,
        position: LogicalPosition<f32>,
        screen_relative_position: Option<LogicalPosition<f32>>
    },
    CursorEnteredWindow {
        device_id: KWDeviceId 
    },
    CursorLeftWindow {
        device_id: KWDeviceId 
    },
    MouseWheel {
        device_id: KWDeviceId,
        delta: LogicalPosition<f32>,
    },
    MouseInput {
        device_id: KWDeviceId,
        state: glutin::event::ElementState,
        button: glutin::event::MouseButton
    },
    TouchpadPressure {
        device_id: KWDeviceId,
        pressure: f32,
        stage: i64,
    },
    ScaleFactorChanged {
        scale_factor: f64,
        new_inner_size: LogicalSize<f32>,
    },
    ThemeChanged {
        theme: Theme
    },
}

#[derive(Debug, PartialEq, Serialize)]
pub enum Theme {
    Light, Dark
}

#[derive(PartialEq, Deserialize, Clone, Debug)]
pub struct Callback {
    pub cookie: i64
}

#[allow(unused_variables)]
pub fn to_kwindow_event(scale_factor: f64, event: glutin::event::WindowEvent) -> Option<KWindowEvent> {
    match event {
        glutin::event::WindowEvent::Resized(size) =>
            Some(KWindowEvent::Resized { size: size.to_logical(scale_factor) }),
        glutin::event::WindowEvent::Moved(pos) =>
            Some(KWindowEvent::Moved { position: pos.to_logical(scale_factor) }),
        glutin::event::WindowEvent::CloseRequested =>
            Some(KWindowEvent::CloseRequested),
        glutin::event::WindowEvent::Destroyed =>
            Some(KWindowEvent::Destroyed),
        glutin::event::WindowEvent::DroppedFile(path) =>
            Some(KWindowEvent::DroppedFile {
                file: path.to_str().unwrap().into()
            }),
        glutin::event::WindowEvent::HoveredFile(path) =>
            Some(KWindowEvent::HoveredFile {
                file: path.to_str().unwrap().into()
            }),
        glutin::event::WindowEvent::HoveredFileCancelled =>
            Some(KWindowEvent::HoveredFileCancelled),
        glutin::event::WindowEvent::ReceivedCharacter(codepoint) =>
            Some(KWindowEvent::ReceivedCharacter {
                codepoint: codepoint as i32
            }),
        glutin::event::WindowEvent::Focused(focused) =>
            Some(KWindowEvent::Focused {
                focused: focused
            }),
        glutin::event::WindowEvent::KeyboardInput { device_id, input, is_synthetic } =>
            Some(KWindowEvent::KeyboardInput {
                input: KWKeyboardInput {
                    scancode: input.scancode,
                    state: input.state,
                    virtual_key_code: input.virtual_keycode,
                    is_synthetic: is_synthetic
                },
                device_id: KWDeviceId(0)
            }),
        glutin::event::WindowEvent::ModifiersChanged(modifiers_state) =>
            Some(KWindowEvent::ModifiersChanged {
                state: modifiers_state
            }),
        #[allow(deprecated)]
        glutin::event::WindowEvent::CursorMoved { device_id, position, screen_relative_position, modifiers } =>
            Some(KWindowEvent::CursorMoved {
                device_id: KWDeviceId(0), // TODO remove?
                position: position.to_logical(scale_factor),
                screen_relative_position: screen_relative_position.map(|srp| srp.to_logical(scale_factor))
            }),
        glutin::event::WindowEvent::CursorEntered { device_id } =>
            Some(KWindowEvent::CursorEnteredWindow {
                device_id: KWDeviceId(0)
            }),
        glutin::event::WindowEvent::CursorLeft { device_id } =>
            Some(KWindowEvent::CursorLeftWindow {
                device_id: KWDeviceId(0)
            }),
        glutin::event::WindowEvent::MouseWheel { device_id, delta, .. } => {
            match delta {
                glutin::event::MouseScrollDelta::LineDelta(delta_line_x, delta_line_y) => {
                    const X_SCROLL_STEP: f32 = 38.0;
                    const Y_SCROLL_STEP: f32 = 38.0;

                    Some(KWindowEvent::MouseWheel {
                        device_id: KWDeviceId(0),
                        delta: LogicalPosition::new(delta_line_x * X_SCROLL_STEP,
                                                    delta_line_y * Y_SCROLL_STEP)
                    })
                }
                glutin::event::MouseScrollDelta::PixelDelta(pos) =>
                    Some(KWindowEvent::MouseWheel {
                        device_id: KWDeviceId(0),
                        delta: pos.to_logical(scale_factor)
                    })
            }
        }
        glutin::event::WindowEvent::MouseInput { device_id, state, button, .. } =>
            Some(KWindowEvent::MouseInput {
                device_id: KWDeviceId(0),
                state,
                button
            }),
        glutin::event::WindowEvent::TouchpadPressure { device_id, pressure, stage } =>
            Some(KWindowEvent::TouchpadPressure {
                device_id: KWDeviceId(0),
                pressure,
                stage
            }),
        glutin::event::WindowEvent::AxisMotion { device_id, axis, value } => None,
        glutin::event::WindowEvent::Touch(_) => None,
        glutin::event::WindowEvent::ScaleFactorChanged { scale_factor, new_inner_size } =>
            Some(KWindowEvent::ScaleFactorChanged {
                scale_factor,
                new_inner_size: new_inner_size.to_logical(scale_factor)
            }),
        glutin::event::WindowEvent::ThemeChanged(theme) => {
            Some(KWindowEvent::ThemeChanged {
                theme: match theme {
                    glutin::window::Theme::Light => Theme::Light,
                    glutin::window::Theme::Dark => Theme::Dark
                }
            })
        }
        glutin::event::WindowEvent::ChangedFullscreen(fullscreen) => {
            Some(KWindowEvent::ChangedFullscreen {
                fullscreen
            })
        }
    }
}
