use std::env;

use glutin::{ContextBuilder, ContextCurrentState, PossiblyCurrent, WindowedContext};
use glutin::dpi::PhysicalSize;
use glutin::event_loop::EventLoopWindowTarget;
use glutin::window::{Window, WindowBuilder};

use crate::angle::Context;

pub enum WindowedContextWrapper {
    Default(Option<WindowedContext<PossiblyCurrent>>),
    Angle(glutin::window::Window, crate::angle::Context),
}

pub(crate) fn create_windowed_context_wrapper<T: ContextCurrentState, Msg>(window_builder: WindowBuilder,
                                                                           context_builder: ContextBuilder<'_, T>,
                                                                           event_loop_window_target: &EventLoopWindowTarget<Msg>) -> WindowedContextWrapper {
    if env::var("KWINIT_ANGLE").is_ok() {
        #[allow(unused_variables)] // suppress false positive unused window and windowed_context vars
        let (window, windowed_context) = Context::with_window(window_builder, context_builder, event_loop_window_target).unwrap();
        unsafe {
            windowed_context.make_current().unwrap();
        }
        WindowedContextWrapper::Angle(window, windowed_context)
    } else {
        let windowed_context = unsafe {
            context_builder
                .build_windowed(window_builder, event_loop_window_target)
                .expect("Failed to create window.")
                .make_current()
                .expect("Can't make context current")
        };
        WindowedContextWrapper::Default(Some(windowed_context))
    }
}

impl WindowedContextWrapper {
    pub fn swap_buffers(&self) {
        match *self {
            WindowedContextWrapper::Default(ref windowed_context) => {
                windowed_context.as_ref().unwrap().swap_buffers().expect("Can't swap buffers");
            }
            WindowedContextWrapper::Angle(_, ref windowed_context) => {
                windowed_context.swap_buffers().expect("Can't swap buffers");
            }
        };
    }

    pub fn window(&self) -> &Window {
        match *self {
            WindowedContextWrapper::Default(ref windowed_context) => {
                &windowed_context.as_ref().unwrap().window()
            }
            WindowedContextWrapper::Angle(ref window, _) => {
                window
            }
        }
    }

    pub fn make_current(&mut self) {
        match *self {
            WindowedContextWrapper::Default(ref mut windowed_context) => {
                unsafe {
                    let real_context = windowed_context.take().unwrap();
                    let current_context = real_context.make_current().expect("Can't make current");
                    windowed_context.replace(current_context);
                }
            }
            WindowedContextWrapper::Angle(_, ref windowed_context) => {
                unsafe {
                    windowed_context.make_current().expect("Can't make current");
                }
            }
        };
    }

    pub fn resize(&mut self, size: PhysicalSize<u32>) {
        match *self {
            WindowedContextWrapper::Default(ref windowed_context) => {
                let context = windowed_context.as_ref().unwrap();
                context.resize(size)
            }
            WindowedContextWrapper::Angle(ref mut window, _) => {
                window.set_inner_size(size)
            }
        };
    }
}