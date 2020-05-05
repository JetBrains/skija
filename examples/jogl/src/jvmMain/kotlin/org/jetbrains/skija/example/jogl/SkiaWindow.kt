package org.jetbrains.skija.example.jogl

import com.jogamp.opengl.*
import com.jogamp.opengl.awt.GLCanvas
import com.jogamp.opengl.util.FPSAnimator
import org.jetbrains.skija.BackendRenderTarget
import org.jetbrains.skija.Canvas
import org.jetbrains.skija.Context
import org.jetbrains.skija.ColorSpace
import org.jetbrains.skija.JNI
import org.jetbrains.skija.Surface

import java.nio.IntBuffer
import javax.swing.JFrame

private class SkijaState {
    var context: Context? = null
    var renderTarget: BackendRenderTarget? = null
    var surface: Surface? = null
    var canvas: Canvas? = null
    var textureId: Int = 0
    val intBuf1 = IntBuffer.allocate(1)

    fun clear() {
        surface?.close()
        renderTarget?.close()
    }
}

interface SkiaRenderer {
    fun onInit()
    fun onRender(canvas: Canvas, width: Int, height: Int)
    fun onReshape(width: Int, height: Int)
    fun onDispose()
}

class SkiaWindow(
    width: Int,
    height: Int,
    fps: Int = 0
) : JFrame() {
    companion object {
        init {
            JNI.loadLibrary("/", "skija")
        }
    }

    val glCanvas: GLCanvas
    var animator: FPSAnimator? = null
    var renderer: SkiaRenderer? = null
    private val skijaState = SkijaState()

    init {
        val profile = GLProfile.get(GLProfile.GL3)
        val capabilities = GLCapabilities(profile)
        capabilities.doubleBuffered = true
        glCanvas = GLCanvas(capabilities)
        glCanvas.autoSwapBufferMode = true

        glCanvas.addGLEventListener(object : GLEventListener {
            override fun reshape(
                drawable: GLAutoDrawable?,
                x: Int,
                y: Int,
                width: Int,
                height: Int
            ) {
                initSkija(glCanvas)
                renderer!!.onReshape(width, height)
            }

            override fun init(drawable: GLAutoDrawable?) {
                skijaState.context = Context.makeGL()
                initSkija(glCanvas)
                renderer!!.onInit()
            }

            override fun dispose(drawable: GLAutoDrawable?) {
                renderer!!.onDispose()
                skijaState.clear()
                skijaState.context?.close()
            }

            override fun display(drawable: GLAutoDrawable?) {
                skijaState.apply {
                    val gl = drawable!!.gl!!
                    gl.glBindTexture(GL.GL_TEXTURE_2D, textureId)
                    canvas!!.clear(0xFFFFFFFFL.toInt())
                    renderer!!.onRender(
                        canvas!!, glCanvas.width, glCanvas.height
                    )
                    skijaState.context!!.flush()
                    glCanvas.gl.glGetIntegerv(GL.GL_TEXTURE_BINDING_2D, intBuf1)
                    textureId = intBuf1[0]
                }
            }
        })

        glCanvas.setSize(width, height)

        setFps(fps)

        contentPane.add(glCanvas)
        size = contentPane.preferredSize
    }

    fun setFps(fps: Int) {
        animator?.stop()
        animator = if (fps > 0) {
            FPSAnimator(fps).also {
                it.add(glCanvas)
                it.start()
            }
        } else {
            null
        }
    }

    private fun initSkija(glCanvas: GLCanvas) {
        val width = glCanvas.width
        val height = glCanvas.height
        val dpi = 1f
        skijaState.clear()
        val intBuf1 = IntBuffer.allocate(1)
        glCanvas.gl.glGetIntegerv(GL.GL_DRAW_FRAMEBUFFER_BINDING, intBuf1)
        val fbId = intBuf1[0]
        skijaState.renderTarget = BackendRenderTarget.newGL(
            (width * dpi).toInt(),
            (height * dpi).toInt(),
            0,
            8,
            fbId.toLong(),
            BackendRenderTarget.FramebufferFormat.GR_GL_RGBA8.toLong()
        )
        skijaState.surface = Surface.makeFromBackendRenderTarget(
            skijaState.context,
            skijaState.renderTarget,
            Surface.Origin.BOTTOM_LEFT,
            Surface.ColorType.RGBA_8888,
            ColorSpace.SRGB
        )
        skijaState.canvas = skijaState.surface!!.canvas
        skijaState.canvas!!.scale(dpi, dpi)
        glCanvas.gl.glGetIntegerv(GL.GL_TEXTURE_BINDING_2D, intBuf1)
        skijaState.textureId = intBuf1[0]
    }
}
