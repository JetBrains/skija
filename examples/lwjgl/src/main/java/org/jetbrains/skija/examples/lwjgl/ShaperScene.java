package org.jetbrains.skija.examples.lwjgl;

import java.util.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.shaper.*;

public class ShaperScene implements Scene {
    public final Font firaCode11;
    public final Paint stroke = new Paint().setColor(0x203333CC).setMode(PaintMode.STROKE).setStrokeWidth(1);
    public final Paint fill = new Paint().setColor(0xFF000000);

    public ShaperScene() {
        firaCode11 = new Font(Typeface.makeFromFile("fonts/FiraCode-Regular.ttf"), 11);
    }

    public void drawWithShaper(Canvas canvas, float maxWidth, int color, String name, Shaper shaper) {
        fill.setColor(color);
        String text = "Incomprehensibilities ararar123456 +++ *** <-> " + name;

        try (var blob = shaper.shape(text, firaCode11, null, true, maxWidth, new Point(0, 0)); ) {
            canvas.drawTextBlob(blob, 0, 0, firaCode11, fill);
            canvas.translate(0, blob.getBounds().getHeight());
        }

        try (var blob = shaper.shape(text + " RTL", firaCode11, null, false, maxWidth, new Point(0, 0)); ) {
            canvas.drawTextBlob(blob, 0, 0, firaCode11, fill);
            canvas.translate(0, blob.getBounds().getHeight());
        }

        String features = "ss01 cv01 onum -calt";
        try (var blob = shaper.shape(text + " " + features, firaCode11, FontFeature.parse(features), true, maxWidth, new Point(0, 0)); ) {
            canvas.drawTextBlob(blob, 0, 0, firaCode11, fill);
            canvas.translate(0, blob.getBounds().getHeight());
        }

        features = "ss01[22:25] cv01[22:25] onum[28:31] calt[43:44]=0";
        try (var blob = shaper.shape(text + " " + features, firaCode11, FontFeature.parse(features), true, maxWidth, new Point(0, 0)); ) {
            canvas.drawTextBlob(blob, 0, 0, firaCode11, fill);
            canvas.translate(0, blob.getBounds().getHeight());
        }

        float height = 0;

        try (var blob = shaper.shape(text, firaCode11, null, true, 75, new Point(0, 0)); ) {
            canvas.drawTextBlob(blob, 0, 0, firaCode11, fill);
            Rect bounds = blob.getBounds();
            canvas.drawRect(Rect.makeXYWH(0, 0, 75, bounds.getHeight()), stroke);
            height = bounds.getHeight();
            canvas.translate(maxWidth / 2, 0);
        }

        try (var handler = new TextBlobHandler();) {
            shaper.shape(text + " RunHandler", firaCode11, null, true, 75, handler);
            // handler._builder.appendRun(firaCode11, "--- // ---", 0, 11);
            try (var blob = handler.build();) {
                canvas.drawTextBlob(blob, 0, 0, firaCode11, fill);
                Rect bounds = blob.getBounds();
                canvas.drawRect(Rect.makeXYWH(0, 0, 75, bounds.getHeight()), stroke);

                height = Math.max(blob.getBounds().getHeight(), height);
                canvas.translate(- maxWidth / 2, height);
            }
        }

        canvas.translate(0, 20);
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        float columnWidth = (width - 60) / 2;
        var fontMgr = FontMgr.getDefault();

        canvas.save();
        canvas.translate(20, 20);

        try (var shaper = Shaper.makePrimitive()) {
            drawWithShaper(canvas, columnWidth, 0xFFf94144, "Primitive", shaper);
        }

        try (var shaper = Shaper.make()) {
            drawWithShaper(canvas, columnWidth, 0xFF577590, "make", shaper);
        }

        if (System.getProperty("os.name").equals("Mac OS X"))
            try (var shaper = Shaper.makeCoreText()) {
                drawWithShaper(canvas, columnWidth, 0xFFf3722c, "CoreText", shaper);
            }

        canvas.restore();


        canvas.save();
        canvas.translate(20 + columnWidth + 20, 20);

        try (var shaper = Shaper.makeShaperDrivenWrapper()) {
            drawWithShaper(canvas, columnWidth, 0xFF43aa8b, "ShaperDrivenWrapper", shaper);
        }

        try (var shaper = Shaper.makeShapeThenWrap()) {
            drawWithShaper(canvas, columnWidth, 0xFFf8961e, "ShapeThenWrap", shaper);
        }

        try (var shaper = Shaper.makeShapeDontWrapOrReorder()) {
            drawWithShaper(canvas, columnWidth, 0xFF90be6d, "ShapeDontWrapOrReorder", shaper);
        }

        canvas.restore();
    }
}

class TextBlobHandler implements RunHandler, AutoCloseable {
    public final TextBlobBuilder _builder;
    float maxRunAscent = 0;
    float maxRunDescent = 0;
    float maxRunLeading = 0;
    float xPos = 0;
    float yPos = 0;

    public TextBlobHandler() {
        _builder = new TextBlobBuilder();
    }

    @Override
    public void close() {
        _builder.close();
    }

    public TextBlob build() {
        return _builder.build();
    }

    @Override
    public void beginLine() {
        xPos = 0;
        maxRunAscent = 0;
        maxRunDescent = 0;
        maxRunLeading = 0;
    }

    @Override
    public void runInfo(RunInfo info) {
        var metrics   = info.getFont().getMetrics();
        maxRunAscent  = Math.min(maxRunAscent,  metrics.getAscent());
        maxRunDescent = Math.max(maxRunDescent, metrics.getDescent());
        maxRunLeading = Math.max(maxRunLeading, metrics.getLeading());
    }

    @Override
    public void commitRunInfo() {
        yPos += -maxRunAscent;
    }

    @Override
    public Point runOffset(RunInfo info) {
        return new Point(xPos, yPos);
    }

    @Override
    public void commitRun(RunInfo info, short[] glyphs, Point[] positions, int[] clusters) {
        _builder.appendRunPos(info.getFont(), glyphs, positions);
        xPos += info.getAdvance().getX();
    }

    @Override
    public void commitLine() {
        yPos += maxRunDescent + maxRunLeading;
    }
}