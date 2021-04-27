package org.jetbrains.skija.examples.scenes;

import org.jetbrains.skija.*;
import java.util.*;
import java.util.concurrent.atomic.*;

public class PictureRecorderScene extends Scene {
    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        canvas.translate(30, 30);
        int id1 = -1;

        try (PictureRecorder recorder = new PictureRecorder();
             Paint paint1 = new Paint().setColor(0xFF264653); 
             Paint paint2 = new Paint().setColor(0xFF2a9d8f); 
             Paint paint3 = new Paint().setColor(0xFFe9c46a); 
             Paint paint4 = new Paint().setColor(0xFFf4a261);
             Paint paint5 = new Paint().setColor(0xFFe76f51);
             Font font = new Font() )
        {
            for (Rect bounds: new Rect[] { Rect.makeXYWH(0, 0, 40, 40),
                                           Rect.makeXYWH(0, 0, 20, 20) })
            {
                Canvas rc = recorder.beginRecording(bounds);

                rc.drawRect(Rect.makeXYWH( 0,  0, 10, 10), paint1);
                rc.drawRect(Rect.makeXYWH(20,  0, 10, 10), paint2);
                rc.drawRect(Rect.makeXYWH( 5,  5, 20, 20), paint3);
                rc.translate(0, 20);
                rc.drawRect(Rect.makeXYWH( 0, 0, 10, 10), paint4);
                rc.drawRect(Rect.makeXYWH(20, 0, 10, 10), paint5);

                assert Objects.equals(rc, recorder.getRecordingCanvas());
                
                try (Picture picture = recorder.finishRecordingAsPicture();
                     Paint transparent = new Paint().setColor(0x80000000);)
                {
                    assert Objects.equals(bounds, picture.getCullRect());
                    id1 = picture.getUniqueId();

                    canvas.save();
                    canvas.drawPicture(picture);
                    canvas.drawPicture(picture, Matrix33.makeTranslate(40, 0), null);
                    canvas.translate(80, 0);
                    canvas.drawPicture(picture, null, transparent);
                    canvas.translate(40, 0);
                    try (
                            AlmostTransparentFilterCanvas transparentCanvas =
                                 new AlmostTransparentFilterCanvas(canvas)
                    ) {
                        transparentCanvas.drawPicture(picture, null, null);
                    }
                    canvas.translate(40, 0);
                    picture.playback(canvas);
                    canvas.translate(40, 0);

                    AtomicInteger counter = new AtomicInteger(0);
                    picture.playback(canvas, () -> counter.addAndGet(1) > 3);
                    canvas.restore();
                }

                canvas.translate(0, 40);
            }

            Canvas rc = recorder.beginRecording(Rect.makeXYWH(0, 0, width, height));

            rc.drawRect(Rect.makeXYWH( 0,  0, 10, 10), paint1);
            rc.drawRect(Rect.makeXYWH(20,  0, 10, 10), paint2);
            rc.drawRect(Rect.makeXYWH( 5,  5, 20, 20), paint3);
            rc.translate(0, 20);
            rc.drawRect(Rect.makeXYWH( 0, 0, 10, 10), paint4);
            rc.drawRect(Rect.makeXYWH(20, 0, 10, 10), paint5);
            
            try (Picture picture   = recorder.finishRecordingAsPicture(Rect.makeXYWH(0, 0, 20, 20));
                 Paint transparent = new Paint().setColor(0x80000000);
                 Paint black       = new Paint().setColor(0xFF000000);)

            {
                assert id1 != picture.getUniqueId();
                canvas.save();
                canvas.drawPicture(picture);
                canvas.drawPicture(picture, Matrix33.makeTranslate(40, 0), null);
                canvas.translate(80, 0);
                canvas.drawPicture(picture, null, transparent);
                canvas.translate(40, 0);
                picture.playback(canvas);
                canvas.translate(40, 0);

                AtomicInteger counter = new AtomicInteger(0);
                picture.playback(canvas, () -> counter.addAndGet(1) > 3);
                canvas.translate(40, 0);

                try (Data data = picture.serializeToData();
                     Picture picture2 = Picture.makeFromData(data); )
                {
                    picture2.playback(canvas);
                    canvas.translate(40, 0);
                }

                try (Picture placeholder = Picture.makePlaceholder(Rect.makeXYWH(0, 0, 40, 40)); ) {
                    canvas.drawPicture(placeholder, null, black);
                    canvas.translate(40, 0);
                }

                try (Shader s = picture.makeShader(FilterTileMode.CLAMP, FilterTileMode.REPEAT, FilterMode.LINEAR)) {
                    black.setShader(s);
                    canvas.drawRect(Rect.makeXYWH(0, 0, 30, 30), black);
                    canvas.translate(40, 0);
                }

                try (Shader s = picture.makeShader(FilterTileMode.CLAMP, FilterTileMode.REPEAT, FilterMode.LINEAR, Matrix33.makeTranslate(5, 5))) {
                    black.setShader(s);
                    canvas.drawRect(Rect.makeXYWH(0, 0, 30, 30), black);
                    canvas.translate(40, 0);
                }

                try (Shader s = picture.makeShader(FilterTileMode.CLAMP, FilterTileMode.REPEAT, FilterMode.LINEAR, null, Rect.makeXYWH(5, 5, 20, 20))) {
                    black.setShader(s);
                    canvas.drawRect(Rect.makeXYWH(0, 0, 30, 30), black);
                    canvas.translate(40, 0);
                }

                black.setShader(null);
                canvas.drawString("approximateOpCount = " + picture.getApproximateOpCount(), 0, 10, font, black);
                canvas.drawString("approximateBytesUsed = " + picture.getApproximateBytesUsed(), 0, 25, font, black);

                canvas.restore();
            }
            canvas.translate(0, 40);
        }
    }
}

class AlmostTransparentFilterCanvas extends PaintFilterCanvas {
    public AlmostTransparentFilterCanvas(Canvas canvas) {
        super(canvas, true);
    }

    @Override
    public boolean onFilter(Paint paint) {
        paint.setColor(Color.withA(paint.getColor(), 32));
        return true;
    }
}