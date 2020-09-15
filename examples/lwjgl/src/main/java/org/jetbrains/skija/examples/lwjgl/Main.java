package org.jetbrains.skija.examples.lwjgl;

import java.nio.IntBuffer;
import java.util.*;

import org.jetbrains.skija.impl.Stats;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.jetbrains.skija.*;

public class Main {
    public static void main(String [] args) throws Exception {
        Window.lwjglInit();
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        new Window((int) (vidmode.width() * 0.75), (int) (vidmode.height() * 0.75)).run();
    }
}

class Window {
    public long window;
    public int width;
    public int height;
    public float dpi = 1f;
    public int left;
    public int top;
    public int xpos = 0;
    public int ypos = 0;
    public boolean vsync = true;
    public boolean stats = true;
    private Typeface interRegular;
    private Font     interRegular13tnum;
    private int[] refreshRates;

    public Window(int width, int height) {
        this.width = width;
        this.height = height;
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        this.left = Math.max(0, (vidmode.width() - width) / 2);
        this.top = Math.max(0, (vidmode.height() - height) / 2);
        xpos = width / 2;
        ypos = height / 2;
        refreshRates = getRefreshRates();
    }

    public Window(int width, int height, int left, int top) {
        this.width = width;
        this.height = height;
        this.left = left;
        this.top = top;
    }

    public void run() {
        init();
        loop();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public static void lwjglInit() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");
    }

    private int[] getFramebufferSize(long window) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);
            glfwGetFramebufferSize(window, pWidth, pHeight);
            return new int[] {pWidth.get(0), pHeight.get(0)};
        }
    }

    private int[] getWindowSize(long window) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);
            glfwGetWindowSize(window, pWidth, pHeight);
            return new int[] {pWidth.get(0), pHeight.get(0)};
        }
    }

    private int[] getRefreshRates() {
        var monitors = glfwGetMonitors();
        int[] res = new int[monitors.capacity()];
        for (int i=0; i < monitors.capacity(); ++i) {
            res[i] = glfwGetVideoMode(monitors.get(i)).refreshRate();
        }
        return res;
    }

    private void init() {
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        window = glfwCreateWindow(width, height, "Lightweight Java Game Library", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true);
        });

        int[] wSize = getWindowSize(window);
        width = wSize[0];
        height = wSize[1];

        int[] fbSize = getFramebufferSize(window);
        dpi = fbSize[0] / width;
        assert fbSize[1] / height == dpi : "Horizontal dpi=" + dpi + ", vertical dpi=" + fbSize[1] / height;

        glfwSetWindowPos(window, left, top);

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1); // Enable v-sync
        glfwShowWindow(window);
    }

    private Context context;
    private BackendRenderTarget renderTarget;
    private Surface surface;
    private Canvas canvas;

    private void initSkia() {
        System.out.println("Buffer " + width + "x" + height + "@" + dpi);

        Stats.enabled = true;

        canvas = null;
        if (surface != null) { surface.close(); surface = null; }
        if (renderTarget != null) { renderTarget.close(); renderTarget = null; }

        int fbId = GL11.glGetInteger(0x8CA6); // GL_FRAMEBUFFER_BINDING
        renderTarget = BackendRenderTarget.makeGL((int) (width * dpi), (int) (height * dpi), /*samples*/0, /*stencil*/8, fbId, FramebufferFormat.GR_GL_RGBA8);

        surface = Surface.makeFromBackendRenderTarget(context, renderTarget, SurfaceOrigin.BOTTOM_LEFT, SurfaceColorFormat.RGBA_8888, ColorSpace.getDisplayP3()); // TODO load monitor profile

        canvas = surface.getCanvas();
        canvas.scale(dpi, dpi);
        // canvas.translate(0.5f, 0.5f);
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
        canvas.save();
        scenes.get(currentScene).draw(canvas, width, height, dpi, xpos, ypos);
        canvas.restore();
        canvas.save();
        if (stats)
            drawStats();
        canvas.restore();
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

    public void drawStats() {
        long nativeCalls = Stats.nativeCalls;
        Stats.nativeCalls = 0;
        int allocated = Stats.allocated.values().stream().reduce(0, Integer::sum);

        Paint bg = new Paint().setColor(0x90000000);
        Paint fg = new Paint().setColor(0xFFFFFFFF);
        Paint graph = new Paint().setColor(0xFF00FF00).setStrokeWidth(1);
        Paint graphPast = new Paint().setColor(0x9000FF00).setStrokeWidth(1);
        Paint graphLimit = new Paint().setColor(0xFFcc3333).setStrokeWidth(1);
        Font font = interRegular13tnum;
        FontMetrics metrics = font.getMetrics();

        // Background
        canvas.translate(width - 230, height - 185);
        canvas.drawRRect(RRect.makeLTRB(0, 0, 225, 180, 7), bg);
        canvas.translate(5, 5);

        // Scene
        RRect buttonBounds = RRect.makeLTRB(0, 0, 30, 20, 2);
        Rect  labelBounds = Rect.makeLTRB(35, 0, 225, 20);
        canvas.drawRRect(buttonBounds, bg);
        drawStringCentered("←→", buttonBounds, font, metrics,canvas, fg);
        int sceneIdx = 1;
        for (String scene : scenes.keySet()) {
            if (scene.equals(currentScene)) break;
            sceneIdx++;
        }
        drawStringLeft(sceneIdx + "/" + scenes.size() + " " + currentScene, labelBounds, font, metrics, canvas, fg);
        canvas.translate(0, 25);

        // VSync
        buttonBounds = RRect.makeXYWH(5, 0, 20, 20, 2);
        canvas.drawRRect(buttonBounds, bg);
        drawStringCentered("V", buttonBounds, font, metrics,canvas, fg);
        drawStringLeft("VSync: " + (vsync ? "ON" : "OFF"), labelBounds, font, metrics, canvas, fg);
        canvas.translate(0, 25);

        // Stats
        buttonBounds = RRect.makeXYWH(5, 0, 20, 20, 2);
        canvas.drawRRect(buttonBounds, bg);
        drawStringCentered("S", buttonBounds, font, metrics,canvas, fg);
        drawStringLeft("Stats: " + (stats ? "ON" : "OFF"), labelBounds, font, metrics, canvas, fg);
        canvas.translate(0, 25);

        // GC
        canvas.drawRRect(buttonBounds, bg);
        drawStringCentered("G", buttonBounds, font, metrics,canvas, fg);
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
        Library.load();
        context = Context.makeGL();

        GLFW.glfwSetWindowSizeCallback(window, (window, width, height) -> {
            this.width = width;
            this.height = height;

            int[] fbSize = getFramebufferSize(window);
            dpi = fbSize[0] / width;
            assert fbSize[1] / height == dpi : "Horizontal dpi=" + dpi + ", vertical dpi=" + fbSize[1] / height;

            initSkia();
            draw();
        });

        glfwSetCursorPosCallback(window, (window, xpos, ypos) -> {
            this.xpos = (int) xpos;
            this.ypos = (int) ypos;
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
        scenes.put("Empty",            new EmptyScene());
        scenes.put("Figma",            new FigmaScene());
        scenes.put("Font",             new FontScene());
        scenes.put("Geometry",         new GeometryScene());
        scenes.put("Images",           new ImagesScene());
        scenes.put("Image Filters",    new ImageFiltersScene());
        scenes.put("Mask Filters",     new MaskFiltersScene());
        scenes.put("Paragraph",        new ParagraphScene());
        scenes.put("Paragraph Style",  new ParagraphStyleScene());
        scenes.put("Path Effects",     new PathEffectsScene());
        scenes.put("Paths",            new PathsScene());
        scenes.put("Picture Recorder", new PictureRecorderScene());
        scenes.put("Pythagoras",       new PythagorasScene());
        scenes.put("Shaders",          new ShadersScene());
        scenes.put("Shadow Utils",     new ShadowUtilsScene());
        scenes.put("Squares",          new SquaresScene());
        scenes.put("Text",             new TextScene());
        scenes.put("Text Blob",        new TextBlobScene());
        scenes.put("Text Style",       new TextStyleScene());
        // scenes.put("Wall Cached",   new WallOfTextScene(true));
        // scenes.put("Wall of Text",  new WallOfTextScene(false));
        scenes.put("Watches",       new WatchesScene());
        currentScene = "Shadow Utils";
        interRegular = Typeface.makeFromFile("fonts/Inter-Regular.ttf");
        interRegular13tnum = new Font(interRegular, 13); // , new FontFeature("tnum"));
        t0 = System.nanoTime();

        while (!glfwWindowShouldClose(window)) {
            draw();
            glfwPollEvents();
        }
    }
}