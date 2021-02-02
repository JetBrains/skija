package noria.kwinit.impl;

import com.google.gson.*;
import java.nio.charset.*;
import java.nio.file.*;
import org.jetbrains.skija.*;

public class Main {
    public long window = -1;
    public float scale = 1f;
    public int width = 800;
    public int height = 600;
    public DirectContext context;
    public BackendRenderTarget renderTarget;
    public Surface surface;
    public Canvas canvas;

    public static void main(String[] args) throws Exception {
        System.load(java.nio.file.Path.of("target/release/libkwinit.dylib").toAbsolutePath().toString());
        new Main().run();
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
                        System.exit(0);
                    else if ("Resized".equals(type)) {
                        width = event.get("size").getAsJsonObject().get("width").getAsInt();
                        height = event.get("size").getAsJsonObject().get("height").getAsInt();
                        resize();
                    } else
                        System.out.println("WidnowEvent " + event);
                } else
                    System.out.println("Unknown event " + event);
            }
        },
        (window) -> {
            draw();
            context.flush();
            ExternalAPI.requestRedraw(window);
        });
    }

    public void draw() {
        canvas.clear(0xFFFFFFFF);
        canvas.save();
        canvas.translate(width * scale / 2, height * scale / 2);
        canvas.rotate(System.currentTimeMillis() % 5000 / 5000f * 360f);
        canvas.drawRect(Rect.makeLTRB(-100 * scale, -100 * scale, 100 * scale, 100 * scale), new Paint().setColor(0xFFCC3333));
        canvas.restore();
    }

    public void resize() {
        canvas = null;
        if (surface != null) { surface.close(); surface = null; }
        if (renderTarget != null) { renderTarget.close(); renderTarget = null; }

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

    public void handleUserEvent(int cookie) {
        switch (cookie) {
            case 0:
                window = ExternalAPI.createWindow("{\"inner_size\":{\"width\": " + width + ".0, \"height\": " + height + ".0},"
                                                  + "\"position\":{\"x\":100.0,\"y\":100.0},"
                                                  + "\"title\":\"Skija KWinit Example\"}");
                context = DirectContext.makeGL();
                scale = (float) ExternalAPI.getScaleFactor(window);
                resize();
                break;
            default:
                System.out.println("Unknown user event " + cookie);
        }
    }
}