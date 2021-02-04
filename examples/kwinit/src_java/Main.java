package noria.kwinit.impl;

import com.google.gson.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.examples.scenes.*;

public class Main {
    public static boolean verbose = false;

    public static void main(String[] args) throws Exception {
        verbose = Arrays.stream(args).anyMatch("--verbose"::equals);
        System.load(java.nio.file.Path.of("target/release/libkwinit.dylib").toAbsolutePath().toString());
        new Main().run();
    }

    public static void log(Object... msg) {
        if (verbose)
            System.out.println(Arrays.stream(msg).map(Object::toString).collect(Collectors.joining(" ")));
    }

    public long window = -1;
    public float scale = 1f;
    public int width = 1280;
    public int height = 800;
    public float mouseX, mouseY, screenStartX, screenStartY, outerX, outerY;
    public boolean leftMouse = false;
    public boolean shift = false;
    public boolean ctrl = false;
    public boolean alt = false;
    public boolean logo = false;
    public DirectContext context;
    public BackendRenderTarget renderTarget;
    public Surface surface;
    public Canvas canvas;
    public String os;

    public Main() {
        var osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("mac") || osName.contains("darwin"))
            os = "macOS";
        else if (os.contains("windows"))
            os = "Windows";
        else if (os.contains("nux") || os.contains("nix"))
            os = "Linux";
    }

    public void run() {
        new Thread(() -> {
            ExternalAPI.fireUserEvent(0);
        }).start();

        ExternalAPI.runEventLoop((bb) -> {
            var bytes = new byte[bb.limit()];
            bb.get(bytes);
            var json = new JsonParser().parse(new String(bytes));
            for (var el: json.getAsJsonArray()) {
                var event = el.getAsJsonObject();
                var type = event.get("type").getAsString();
                if ("UserEvent".equals(type))
                    handleUserEvent(event.get("cookie").getAsInt());
                else if ("WindowEvent".equals(type)) {
                    event = event.get("event").getAsJsonObject();
                    type = event.get("type").getAsString();
                    if ("CloseRequested".equals(type))
                        onClose();
                    else if ("Resized".equals(type)) {
                        onResize(event.get("size").getAsJsonObject().get("width").getAsInt(),
                                 event.get("size").getAsJsonObject().get("height").getAsInt());
                    } else if ("KeyboardInput".equals(type)) {
                        event = event.get("input").getAsJsonObject();
                        if ("Pressed".equals(event.get("state").getAsString())) {
                            var keyCode = event.get("virtual_key_code").getAsString();
                            onKeyDown(keyCode);
                        } else
                            log("KeyboardInput", event);
                    } else if ("ModifiersChanged".equals(type)) {
                        event = event.get("state").getAsJsonObject();
                        onModifiersChange(event.get("shift").getAsBoolean(),
                                          event.get("ctrl").getAsBoolean(),
                                          event.get("alt").getAsBoolean(),
                                          event.get("logo").getAsBoolean());
                    } else if ("MouseInput".equals(type)) {
                        var button = event.get("button").getAsString();
                        var state = event.get("state").getAsString();
                        if ("Pressed".equals(state))
                            onMouseDown(button);
                        else if ("Released".equals(state))
                            onMouseUp(button);
                        else
                            log("MouseInput", event);
                    } else if ("CursorMoved".equals(type)) {
                        var position = event.get("position").getAsJsonObject();
                        var screenPosition = event.get("screen_relative_position").getAsJsonObject();
                        onMouseMove(position.get("x").getAsFloat(), position.get("y").getAsFloat(),
                                    screenPosition.get("x").getAsFloat(), screenPosition.get("y").getAsFloat());
                    } else
                        log("WidnowEvent", event);
                } else
                    log("Unknown event", event);
            }
        },
        (window) -> {
            onDraw();
        });
    }

    public void onClose() {
        ExternalAPI.destroyWindow(window);
        ExternalAPI.stopApplication();
    }

    public void onResize(int width, int height) {
        this.width = width;
        this.height = height;

        if (surface != null)
            surface.close();
        if (renderTarget != null)
            renderTarget.close();

        renderTarget = BackendRenderTarget.makeGL(
                         (int) (width * scale),
                         (int) (height * scale),
                         /*samples*/0,
                         /*stencil*/8,
                         /*fbId*/0,
                         FramebufferFormat.GR_GL_RGBA8);

        surface = Surface.makeFromBackendRenderTarget(
                    context,
                    renderTarget,
                    SurfaceOrigin.BOTTOM_LEFT,
                    SurfaceColorFormat.RGBA_8888,
                    ColorSpace.getDisplayP3()); // TODO load monitor profile

        canvas = surface.getCanvas();
    }

    public void onModifiersChange(boolean shift, boolean ctrl, boolean alt, boolean logo) {
         this.shift = shift;
         this.ctrl = ctrl;
         this.alt = alt;
         this.logo = logo;
    }

    public void onKeyDown(String keyCode) {
        if ("Left".equals(keyCode))
            Scenes.prevScene();
        else if ("Right".equals(keyCode))
            Scenes.nextScene();
        else if ("Up".equals(keyCode))
            Scenes.currentScene().changeVariant(-1);
        else if ("Down".equals(keyCode))
            Scenes.currentScene().changeVariant(1);
        else if ("S".equals(keyCode)) {
            Scenes.stats = !Scenes.stats;
            Stats.enabled = Scenes.stats;
        } else if ("macOS".equals(os) && logo && "Q".equals(keyCode))
            onClose();
        else if (("Windows".equals(os) || "Linux".equals(os)) && ((alt && "F4".equals(keyCode)) || (ctrl && "W".equals(keyCode))))
            onClose();
        else
            log("KeyDown", keyCode);
    }

    public void onMouseMove(float x, float y, float screenX, float screenY) {
        mouseX = x;
        mouseY = y;
        if (leftMouse) {
            outerX = screenX - screenStartX;
            outerY = screenY - screenStartY;
            ExternalAPI.fireUserEvent(2);
        }
    }

    public void onMouseDown(String button) {
        leftMouse = true;
        screenStartX = mouseX;
        screenStartY = mouseY;
    }

    public void onMouseUp(String button) {
        leftMouse = false;
        screenStartX = 0f;
        screenStartY = 0f;
    }

    public void onDraw() {
        Scenes.draw(canvas, width, height, scale, (int) mouseX, (int) mouseY);
        context.flush();
        ExternalAPI.fireUserEvent(1);
    }

    public void handleUserEvent(int cookie) {
        switch (cookie) {
            case 0:
                window = ExternalAPI.createWindow("{\"inner_size\":{\"width\": " + width + ".0, \"height\": " + height + ".0},"
                                                  + "\"position\":{\"x\":100.0,\"y\":100.0},"
                                                  + "\"title\":\"Skija KWinit Example\"}");
                Stats.enabled = true;
                context = DirectContext.makeGL();
                scale = (float) ExternalAPI.getScaleFactor(window);
                mouseX = width / 2f;
                mouseY = height / 2f;
                onResize(width, height);
                break;
            case 1:
                ExternalAPI.requestRedraw(window);
                break;
            case 2:
                ExternalAPI.setOuterPosition(window, outerX, outerY);
                break;
            default:
                log("Unknown user event", cookie);
        }
    }
}