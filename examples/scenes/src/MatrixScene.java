package org.jetbrains.skija.examples.scenes;

import org.jetbrains.skija.*;

public class MatrixScene extends Scene {
    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        canvas.translate(20, 20);

        try (var stroke = new Paint().setColor(0x40000000).setMode(PaintMode.STROKE).setStrokeWidth(1)) {
            canvas.save();
            canvas.drawRect(Rect.makeLTRB(0, 0, 200, 200), stroke);
            canvas.drawLine(0, 100, 200, 100, stroke);
            canvas.drawLine(100, 0, 100, 200, stroke);
            canvas.concat(Matrix33.makeTranslate(100, 100).makeConcat(Matrix33.makeRotate(5)));
            canvas.drawRect(Rect.makeLTRB(-10, -10, 10, 10), blackFill);
            var m44 = canvas.getLocalToDevice().getMat();
            var m33 = canvas.getLocalToDeviceAsMatrix33().getMat();
            canvas.restore();
            canvas.drawString("translate(100, 100) + rotate(5)", 0, 220, inter13, blackFill);
            for (var row = 0; row < 4; ++row)
                canvas.drawString(String.format("[ %.2f %.2f %.2f %.2f ]", m44[row * 4], m44[row * 4 + 1], m44[row * 4 + 2], + m44[row * 4 + 3]), 0, 260 + row * 20, inter13, blackFill);
            for (var row = 0; row < 3; ++row)
                canvas.drawString(String.format("[ %.2f %.2f %.2f ]", m33[row * 3], m33[row * 3 + 1], m33[row * 3 + 2]), 0, 360 + row * 20, inter13, blackFill);
            canvas.translate(300, 0);

            canvas.save();
            canvas.drawRect(Rect.makeLTRB(0, 0, 200, 200), stroke);
            canvas.drawLine(0, 100, 200, 100, stroke);
            canvas.drawLine(100, 0, 100, 200, stroke);
            canvas.concat(Matrix33.makeRotate(5).makeConcat(Matrix33.makeTranslate(100, 100)));
            canvas.drawRect(Rect.makeLTRB(-10, -10, 10, 10), blackFill);
            m44 = canvas.getLocalToDevice().getMat();
            m33 = canvas.getLocalToDeviceAsMatrix33().getMat();
            canvas.restore();
            canvas.drawString("rotate(5) + translate(100, 100)", 0, 220, inter13, blackFill);
            for (var row = 0; row < 4; ++row)
                canvas.drawString(String.format("[ %.2f %.2f %.2f %.2f ]", m44[row * 4], m44[row * 4 + 1], m44[row * 4 + 2], + m44[row * 4 + 3]), 0, 260 + row * 20, inter13, blackFill);
            for (var row = 0; row < 3; ++row)
                canvas.drawString(String.format("[ %.2f %.2f %.2f ]", m33[row * 3], m33[row * 3 + 1], m33[row * 3 + 2]), 0, 360 + row * 20, inter13, blackFill);
            canvas.translate(300, 0);

            // rotate over pivot
            canvas.save();
            canvas.drawRect(Rect.makeLTRB(0, 0, 200, 200), stroke);
            canvas.drawLine(0, 100, 200, 100, stroke);
            canvas.drawLine(100, 0, 100, 200, stroke);
            canvas.concat(Matrix33.makeRotate(5, 100, 100));
            canvas.drawRect(Rect.makeLTRB(-10, -10, 10, 10), blackFill);
            m44 = canvas.getLocalToDevice().getMat();
            m33 = canvas.getLocalToDeviceAsMatrix33().getMat();
            canvas.restore();
            canvas.drawString("translate(100, 100) + rotate(5) + translate(-100, -100)", 0, 220, inter13, blackFill);
            for (var row = 0; row < 4; ++row)
                canvas.drawString(String.format("[ %.2f %.2f %.2f %.2f ]", m44[row * 4], m44[row * 4 + 1], m44[row * 4 + 2], + m44[row * 4 + 3]), 0, 260 + row * 20, inter13, blackFill);
            for (var row = 0; row < 3; ++row)
                canvas.drawString(String.format("[ %.2f %.2f %.2f ]", m33[row * 3], m33[row * 3 + 1], m33[row * 3 + 2]), 0, 360 + row * 20, inter13, blackFill);
            canvas.translate(300, 0);
        }                
    }
}