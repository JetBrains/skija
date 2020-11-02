package org.jetbrains.skija.examples.lwjgl;

import java.util.*;
import java.util.stream.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.shaper.*;

public class ShapingScene implements Scene {
    public final Font lato36;
    public final Font inter11;
    public final Paint boundsStroke = new Paint().setColor(0x403333CC).setMode(PaintMode.STROKE).setStrokeWidth(1);
    public final Paint boundsFill = new Paint().setColor(0x403333CC);
    public final Paint textFill = new Paint().setColor(0xFF000000);

    public ShapingScene() {
        lato36 = new Font(Typeface.makeFromFile("fonts/Lato-Regular.ttf"), 36);
        inter11 = new Font(Typeface.makeFromFile("fonts/Inter-Regular.ttf"), 11);
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        canvas.translate(20, 20);

        var shapers = new Shaper[] { Shaper.makeShapeThenWrap(),
                                     Shaper.makeCoreText() };
        for (var shaper: shapers) {
            // System.out.println(shaper == shapers[0] ? "\nMakeShapeThenWrap" : "\nMakeCoreText");
            try (var handler = new TextBlobHandler();
                 var blob = shape(shaper, "hello мир дружба fi fl 👃 one two ثلاثة 12 👂 34 خمسة", lato36, FontMgr.getDefault(), null, true, width / 2 - 30, handler);)
            {
                canvas.drawTextBlob(blob, 0, 0, lato36, textFill);

                List<Rect> taken = new ArrayList<>();
                FontMetrics interMetrics = inter11.getMetrics();
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
                        builder.appendRun(inter11, "font " + info.getFont().getTypeface().getFamilyName() + " " + info.getFont().getSize() + "px", 0, yPos);
                        builder.appendRun(inter11, "bidi " + info.getBiDiLevel(), 0, yPos + lh);
                        builder.appendRun(inter11, "adv (" + info.getAdvanceX() + ", " + info.getAdvanceY() + ")", 0, yPos + lh * 2);
                        builder.appendRun(inter11, "glyphs " + info.getGlyphCount(), 0, yPos + lh * 3);
                        builder.appendRun(inter11, "range " + info.getUtf8RangeBegin() + ".." + info.getUtf8RangeEnd(), 0, yPos + lh * 4);

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
                                    canvas.drawTextBlob(detailsBlob, detailsInner.getLeft(), detailsInner.getTop(), inter11, textFill);
                                    taken.add(detailsOuter);
                                    maxBottom = Math.max(maxBottom, detailsOuter.getBottom());
                                    break;
                                }
                            }
                        }
                    }
                }

                // canvas.translate(0, maxBottom);
                canvas.translate(width / 2 - 10, 0);
            }
        }
    }

    public TextBlob shape(Shaper shaper, String text, Font font, FontMgr fontMgr, FontFeature[] features, boolean leftToRight, float width, TextBlobHandler handler) {
        shaper.shape(text, font, fontMgr, features, leftToRight, width, handler);
        return handler._builder.build();
    }
}

class TextBlobHandler implements RunHandler, AutoCloseable {
    public final TextBlobBuilder _builder;
    public float _maxRunAscent = 0;
    public float _maxRunDescent = 0;
    public float _maxRunLeading = 0;
    public float _xPos = 0;
    public float _yPos = 0;
    public List<Pair<RunInfo, Rect>> _infos = new ArrayList<>();

    public TextBlobHandler() {
        _builder = new TextBlobBuilder();
    }

    @Override
    public void close() {
        _builder.close();
    }

    @Override
    public void beginLine() {
        _xPos = 0;
        _maxRunAscent = 0;
        _maxRunDescent = 0;
        _maxRunLeading = 0;
    }

    @Override
    public void runInfo(RunInfo info) {
        var metrics   = info.getFont().getMetrics();
        _maxRunAscent  = Math.min(_maxRunAscent,  metrics.getAscent());
        _maxRunDescent = Math.max(_maxRunDescent, metrics.getDescent());
        _maxRunLeading = Math.max(_maxRunLeading, metrics.getLeading());
    }

    @Override
    public void commitRunInfo() {
        _yPos += -_maxRunAscent;
    }

    @Override
    public Point runOffset(RunInfo info) {
        return new Point(_xPos, _yPos);
    }

    @Override
    public void commitRun(RunInfo info, short[] glyphs, Point[] positions, int[] clusters) {
        // System.out.println("advance=" + info._advanceX
        //                    + " glyphCount=" + info._glyphCount
        //                    + " utf8Range=" + info._utf8RangeBegin + ".." + info.getUtf8RangeEnd() 
        //                    + " positions=" + Arrays.stream(positions).map(Point::getX).collect(Collectors.toList()));
        _builder.appendRunPos(info.getFont(), glyphs, positions);
        _infos.add(new Pair(info, Rect.makeXYWH(_xPos, _yPos - (-_maxRunAscent), info.getAdvance().getX(), (-_maxRunAscent) + _maxRunDescent)));
        _xPos += info.getAdvance().getX();
    }

    @Override
    public void commitLine() {
        _yPos += _maxRunDescent + _maxRunLeading;
    }
}