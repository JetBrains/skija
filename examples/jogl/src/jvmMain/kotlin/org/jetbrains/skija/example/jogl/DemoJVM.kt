package org.jetbrains.skija.example.jogl

import skija.Canvas
import skija.Paint
import skija.Rect
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionAdapter
import javax.swing.WindowConstants
import kotlin.math.cos
import kotlin.math.sin

fun displayScene(canvas: Canvas, width: Int, height: Int, xpos: Int, ypos: Int) {
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
                    (y.toFloat() / width * 5000).toLong()

            val angle1 = (time.toFloat() / 5000 * 2f * Math.PI).toFloat()
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

actual class Demo actual constructor() {
    actual fun run(): Int {
        val width = 1024
        val height = 768

        var mouseX = 0
        var mouseY = 0

        val frame = SkiaWindow(width = width, height = height, fps = 120) {
            canvas, w, h -> displayScene(canvas, w, h, mouseX, mouseY)
        }
        frame.glCanvas.addMouseMotionListener(object : MouseMotionAdapter() {
            override fun mouseMoved(event: MouseEvent) {
                mouseX = event.x
                mouseY = event.y
            }
        })
        frame.title = "Skija Demo"
        frame.setLocation(400, 400)
        frame.setVisible(true)
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE

        return 0
    }
}
