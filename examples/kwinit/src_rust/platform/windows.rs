#![cfg(target_os = "windows")]

use glutin::window::{Icon, Window};

use image::GenericImageView;

use crate::app_menu::ApplicationMenu;
use glutin::platform::windows::WindowExtWindows;
use winapi::shared::windef::{HWND, RECT};
use winapi::um::winuser::{WM_ACTIVATE, WM_NCCALCSIZE, WM_NCHITTEST, GetWindowRect, GetSystemMetrics, SM_CXFRAME, SM_CYFRAME, HTTOPLEFT, HTBOTTOMLEFT, HTBOTTOMRIGHT, HTTOPRIGHT, HTLEFT, HTRIGHT, HTTOP, HTBOTTOM};
use winapi::um::dwmapi::{DwmExtendFrameIntoClientArea, DwmDefWindowProc};
use winapi::um::uxtheme::MARGINS;
use winapi::shared::windowsx::{GET_X_LPARAM, GET_Y_LPARAM};
use winapi::shared::minwindef::{BOOL, TRUE, UINT, WPARAM, LPARAM, LRESULT};
use winapi::um::commctrl::{SetWindowSubclass, DefSubclassProc};
use winapi::shared::basetsd::{UINT_PTR, DWORD_PTR};
use crate::api::EventsCallback;

pub struct AppMenuState {}

pub fn init_app_menu(_callback: EventsCallback) -> AppMenuState {
    AppMenuState {}
}

pub fn update_app_menu(_menu: &ApplicationMenu, _menu_state: &mut AppMenuState) {}

pub fn set_app_dock_icon(_window: &Window, _filepath: &str) {
    if let Some(image) = image::open(_filepath).ok() {
        let (icon_width, icon_height) = image.dimensions();
        let icon = Icon::from_rgba(image.to_rgba().into_vec(), icon_width, icon_height);
        _window.set_window_icon(icon.ok());
    } else {
        log::error!("Failed to open icon path: {}", _filepath)
    }
}

pub fn set_app_dock_badge(_text: &str) {}

pub fn set_app_dock_progress(_progress: f64) {}

const WINDOW_SUBCLASS_ID: UINT_PTR = 0;
pub unsafe fn remove_system_title_bar(_window: &Window) {
    SetWindowSubclass(_window.hwnd() as HWND, Some(borderless_window_proc), WINDOW_SUBCLASS_ID, 0);
}

extern "system" fn borderless_window_proc(hwnd: HWND, msg: UINT, _w: WPARAM, _l: LPARAM, id: UINT_PTR, data: DWORD_PTR) -> LRESULT {
    if msg == WM_NCCALCSIZE && (_w as BOOL) == TRUE {
        //completely remove non-client area
        return 0;
    } else if msg == WM_NCHITTEST {
        unsafe {
            //handle window borders to be able resize
            let window_rect = window_rect(hwnd);
            let cxframe_size = GetSystemMetrics(SM_CXFRAME);
            let cyframe_size = GetSystemMetrics(SM_CYFRAME);
            let x = GET_X_LPARAM(_l);
            let y = GET_Y_LPARAM(_l);

            let left = x - window_rect.left < cxframe_size;
            let right = window_rect.right - x < cxframe_size;
            let top = y - window_rect.top < cyframe_size;
            let bottom = window_rect.bottom - y < cyframe_size;

            if left && top { return HTTOPLEFT; }
            if left && bottom { return HTBOTTOMLEFT; }
            if right && top { return HTTOPRIGHT; }
            if right && bottom { return HTBOTTOMRIGHT; }
            if left { return HTLEFT; }
            if right { return HTRIGHT; }
            if top { return HTTOP; }
            if bottom { return HTBOTTOM; }
        }
    }
    unsafe {
        return DefSubclassProc(hwnd, msg, _w, _l);
    }
}

unsafe fn window_rect(h_wnd: HWND) -> RECT {
    let mut rect = RECT { left: 0, top: 0, right: 0, bottom: 0, };
    GetWindowRect(h_wnd, &mut rect);
    rect
}