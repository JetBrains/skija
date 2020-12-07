package org.jetbrains.skija.examples.lwjgl;

import java.nio.IntBuffer;
import java.util.*;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;

public class Main {
    public static void main(String [] args) throws Exception {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        int width = (int) (vidmode.width() * 0.75);
        int height = (int) (vidmode.height() * 0.75);
        IRect bounds = IRect.makeXYWH(
                         Math.max(0, (vidmode.width() - width) / 2),
                         Math.max(0, (vidmode.height() - height) / 2),
                         width,
                         height);
        new Window().run(bounds);
    }
}

class Window {
    public long window;
    public int width;
    public int height;
    public float dpi = 1f;
    public int xpos = 0;
    public int ypos = 0;
    public boolean vsync = true;
    public boolean stats = true;
    private int[] refreshRates;
    private String os = System.getProperty("os.name").toLowerCase();

    private int[] getRefreshRates() {
        var monitors = glfwGetMonitors();
        int[] res = new int[monitors.capacity()];
        for (int i=0; i < monitors.capacity(); ++i) {
            res[i] = glfwGetVideoMode(monitors.get(i)).refreshRate();
        }
        return res;
    }

    public void run(IRect bounds) {
        refreshRates = getRefreshRates();

        createWindow(bounds);
        loop();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void updateDimensions() {
        int[] width = new int[1];
        int[] height = new int[1];
        glfwGetFramebufferSize(window, width, height);

        float[] xscale = new float[1];
        float[] yscale = new float[1];
        glfwGetWindowContentScale(window, xscale, yscale);
        assert xscale[0] == yscale[0] : "Horizontal dpi=" + xscale[0] + ", vertical dpi=" + yscale[0];

        this.width = (int) (width[0] / xscale[0]);
        this.height = (int) (height[0] / yscale[0]);
        this.dpi = xscale[0];
        System.out.println("FramebufferSize " + width[0] + "x" + height[0] + ", scale " + this.dpi + ", window " + this.width + "x" + this.height);
    }

    private void createWindow(IRect bounds) {
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        window = glfwCreateWindow(bounds.getWidth(), bounds.getHeight(), "Skija LWJGL Demo", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true);
        });

        glfwSetWindowPos(window, bounds.getLeft(), bounds.getTop());
        updateDimensions();
        xpos = width / 2;
        ypos = height / 2;

        glfwMakeContextCurrent(window);
        glfwSwapInterval(vsync ? 1 : 0); // Enable v-sync
        glfwShowWindow(window);
    }

    private DirectContext context;
    private BackendRenderTarget renderTarget;
    private Surface surface;
    private Canvas canvas;

    private void initSkia() {
        Stats.enabled = true;

        canvas = null;
        if (surface != null) { surface.close(); surface = null; }
        if (renderTarget != null) { renderTarget.close(); renderTarget = null; }

        int fbId = GL11.glGetInteger(0x8CA6); // GL_FRAMEBUFFER_BINDING
        renderTarget = BackendRenderTarget.makeGL((int) (width * dpi), (int) (height * dpi), /*samples*/0, /*stencil*/8, fbId, FramebufferFormat.GR_GL_RGBA8);

        surface = Surface.makeFromBackendRenderTarget(context, renderTarget, SurfaceOrigin.BOTTOM_LEFT, SurfaceColorFormat.RGBA_8888, ColorSpace.getDisplayP3()); // TODO load monitor profile

        canvas = surface.getCanvas();
    }

    private NavigableMap<String, Scene> scenes;
    private String currentScene;
    private long t0;
    private double[] times = new double[155];
    private int timesIdx = 0;

    private void draw() {
        long t1 = System.nanoTime();
        times[timesIdx] = (t1 - t0) / 1000000.0;
        t0 = t1;
        canvas.clear(0xFFFFFFFF);
        int count = canvas.save();
        var scene = scenes.get(currentScene);
        if (scene.scale()) {
            canvas.scale(dpi, dpi);
            // canvas.translate(0.5f, 0.5f);
        }
        scene.draw(canvas, width, height, dpi, xpos, ypos);
        canvas.restoreToCount(count);
        count = canvas.save();
        canvas.scale(dpi, dpi);
        if (stats)
            drawStats(scene);
        else if (timesIdx % 100 == 0)
            System.out.println(String.format("%.0f fps", 1000.0 / Arrays.stream(times).takeWhile(t->t>0).average().getAsDouble()));
        canvas.restoreToCount(count);
        timesIdx = (timesIdx + 1) % times.length;
        context.flush();
        glfwSwapBuffers(window);
    }

    public void drawStringCentered(String text, Rect outer, Font font, FontMetrics metrics, Canvas canvas, Paint paint) {
        Rect inner = font.measureText(text, paint);
        float innerHeight = metrics.getDescent() - metrics.getAscent();

        canvas.drawString(text,
            outer.getLeft() + (outer.getWidth() - inner.getWidth()) / 2f, 
            outer.getTop() + (outer.getHeight() - innerHeight) / 2f - metrics.getAscent(),
            font, paint);
    }

    public void drawStringLeft(String text, Rect outer, Font font, FontMetrics metrics, Canvas canvas, Paint paint) {
        Rect inner = font.measureText(text, paint);
        float innerHeight = metrics.getDescent() - metrics.getAscent();

        canvas.drawString(text,
            outer.getLeft(), 
            outer.getTop() + (outer.getHeight() - innerHeight) / 2f - metrics.getAscent(),
            font, paint);
    }

    public void drawStats(Scene scene) {
        long nativeCalls = Stats.nativeCalls;
        Stats.nativeCalls = 0;
        int allocated = Stats.allocated.values().stream().reduce(0, Integer::sum);

        Paint bg = new Paint().setColor(0x90000000);
        Paint fg = new Paint().setColor(0xFFFFFFFF);
        Paint graph = new Paint().setColor(0xFF00FF00).setStrokeWidth(1);
        Paint graphPast = new Paint().setColor(0x9000FF00).setStrokeWidth(1);
        Paint graphLimit = new Paint().setColor(0xFFcc3333).setStrokeWidth(1);
        Font font = Scene.inter13;
        FontMetrics metrics = font.getMetrics();

        float variantsHeight = scene._variants.length > 1 ? 25 : 0;

        // Background
        canvas.translate(width - 230, height - 185 - variantsHeight);
        canvas.drawRRect(RRect.makeLTRB(0, 0, 225, 180 + variantsHeight, 7), bg);
        canvas.translate(5, 5);

        // Scene
        RRect buttonBounds = RRect.makeLTRB(0, 0, 30, 20, 2);
        Rect  labelBounds = Rect.makeLTRB(35, 0, 225, 20);
        canvas.drawRRect(buttonBounds, bg);
        Scene.drawStringCentered(canvas, "←→", 15, 10, font, fg);
        int sceneIdx = 1;
        for (String sceneKey : scenes.keySet()) {
            if (sceneKey.equals(currentScene)) break;
            sceneIdx++;
        }
        drawStringLeft(sceneIdx + "/" + scenes.size() + " " + currentScene, labelBounds, font, metrics, canvas, fg);
        canvas.translate(0, 25);

        // Variants
        if (scene._variants.length > 1) {
            labelBounds = Rect.makeLTRB(35, 0, 225, 20);
            canvas.drawRRect(buttonBounds, bg);
            Scene.drawStringCentered(canvas, "↑↓", 15, 10, font, fg);
            drawStringLeft((scene._variantIdx + 1) + "/" + scene._variants.length + " " + scene.variantTitle(), labelBounds, font, metrics, canvas, fg);
            canvas.translate(0, 25);
        }

        // VSync
        buttonBounds = RRect.makeXYWH(5, 0, 20, 20, 2);
        canvas.drawRRect(buttonBounds, bg);
        Scene.drawStringCentered(canvas, "V", 15, 10, font, fg);
        drawStringLeft("VSync: " + (vsync ? "ON" : "OFF"), labelBounds, font, metrics, canvas, fg);
        canvas.translate(0, 25);

        // Stats
        canvas.drawRRect(buttonBounds, bg);
        Scene.drawStringCentered(canvas, "S", 15, 10, font, fg);
        drawStringLeft("Stats: " + (stats ? "ON" : "OFF"), labelBounds, font, metrics, canvas, fg);
        canvas.translate(0, 25);

        // GC
        canvas.drawRRect(buttonBounds, bg);
        Scene.drawStringCentered(canvas, "G", 15, 10, font, fg);
        drawStringLeft("GC objects: " + allocated, labelBounds, font, metrics, canvas, fg);
        canvas.translate(0, 25);

        // Native calls
        drawStringLeft("Native calls: " + nativeCalls, labelBounds, font, metrics, canvas, fg);
        canvas.translate(0, 25);

        // fps
        canvas.drawRRect(RRect.makeLTRB(0, 0, times.length, 45, 2), bg);
        for (int i = 0; i < times.length; ++i) {
            canvas.drawLine(i, 45, i, 45 - (float) times[i], i > timesIdx ? graphPast : graph);
        }

        for (int refreshRate : refreshRates) {
            float frameTime = 1000f / refreshRate;
            canvas.drawLine(0, 45 - frameTime, times.length, 45 - frameTime, graphLimit);
        }

        String time = String.format("%.1fms", Arrays.stream(times).takeWhile(t->t>0).average().getAsDouble());
        drawStringLeft(time, Rect.makeLTRB(times.length + 5, 0, 225, 20), font, metrics, canvas, fg);
        String fps = String.format("%.0f fps", 1000.0 / Arrays.stream(times).takeWhile(t->t>0).average().getAsDouble());
        drawStringLeft(fps, Rect.makeLTRB(times.length + 5, 25, 225, 40), font, metrics, canvas, fg);
        canvas.translate(0, 25);

        bg.close();
        fg.close();
        graph.close();
        graphPast.close();
        graphLimit.close();
    }

    private void loop() {
        GL.createCapabilities();
        if ("false".equals(System.getProperty("skija.staticLoad")))
            Library.load();
        context = DirectContext.makeGL();

        GLFW.glfwSetWindowSizeCallback(window, (window, width, height) -> {
            updateDimensions();
            initSkia();
            draw();
        });

        glfwSetCursorPosCallback(window, (window, xpos, ypos) -> {
            if(os.contains("mac") || os.contains("darwin")) {
                this.xpos = (int) xpos;
                this.ypos = (int) ypos;
            } else {
                this.xpos = (int) (xpos / dpi);
                this.ypos = (int) (ypos / dpi);
            }
        });

        glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
            // System.out.println("Button " + button + " " + (action == 0 ? "released" : "pressed"));
        });

        glfwSetScrollCallback(window, (window, xoffset, yoffset) -> {
            scenes.get(currentScene).onScroll((float) xoffset * dpi, (float) yoffset * dpi);
        });

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (action == GLFW_PRESS) {
                switch (key) {
                    case GLFW_KEY_LEFT:
                        currentScene = Optional.ofNullable(scenes.lowerKey(currentScene)).orElse(scenes.lastKey());
                        break; 
                    case GLFW_KEY_RIGHT:
                        currentScene = Optional.ofNullable(scenes.higherKey(currentScene)).orElse(scenes.firstKey());
                        break;
                    case GLFW_KEY_UP:
                        scenes.get(currentScene).changeVariant(-1);
                        break;
                    case GLFW_KEY_DOWN:
                        scenes.get(currentScene).changeVariant(1);
                        break;
                    case GLFW_KEY_V:
                        vsync = !vsync;
                        glfwSwapInterval(vsync ? 1 : 0);
                        break;
                    case GLFW_KEY_S:
                        stats = !stats;
                        break;
                    case GLFW_KEY_G:
                        System.out.println("Before GC " + Stats.allocated);
                        System.gc();
                        break;
                }
            }
        });

        initSkia();

        scenes = new TreeMap<>();
        scenes.put("Bitmap",           new BitmapScene());
        scenes.put("Blends",           new BlendsScene());
        scenes.put("Color Filters",    new ColorFiltersScene());
        scenes.put("Drawable",         new DrawableScene());
        scenes.put("Empty",            new EmptyScene());
        scenes.put("Figma",            new FigmaScene());
        scenes.put("Font",             new FontScene());
        scenes.put("Font Rendering",   new FontRenderingScene());
        scenes.put("Font Variations",  new FontVariationsScene());
        scenes.put("Geometry",         new GeometryScene());
        scenes.put("Images",           new ImagesScene());
        scenes.put("Image Filters",    new ImageFiltersScene());
        scenes.put("Mask Filters",     new MaskFiltersScene());
        scenes.put("Paragraph",        new ParagraphScene());
        scenes.put("Paragraph Metrics",new ParagraphMetricsScene());
        scenes.put("Paragraph Style",  new ParagraphStyleScene());
        scenes.put("Path Effects",     new PathEffectsScene());
        scenes.put("Paths",            new PathsScene());
        scenes.put("Picture Recorder", new PictureRecorderScene());
        scenes.put("Pythagoras",       new PythagorasScene());
        scenes.put("Shaders",          new ShadersScene());
        scenes.put("Shadow Utils",     new ShadowUtilsScene());
        scenes.put("Shapers",          new ShapersScene());
        scenes.put("RunHandler",       new RunHandlerScene());
        scenes.put("RunIterator",      new RunIteratorScene());
        scenes.put("Squares",          new SquaresScene());
        scenes.put("SVG",              new SVGScene());
        scenes.put("Swing",            new SwingScene());
        scenes.put("Text Blob",        new TextBlobScene());
        scenes.put("Text Style",       new TextStyleScene());
        scenes.put("Wall of Text",     new WallOfTextScene());
        scenes.put("Watches",          new WatchesScene());
        currentScene = "Wall of Text";
        t0 = System.nanoTime();

        while (!glfwWindowShouldClose(window)) {
            draw();
            glfwPollEvents();
        }
    }
}
