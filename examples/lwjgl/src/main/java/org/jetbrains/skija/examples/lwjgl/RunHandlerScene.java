package org.jetbrains.skija.examples.lwjgl;

import java.util.*;
import java.util.stream.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.shaper.*;

public class RunHandlerScene implements Scene {
    public final Font lato36;
    public final Font inter9;
    public final Paint boundsStroke = new Paint().setColor(0x403333CC).setMode(PaintMode.STROKE).setStrokeWidth(1);
    public final Paint boundsFill = new Paint().setColor(0x403333CC);
    public final Paint textFill = new Paint().setColor(0xFF000000);

    public RunHandlerScene() {
        lato36 = new Font(Typeface.makeFromFile("fonts/Lato-Regular.ttf"), 36);
        inter9 = new Font(Typeface.makeFromFile("fonts/Inter-Regular.ttf"), 9);
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        canvas.translate(20, 20);

        try (var shaper  = Shaper.makeShapeThenWrap(); // Shaper.makeCoreText();
             var handler = new DebugTextBlobHandler();)
        {
            var text = "hello мир дружба fi fl 👃 one two ثلاثة 12 👂 34 خمسة";
            shaper.shape(text, lato36, FontMgr.getDefault(), null, true, width - 40, handler);
            
            try (var blob = handler._builder.build()) {
                canvas.drawTextBlob(blob, 0, 0, lato36, textFill);
            }

            List<Rect> taken = new ArrayList<>();
            FontMetrics interMetrics = inter9.getMetrics();
            float maxBottom = 0;

            for (var pair: handler._infos) {
                var runBounds = pair.getSecond();
                canvas.drawRect(runBounds, boundsStroke);

                var info = pair.getFirst();
                try (var builder = new TextBlobBuilder(); ) {
                    float lh = interMetrics.getHeight();
                    float yPos = -interMetrics.getAscent();
                    float padding = 6;
                    float margin = 6;

                    // build details blob
                    builder.appendRun(inter9, info.getFont().getTypeface().getFamilyName() + " " + info.getFont().getSize() + "px", 0, yPos);
                    builder.appendRun(inter9, "bidi " + info.getBidiLevel(), 0, yPos + lh);
                    builder.appendRun(inter9, "adv (" + info.getAdvanceX() + ", " + info.getAdvanceY() + ")", 0, yPos + lh * 2);
                    builder.appendRun(inter9, "glyphs " + info.getGlyphCount(), 0, yPos + lh * 3);
                    builder.appendRun(inter9, "range " + info.getUtf8RangeBegin() + ".." + info.getUtf8RangeEnd(), 0, yPos + lh * 4);

                    try (var detailsBlob = builder.build(); ) {

                        // try to fit in
                        var detailsWidth = detailsBlob.getBounds().getWidth();
                        var detailsHeight = detailsBlob.getBounds().getHeight();
                        for (yPos = runBounds.getBottom() + margin; yPos < height; yPos += margin) {
                            Rect detailsOuter = Rect.makeXYWH(runBounds.getLeft(), yPos, detailsWidth + 2 * padding + margin, detailsHeight + 2 * padding + margin);
                            Rect detailsBorder = Rect.makeXYWH(runBounds.getLeft(), yPos, detailsWidth + 2 * padding, detailsHeight + 2 * padding);
                            Rect detailsInner  = Rect.makeXYWH(runBounds.getLeft() + padding, yPos + padding, detailsWidth, detailsHeight);
                            if (taken.stream().allMatch(r -> r.intersect(detailsOuter) == null)) {
                                // draw details
                                canvas.drawRect(detailsBorder, boundsFill);
                                canvas.drawLine(runBounds.getLeft(), runBounds.getBottom(), detailsBorder.getLeft(), detailsBorder.getBottom(), boundsStroke);
                                canvas.drawTextBlob(detailsBlob, detailsInner.getLeft(), detailsInner.getTop(), inter9, textFill);
                                taken.add(detailsOuter);
                                maxBottom = Math.max(maxBottom, detailsOuter.getBottom());
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}