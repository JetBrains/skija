package noria.kwinit.impl;

import com.google.gson.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;
import org.jetbrains.skija.examples.scenes.*;

public class Main {
    public static boolean verbose = false;

    public static void main(String[] args) throws Exception {
        verbose = Arrays.stream(args).anyMatch("--verbose"::equals);
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
        String library;
        if (osName.contains("mac") || osName.contains("darwin")) {
            os = "macOS";
            library = "target/release/libkwinit.dylib";
        } else if (osName.contains("windows")) {
            os = "Windows";
            library = "target\\release\\kwinit.dll";
        } else if (osName.contains("nux") || osName.contains("nix")) {
            os = "Linux";
            library = "target/release/libkwinit.so";
        } else
            throw new RuntimeException("Unknown operation system");

        System.load(java.nio.file.Path.of(library).toAbsolutePath().toString());
    }

    public void run() {
        new Thread(() -> {
            ExternalAPI.fireUserEvent(0);
        }).start();

        ExternalAPI.runEventLoop((str) -> {
            var json = new JsonParser().parse(str);
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
                        var x = position.get("x").getAsFloat();
                        var y = position.get("y").getAsFloat();
                        float relx = x, rely = y;
                        if (event.has("screen_relative_position") && event.get("screen_relative_position").isJsonObject()) {
                            var screenPosition = event.get("screen_relative_position").getAsJsonObject();
                            if (screenPosition.has("x"))
                                relx = screenPosition.get("x").getAsFloat();
                            if (screenPosition.has("y"))
                                rely = screenPosition.get("y").getAsFloat();
                        }
                        onMouseMove(x, y, relx, rely);
                    } else if ("ChangedFullscreen".equals(type)) {
                        var fullscreen = event.get("fullscreen").getAsBoolean();
                        // if (!fullscreen) 
                        //     ExternalAPI.fireUserEvent(3);
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
                    ColorSpace.getDisplayP3(), // TODO load monitor profile
                    new SurfaceProps(PixelGeometry.RGB_H));

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
        } else if ("G".equals(keyCode)) {
            System.out.println("Before GC " + Stats.allocated);
            System.gc();
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
        // canvas.save();
        // canvas.scale(scale, scale);
        // try (var paint = new Paint().setColor(0x10000000)) {
        //     canvas.drawRRect(RRect.makeXYWH(14, 14, 64, 24, 12), paint);
        // }
        // canvas.restore();
        context.flush();
        ExternalAPI.fireUserEvent(1);
    }

    public void handleUserEvent(int cookie) {
        switch (cookie) {
            case 0:
                var windowAttrs = ("{'inner_size':{'width': " + width + ".0, 'height': " + height + ".0},"
                                   + "'position':{'x':100.0,'y':100.0},"
                                   + "'title':'Skija KWinit Example'}").replaceAll("'", "\"");
                window = ExternalAPI.createWindow(windowAttrs);
                // ExternalAPI.fireUserEvent(3);
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
            case 3:
                ExternalAPI.macosMoveStandardWindowButtons(window, 46, 26);
                break;
            default:
                log("Unknown user event", cookie);
        }
    }
}