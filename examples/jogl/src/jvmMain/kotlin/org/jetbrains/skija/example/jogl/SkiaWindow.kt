package org.jetbrains.skija.example.jogl

import com.jogamp.opengl.*
import com.jogamp.opengl.awt.GLCanvas
import com.jogamp.opengl.util.FPSAnimator
import org.jetbrains.skija.BackendRenderTarget
import org.jetbrains.skija.Canvas
import org.jetbrains.skija.Context
import org.jetbrains.skija.JNI
import org.jetbrains.skija.Surface
import java.nio.IntBuffer
import javax.swing.JFrame

private class SkijaState {
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

class SkiaWindow(width: Int, height: Int, fps: Int, renderer: (Canvas, Int, Int) -> Unit): JFrame() {
    companion object {
        init {
            JNI.loadLibrary("/", "skija")
        }
    }

    val glCanvas: GLCanvas

    init {
        val profile = GLProfile.get(GLProfile.GL3)
        val capabilities = GLCapabilities(profile)
        glCanvas = GLCanvas(capabilities)
        val skijaState = SkijaState()
        glCanvas.autoSwapBufferMode = true

        glCanvas.addGLEventListener(object : GLEventListener {
            override fun reshape(drawable: GLAutoDrawable?, x: Int, y: Int, width: Int, height: Int) {
                initSkija(glCanvas, skijaState)
            }

            override fun init(drawable: GLAutoDrawable?) {
                initSkija(glCanvas, skijaState)
            }

            override fun dispose(drawable: GLAutoDrawable?) {
            }

            override fun display(drawable: GLAutoDrawable?) {
                skijaState.apply {
                    canvas!!.clear(0xFFFFFFFF)
                    renderer(
                        canvas!!, glCanvas.width, glCanvas.height
                    )
                    context!!.flush()
                }
            }
        })

        glCanvas.setSize(width, height)

        val animator = FPSAnimator(fps)
        animator.add(glCanvas)
        animator.start()

        contentPane.add(glCanvas)
        size = contentPane.preferredSize
    }

    private fun initSkija(glCanvas: GLCanvas, skijaState: SkijaState) {
        with(skijaState) {
            val width = glCanvas.width
            val height = glCanvas.height
            val dpi = glCanvas.width.toFloat() / width
            skijaState.clear()
            val intBuf1 = IntBuffer.allocate(1)
            glCanvas.gl.glGetIntegerv(GL.GL_DRAW_FRAMEBUFFER_BINDING, intBuf1)
            val fbId = intBuf1[0]
            renderTarget = BackendRenderTarget.newGL(
                (width * dpi).toInt(),
                (height * dpi).toInt(),
                0,
                8,
                fbId.toLong(),
                BackendRenderTarget.FramebufferFormat.GR_GL_RGBA8.toLong()
            )
            context = Context.makeGL()
            surface = Surface.makeFromBackendRenderTarget(
                context,
                renderTarget,
                Surface.Origin.BOTTOM_LEFT,
                Surface.ColorType.RGBA_8888
            )
            canvas = surface!!.canvas
            canvas!!.scale(dpi, dpi)
        }
    }
}