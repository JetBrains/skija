/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

// Based on https://github.com/servo/webrender/blob/b912432/wrench/src/angle.rs
use glutin::{self, ContextBuilder, ContextCurrentState, CreationError};
use glutin::event_loop::EventLoopWindowTarget;
use glutin::window::{Window, WindowBuilder};

#[cfg(windows)]
pub use crate::egl::Context;

#[cfg(not(windows))]
pub enum Context {}

impl Context {
    #[cfg(not(windows))]
    pub fn with_window<T: ContextCurrentState, Msg>(
        _: WindowBuilder,
        _: ContextBuilder<'_, T>,
        _: &EventLoopWindowTarget<Msg>,
    ) -> Result<(Window, Self), CreationError> {
        Err(CreationError::PlatformSpecific(
            "ANGLE rendering is only supported on Windows".into(),
        ))
    }

    #[cfg(windows)]
    pub fn with_window<T: ContextCurrentState, Msg>(
        window_builder: WindowBuilder,
        context_builder: ContextBuilder<'_, T>,
        events_loop: &EventLoopWindowTarget<Msg>,
    ) -> Result<(Window, Self), CreationError> {
        use glutin::platform::windows::WindowExtWindows;

        let pf_reqs = &context_builder.pf_reqs;
        let gl_attr = &context_builder.gl_attr.map_sharing(|_| unimplemented!());
        let window = window_builder.build(events_loop)?;
        Self::new(pf_reqs, gl_attr)
            .and_then(|p| p.finish(window.hwnd() as _))
            .map(|context| (window, context))
    }

    #[cfg(not(windows))]
    pub unsafe fn make_current(&self) -> Result<(), glutin::ContextError> {
        match *self {}
    }
    
    #[cfg(not(windows))]
    pub fn swap_buffers(&self) -> Result<(), glutin::ContextError> {
        match *self {}
    }
}
