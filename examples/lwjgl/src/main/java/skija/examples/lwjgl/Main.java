package skija.examples.lwjgl;

import java.nio.IntBuffer;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import skija.*;

public class Main {
    public static void main(String [] args) throws Exception {
        var p = new Paint();
        System.out.println("p.color = " + Long.toString(p.getColor(), 16));
        p.setColor(0xFF123456);
        System.out.println("p.color = " + Long.toString(p.getColor(), 16));

        new Canvas().drawRect(0, 0, 10, 10, new Paint());

        System.gc();
        Thread.sleep(1000);

        Window.lwjglInit();
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        new Window((int) (vidmode.width() * 0.75), (int) (vidmode.height() * 0.75)).run();
    }
}

class Window {
    public long window;
    public int width;
    public int height;
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

        glfwSetWindowPos(window, left, top);

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1); // Enable v-sync
        glfwShowWindow(window);
    }
        
    private void loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
        
        var c = Context.makeGL();
        System.out.println("Context mNativeInstance:0x" + Long.toString(c.getNativeInstance(), 16));
        c = null;
        System.gc();
        System.out.println("Context over");

        GLFW.glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
            System.out.println("FramebufferSize " + width + "x" + height);
        });
        
        GLFW.glfwSetWindowSizeCallback(window, (window, width, height) -> {
            System.out.println("WindowSize " + width + "x" + height);
        });
        
        while (!glfwWindowShouldClose(window)) {    
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }
}