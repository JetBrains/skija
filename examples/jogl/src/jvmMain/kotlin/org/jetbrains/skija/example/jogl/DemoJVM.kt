package org.jetbrains.skija.example.jogl

import com.jogamp.opengl.GL.GL_DRAW_FRAMEBUFFER_BINDING
import com.jogamp.opengl.GLAutoDrawable
import com.jogamp.opengl.GLCapabilities
import com.jogamp.opengl.GLEventListener
import com.jogamp.opengl.GLProfile
import com.jogamp.opengl.awt.GLCanvas
import com.jogamp.opengl.util.FPSAnimator
import skija.*
import java.nio.IntBuffer
import javax.swing.JFrame
import javax.swing.WindowConstants
import kotlin.math.sin
import kotlin.math.cos

private val skijaLibrary = System.loadLibrary("skija")

actual class Demo actual constructor() {
    class SkijaState {
        var context: Context? = null
        var renderTarget: BackendRenderTarget? = null
        var surface: Surface? = null
        var canvas: Canvas? = null

        fun clear() {
            if (surface != null) {
                surface!!.release()
            }
            if (renderTarget != null) {
                renderTarget!!.release()
            }
        }
    }

    private fun makeWindow(width: Int, height: Int): JFrame {
        val profile = GLProfile.get(GLProfile.GL3)
        val capabilities = GLCapabilities(profile)
        val glCanvas = GLCanvas(capabilities)
        val skijaState = SkijaState()
        glCanvas.addGLEventListener(object : GLEventListener {
            override fun reshape(drawable: GLAutoDrawable?, x: Int, y: Int, width: Int, height: Int) {
                println("GL: reshape")
                initSkija(glCanvas, skijaState)
            }

            override fun init(drawable: GLAutoDrawable?) {
                println("GL: init")
                initSkija(glCanvas, skijaState)
            }

            override fun dispose(drawable: GLAutoDrawable?) {
                println("GL: dispose")
            }

            private var tick = 0

            override fun display(drawable: GLAutoDrawable?) {
                if (tick++ % 60 == 0) {
                    println("GL: display, divided by 60")
                }
                skijaState.apply {
                    canvas!!.clear(0xFFFFFFFF)
                    canvas!!.save()
                    displayScene(
                        canvas!!,
                        glCanvas.width, glCanvas.height, glCanvas.width.toFloat() / width,
                        0, 0
                    )
                    canvas!!.restore()
                    context!!.flush()
                    //glCanvas.swapBuffers()
                }

            }
        })
        glCanvas.setSize(width, height)

        val animator = FPSAnimator(60)
        animator.add(glCanvas)
        animator.start()

        val frame = JFrame()
        frame.contentPane.add(glCanvas)
        frame.size = frame.contentPane.preferredSize
        return frame
    }

    private fun initSkija(glCanvas: GLCanvas, skijaState: SkijaState) {
        with(skijaState) {
            val width = glCanvas.width
            val height = glCanvas.height
            val dpi = glCanvas.width.toFloat() / width
            println("Buffer " + width.toString() + "x" + height.toString() + "@" + dpi)
            skijaState.clear()
            val intBuf1 = IntBuffer.allocate(1)
            glCanvas.gl.glGetIntegerv(GL_DRAW_FRAMEBUFFER_BINDING, intBuf1)
            val fbId = intBuf1[0]
            println("FB id is $fbId")
            renderTarget = BackendRenderTarget.newGL(
                (width * dpi).toInt(),
                (height * dpi).toInt(),
                0,  /*stencil*/
                8,
                fbId.toLong(),
                BackendRenderTarget.FramebufferFormat.GR_GL_RGBA8.toLong()
            )
            println("Allocated $renderTarget")
            context = Context.makeGL()
            surface = Surface.makeFromBackendRenderTarget(
                context,
                renderTarget,
                Surface.Origin.BOTTOM_LEFT,
                Surface.ColorType.RGBA_8888
            )
            println("Allocated $surface")
            canvas = surface!!.canvas
            canvas!!.scale(dpi, dpi)
        }
    }

    fun displayScene(canvas: Canvas, width: Int, height: Int, dpi: Float, xpos: Int, ypos: Int) {
        val watchFill = Paint().setColor(0xFFFFFFFF)
        val watchStroke = Paint().setColor(0xFF000000).setStyle(Paint.Style.STROKE).setStrokeWidth(1f).setAntiAlias(false)
        val watchStrokeAA = Paint().setColor(0xFF000000).setStyle(Paint.Style.STROKE).setStrokeWidth(1f)
        val watchFillHover = Paint().setColor(0xFFE4FF01)
        for (x in 0 .. (width - 50) step 50) {
            for (y in 0 .. (height - 50) step 50) {
                val hover = xpos > x + 0 && xpos < x + 50 && ypos > y + 0 && ypos < y + 50
                val fill = if (hover) watchFillHover else watchFill
                val stroke = if (x > width / 2) watchStrokeAA else watchStroke
                canvas.drawOval(Rect.makeXYWH(x + 5f, y + 5f, 40f, 40f), fill)
                canvas.drawOval(Rect.makeXYWH(x + 5f, y + 5f, 40f, 40f), stroke)
                var angle = 0f
                while (angle < 2f * Math.PI) {
                    canvas.drawLine(
                            (x + 25 - 17 * sin(angle)),
                            (y + 25 + 17 * cos(angle)),
                            (x + 25 - 20 * sin(angle)),
                            (y + 25 + 20 * cos(angle)),
                            stroke
                        )
                    angle += (2.0 * Math.PI / 12.0).toFloat()
                }
                val time = System.currentTimeMillis() % 60000 +
                        (x.toFloat() / width * 5000).toLong() +
                        (y.toFloat() / width * 5000).toLong();

                val angle1 = (time / 5000 * 2f * Math.PI).toFloat()
                canvas.drawLine(x + 25f, y + 25f,
                    x + 25f - 15f * sin(angle1),
                    y + 25f + 15 * cos(angle1),
                    stroke)

                val angle2 = (time / 60000 * 2f * Math.PI).toFloat()
                canvas.drawLine(x + 25f, y + 25f,
                    x + 25f - 10f * sin(angle2),
                    y + 25f + 10f * cos(angle2),
                    stroke)
            }
        }
    }

    actual fun run(): Int {
        System.loadLibrary("skija")

        val width = 640
        val height = 480
        val frame = makeWindow(width, height)

        frame.title = "Skija Demo"
        frame.setLocation(400, 400)
        frame.setVisible(true)
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE

        return 0
    }
}
