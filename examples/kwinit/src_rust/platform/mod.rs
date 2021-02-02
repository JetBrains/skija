#[cfg(target_os = "linux")]
pub use self::linux::{AppMenuState, init_app_menu, set_app_dock_badge, set_app_dock_icon, set_app_dock_progress, update_app_menu};
#[cfg(target_os = "macos")]
pub use self::macos::{AppMenuState, init_app_menu, set_app_dock_badge, set_app_dock_icon, set_app_dock_progress, update_app_menu};
#[cfg(target_os = "windows")]
pub use self::windows::{AppMenuState, init_app_menu, set_app_dock_badge, set_app_dock_icon, set_app_dock_progress, update_app_menu, remove_system_title_bar};

#[cfg(target_os = "macos")]
mod macos;
#[cfg(target_os = "linux")]
mod linux;
#[cfg(target_os = "windows")]
mod windows;
