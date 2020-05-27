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
    private Typeface interRegular;
    private Font interRegular13tnum;
    private int[] refreshRates;

    public Window(int width, int height) {
        this.width = width;
        this.height = height;
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        this.left = Math.max(0, (vidmode.width() - width) / 2);
        this.top = Math.max(0, (vidmode.height() - height) / 2);
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

        Managed.stats = true;

        canvas = null;
        if (surface != null) { surface.close(); surface = null; }
        if (renderTarget != null) { renderTarget.close(); renderTarget = null; }

        int fbId = GL11.glGetInteger(0x8CA6); // GL_FRAMEBUFFER_BINDING
        renderTarget = BackendRenderTarget.newGL((int) (width * dpi), (int) (height * dpi), /*samples*/0, /*stencil*/8, fbId, BackendRenderTarget.FramebufferFormat.GR_GL_RGBA8);
        System.out.println("Allocated " + renderTarget);

        surface = Surface.makeFromBackendRenderTarget(context, renderTarget, Surface.Origin.BOTTOM_LEFT, Surface.ColorType.RGBA_8888, ColorSpace.getDisplayP3());
        System.out.println("Allocated " + surface);

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
        drawStats();
        canvas.restore();
        timesIdx = (timesIdx + 1) % times.length;
        context.flush();
        glfwSwapBuffers(window);
    }

    public void drawStats() {
        long nativeCalls = Native.nativeCalls;
        Native.nativeCalls = 0;
        int allocated = Managed.allocated.values().stream().reduce(0, Integer::sum);

        Paint bg = new Paint().setColor(0x90000000);
        Paint fg = new Paint().setColor(0xFFFFFFFF);
        Paint graph = new Paint().setColor(0xFF00FF00).setStrokeWidth(1);
        Paint graphPast = new Paint().setColor(0x9000FF00).setStrokeWidth(1);
        Paint graphLimit = new Paint().setColor(0xFFcc3333).setStrokeWidth(1);
        Font font = interRegular13tnum;
        FontExtents extents = font.hbFont.getHorizontalExtents();
        float baseline = (20 - (extents.descender + extents.getAscenderAbs())) / 2 + extents.getAscenderAbs();

        // Background
        canvas.translate(width - 230, height - 160);
        canvas.drawRoundedRect(RoundedRect.makeLTRB(0, 0, 225, 155, 7), bg);
        canvas.translate(5, 5);

        // Scene
        canvas.drawRoundedRect(RoundedRect.makeLTRB(0, 0, 30, 20, 2), bg);
        TextBuffer buffer = font.hbFont.shape("←→");
        canvas.drawTextBuffer(buffer, (30 - buffer.getAdvances()[0]) / 2, baseline, font.skFont, fg);
        int sceneIdx = 1;
        for (String scene : scenes.keySet()) {
            if (scene.equals(currentScene)) break;
            sceneIdx++;
        }
        buffer.close();
        buffer = font.hbFont.shape("Scene: " + currentScene + " " + sceneIdx + "/" + scenes.size());
        canvas.drawTextBuffer(buffer, 35, baseline, font.skFont, fg);
        buffer.close();
        canvas.translate(0, 25);

        // VSync
        canvas.drawRoundedRect(RoundedRect.makeXYWH(5, 0, 20, 20, 2), bg);
        buffer = font.hbFont.shape("V");
        canvas.drawTextBuffer(buffer, 5 + (20 - buffer.getAdvances()[0]) / 2, baseline, font.skFont, fg);
        buffer.close();
        buffer = font.hbFont.shape("VSync: " + (vsync ? "ON" : "OFF"));
        canvas.drawTextBuffer(buffer, 35, baseline, font.skFont, fg);
        buffer.close();
        canvas.translate(0, 25);

        // GC
        canvas.drawRoundedRect(RoundedRect.makeXYWH(5, 0, 20, 20, 2), bg);
        buffer = font.hbFont.shape("G");
        canvas.drawTextBuffer(buffer, 5 + (20 - buffer.getAdvances()[0]) / 2, baseline, font.skFont, fg);
        buffer.close();

        buffer = font.hbFont.shape("GC objects: " + allocated);
        canvas.drawTextBuffer(buffer, 35, baseline, font.skFont, fg);
        buffer.close();
        canvas.translate(0, 25);

        // Native calls
        try (var b = font.hbFont.shape("Native calls: " + nativeCalls)) {
            canvas.drawTextBuffer(b, 35, baseline, font.skFont, fg);
            canvas.translate(0, 25);
        }

        // fps
        canvas.drawRoundedRect(RoundedRect.makeLTRB(0, 0, times.length, 45, 2), bg);
        for (int i = 0; i < times.length; ++i) {
            canvas.drawLine(i, 45, i, 45 - (float) times[i], i > timesIdx ? graphPast : graph);
        }

        for (int refreshRate : refreshRates) {
            float frameTime = 1000f / refreshRate;
            canvas.drawLine(0, 45 - frameTime, times.length, 45 - frameTime, graphLimit);
        }

        buffer = font.hbFont.shape(String.format("%.1fms", Arrays.stream(times).takeWhile(t->t>0).average().getAsDouble()));
        canvas.drawTextBuffer(buffer, times.length + 5, baseline, font.skFont, fg);
        buffer.close();

        buffer = font.hbFont.shape(String.format("%.0f fps", 1000.0 / Arrays.stream(times).takeWhile(t->t>0).average().getAsDouble()));
        canvas.drawTextBuffer(buffer, times.length + 5, baseline + 25, font.skFont, fg);
        buffer.close();
        canvas.translate(0, 25);

        bg.close();
        fg.close();
        graph.close();
        graphPast.close();
        graphLimit.close();
    }

    private void loop() {
        GL.createCapabilities();
        JNI.loadLibrary("/", "skija");
        context = Context.makeGL();
        System.out.println("Allocated " + context);

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
            System.out.println("Button " + button + " " + (action == 0 ? "released" : "pressed"));
        });

        glfwSetScrollCallback(window, (window, xoffset, yoffset) -> { System.out.println("Scroll by " + xoffset + "x" + yoffset); });

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
                    case GLFW_KEY_G:
                        System.out.println("Before GC " + Managed.allocated);
                        System.gc();
                        break;
                }
            }
        });

        initSkia();

        scenes = new TreeMap<>();
        scenes.put("Blends",        new BlendsScene());
        scenes.put("Color Filters", new ColorFiltersScene());
        scenes.put("Empty",         new EmptyScene());
        scenes.put("Geometry",      new GeometryScene());
        scenes.put("Images",        new ImagesScene());
        scenes.put("Image Filters", new ImageFiltersScene());
        scenes.put("Mask Filters",  new MaskFiltersScene());
        scenes.put("Paragraph",     new ParagraphScene());
        scenes.put("Path Effects",  new PathEffectsScene());
        scenes.put("Paths",         new PathsScene());
        scenes.put("Pythagoras",    new PythagorasScene());
        scenes.put("Shaders",       new ShadersScene());
        scenes.put("Squares",       new SquaresScene());
        scenes.put("Text",          new TextScene());
        scenes.put("Text Blob",     new TextBlobScene());
        scenes.put("Wall Cached",   new WallOfTextScene(true));
        scenes.put("Wall of Text",  new WallOfTextScene(false));
        scenes.put("Watches",       new WatchesScene());
        currentScene = "Paragraph";
        interRegular = Typeface.makeFromFile("fonts/Inter-Regular.ttf");
        interRegular13tnum = new Font(interRegular, 13, new FontFeature("tnum"));
        t0 = System.nanoTime();

        while (!glfwWindowShouldClose(window)) {
            draw();
            glfwPollEvents();
        }
    }
}