#![cfg(target_os = "linux")]

use glutin::window::Window;

use crate::app_menu::ApplicationMenu;
use crate::api::EventsCallback;

pub struct AppMenuState {}

pub fn init_app_menu(_callback: EventsCallback) -> AppMenuState {
    AppMenuState {}
}

pub fn update_app_menu(_menu: &ApplicationMenu, _menu_state: &mut AppMenuState) {}

pub fn set_app_dock_icon(_window: &Window, _filepath: &str) {}

pub fn set_app_dock_badge(_text: &str) {}

pub fn set_app_dock_progress(_progress: f64) {}
