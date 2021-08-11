package org.jetbrains.skija.examples.scenes;

import java.util.*;
import java.util.stream.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.shaper.*;

public class RunHandlerScene extends Scene {
    public final Font lato36;
    public final Font inter9;
    public final Paint boundsStroke = new Paint().setColor(0x403333CC).setMode(PaintMode.STROKE).setStrokeWidth(1);
    public final Paint boundsFill = new Paint().setColor(0x403333CC);
    public final Paint textFill = new Paint().setColor(0xFF000000);

    public RunHandlerScene() {
        lato36 = new Font(Typeface.makeFromFile(file("fonts/Lato-Regular.ttf")), 36);
        inter9 = new Font(inter, 9);

        _variants = new String[] {
            "Approximate All",
            "Approximate Spaces",
            "Approximate Punctuation",
            "Approximate None",
        };
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        canvas.translate(20, 20);
        var text = "hello мир, дружба! fi fl 👃 one two ثلاثة 12 👂 34 خمسة";

        try (var shaper = Shaper.makeShapeThenWrap(); // Shaper.makeCoreText();
             var tbHandler = new TextBlobBuilderRunHandler(text, new Point(0, 0));
             var handler = new DebugTextBlobHandler().withRuns();)
        {
            var opts = switch (_variants[_variantIdx]) {
                case "Approximate Spaces" -> ShapingOptions.DEFAULT.withApproximatePunctuation(false);
                case "Approximate Punctuation"  -> ShapingOptions.DEFAULT.withApproximateSpaces(false);
                case "Approximate None" -> ShapingOptions.DEFAULT.withApproximateSpaces(false).withApproximatePunctuation(false);
                default -> ShapingOptions.DEFAULT;
            };

            // TextBlobBuilderRunHandler
            shaper.shape(text, lato36, opts, width - 40, tbHandler);
            try (var blob = tbHandler.makeBlob()) {
                canvas.drawTextBlob(blob, 0, 0, textFill);
                canvas.translate(0, blob.getBounds().getBottom() + 20);
            }

            // DebugTextBlobHandler
            shaper.shape(text, lato36, opts, width - 40, handler);
            
            try (var blob = handler.makeBlob()) {
                canvas.drawTextBlob(blob, 0, 0, textFill);
            }

            List<Rect> taken = new ArrayList<>();
            FontMetrics interMetrics = inter9.getMetrics();
            float maxBottom = 0;

            for (var run: handler._runs) {
                var runBounds = run.getBounds();
                canvas.drawRect(runBounds, boundsStroke);

                var info = run.getInfo();
                try (var builder = new TextBlobBuilder();) {
                    float lh = interMetrics.getHeight();
                    float yPos = -interMetrics.getAscent();
                    float padding = 6;
                    float margin = 6;
                    var font = run.getFont();

                    // build details blob
                    try (var typeface = font.getTypeface();) {
                        builder.appendRun(inter9, typeface.getFamilyName() + " " + font.getSize() + "px", 0, yPos);
                    }
                    builder.appendRun(inter9, "bidi " + info.getBidiLevel(), 0, yPos + lh);
                    builder.appendRun(inter9, "adv (" + info.getAdvanceX() + ", " + info.getAdvanceY() + ")", 0, yPos + lh * 2);
                    builder.appendRun(inter9, "range " + info.getRangeBegin() + ".." + info.getRangeEnd() + " “" + text.substring(info.getRangeBegin(), info.getRangeEnd()) + "”", 0, yPos + lh * 3);
                    builder.appendRun(inter9, info.getGlyphCount() + " glyphs: " + Arrays.toString(run.getGlyphs()), 0, yPos + lh * 4);
                    builder.appendRun(inter9, "x positions " + Arrays.stream(run.getPositions()).map(Point::getX).map(Object::toString).collect(Collectors.joining(", ", "[", "]")), 0, yPos + lh * 5);
                    builder.appendRun(inter9, "y position " + run.getPositions()[0].getY(), 0, yPos + lh * 6);
                    builder.appendRun(inter9, "clusters " + Arrays.toString(run.getClusters()), 0, yPos + lh * 7);

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
                                canvas.drawTextBlob(detailsBlob, detailsInner.getLeft(), detailsInner.getTop(), textFill);
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