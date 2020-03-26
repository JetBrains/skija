package skija.examples.lwjgl;

import skija.*;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main {
    public static void main(String [] args) throws Exception {
        var p = new Paint();
        System.out.println("p.color = " + Long.toString(p.getColor(), 16));
        p.setColor(0xFF123456);
        System.out.println("p.color = " + Long.toString(p.getColor(), 16));

        new Canvas().drawRect(0, 0, 10, 10, new Paint());
        System.gc();
        // Thread.sleep(1000);
    }
}