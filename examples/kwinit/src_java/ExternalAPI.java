package noria.kwinit.impl;

import java.nio.*;
import java.util.function.*;

public class ExternalAPI {
    public static native long    createWindow(String attributes);
    public static native void    runEventLoop(Consumer<String> callback, LongConsumer redrawCallback);
    public static native boolean fireUserEvent(long cookie);
    public static native double  getScaleFactor(long window);
    public static native void    requestRedraw(long window);
    public static native String  getOuterPosition(long window);
    public static native void    setOuterPosition(long window, float x, float y);
    public static native void    destroyWindow(long window);
    public static native void    stopApplication();

    public static native void    macosMoveStandardWindowButtons(long window, float center_x, float center_y);

    // public static native  getInnerSize(windowId: Long): String
    // public static native  setInnerSize(windowId: Long, width: Float, height: Float)
    // public static native  resize(windowId: Long, width: Long, height: Long)
    // public static native  setMinimized(windowId: Long, value: Boolean)
    // public static native  setMaximized(windowId: Long, value: Boolean)
    // public static native  getFullscreen(windowId: Long): Boolean
    // public static native  setCursorIcon(windowId: Long, cursorIconJson: String)
    // public static native  getClipboardContent(): String?
    // public static native  setClipboardContent(content: String)
    // public static native  setIMEPosition(windowId: KWindowId, x: Float, y: Float)
    // public static native  updateAppMenu(appMenuJson: String)
    // public static native  setAppDockIcon(windowId: KWindowId, appIconPath: String)
    // public static native  setAppDockBadge(text: String)
    // public static native  setAppDockProgress(progress: Double)
    // public static native  openUrl(url: String)
    // public static native  makeCurrent(windowId: KWindowId)
    // public static native  showNotification(title: String, message: String)
}