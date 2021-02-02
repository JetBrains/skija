use glutin::event::{ModifiersState, VirtualKeyCode};
use serde::Deserialize;

pub use crate::platform::{AppMenuState, init_app_menu, update_app_menu};
pub use crate::kwevents::Callback;

#[derive(PartialEq, Deserialize, Clone, Debug)]
pub struct Command {
    pub keycode: Option<VirtualKeyCode>,
    pub modifier: Option<ModifiersState>,
    pub callback: Callback,
}

#[derive(PartialEq, Deserialize, Clone, Debug)]
#[serde(tag = "type")]
pub enum ApplicationMenu {
    Main { items: Vec<ApplicationMenu> },
    Sub { name: String, items: Vec<ApplicationMenu> },
    Item { name: String, enabled: bool, command: Command },
    Line,
}