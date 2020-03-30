package skija.examples.lwjgl;

import java.nio.IntBuffer;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import skija.*;

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

    public Window(int width, int height) {
        this.width = width;
        this.height = height;
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        this.left = Math.max(0, (vidmode.width() - width) / 2);
        this.top = Math.max(0, (vidmode.height() - height) / 2);
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

        canvas = null;
        if (surface != null) surface = surface.release();
        if (renderTarget != null) renderTarget = renderTarget.release();

        int fbId = GL11.glGetInteger(0x8CA6); // GL_FRAMEBUFFER_BINDING
        renderTarget = BackendRenderTarget.newGL((int) (width * dpi), (int) (height * dpi), /*samples*/0, /*stencil*/0, fbId, /*GR_GL_RGBA8*/ 0x8058);
        System.out.println("Allocated " + renderTarget);

        surface = Surface.makeFromBackendRenderTarget(context, renderTarget, SurfaceOrigin.BottomLeft, ColorType.RGBA_8888);
        System.out.println("Allocated " + surface);

        canvas = surface.getCanvas();
        canvas.scale(dpi, dpi);
    }

    private void loop() {
        GL.createCapabilities();
        JNI.loadLibrary("/", "skija");
        context = Context.makeGL();
        System.out.println("Allocated " + context);

        var resize = new java.util.concurrent.atomic.AtomicBoolean(true);

        GLFW.glfwSetWindowSizeCallback(window, (window, width, height) -> {
            this.width = width;
            this.height = height;

            int[] fbSize = getFramebufferSize(window);
            dpi = fbSize[0] / width;
            assert fbSize[1] / height == dpi : "Horizontal dpi=" + dpi + ", vertical dpi=" + fbSize[1] / height;
            resize.set(true);
        });
        
        while (!glfwWindowShouldClose(window)) {
            if (resize.compareAndSet​(true, false))
                initSkia();

            canvas.clear(0xFFFFFFFF);

            var p = new Paint();
            p.setColor(0xFF123456);
            canvas.drawRect(10, 10, width - 10, height - 10, p);
            context.flush();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }
}