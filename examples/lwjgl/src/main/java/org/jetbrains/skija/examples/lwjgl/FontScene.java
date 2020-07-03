package org.jetbrains.skija.examples.lwjgl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.jetbrains.skija.*;

public class FontScene implements Scene {
    public Typeface _inter;
    public Typeface _jbMono;
    public Paint    _paint;
    public Paint    _stroke;
    public Paint    _boundaryPaint;
    public Font     _defaultFont;
    public Font     _inter13;
    public Font     _inter18;
    public Font     _inter13_1_0;
    public Font     _inter13_2_0;
    public Font     _inter13_1_1;
    public Font     _inter13_1_neg1;
    public Font     _inter48;
    public Font     _jbMono13;

    public FontScene() {
        _inter          = Typeface.makeFromFile("fonts/Inter-Regular.ttf");
        _jbMono         = Typeface.makeFromFile("fonts/JetBrainsMono-Regular.ttf");
        _paint          = new Paint().setColor(0xFF1d3557);
        _stroke         = new Paint().setColor(0xFF2a9d8f).setMode(PaintMode.STROKE).setStrokeWidth(2).setPathEffect(PathEffect.makeDash(new float[] {6, 2}, 0));
        _boundaryPaint  = new Paint().setColor(0xFFe76f51).setMode(PaintMode.STROKE);
        _defaultFont    = new Font();
        _inter13        = new Font(_inter, 13);
        _inter18        = new Font(_inter, 18);
        _inter13_1_0    = new Font(_inter, 13, 1, 0);
        _inter13_2_0    = new Font(_inter, 13, 2, 0);
        _inter13_1_1    = new Font(_inter, 13, 1, 1);
        _inter13_1_neg1 = new Font(_inter, 13, 1, -1);
        _inter48        = new Font(_inter, 48);
        _jbMono13       = new Font(_jbMono, 13);
    }

    public float _drawLine(Canvas canvas, String text, Font font) {
        var blob = font.shape(text, Float.POSITIVE_INFINITY);
        var bounds = blob.getBounds();
        canvas.drawTextBlob(blob, 0, 0, font, _paint);
        canvas.translate(0, bounds.getHeight());
        return bounds.getHeight();
    }

    @Override
    public void draw(Canvas canvas, int windowWidth, int windowHeight, float dpi, int xpos, int ypos) {
        canvas.translate(30, 30);
        _drawLine(canvas, "Default", _defaultFont);
        _drawLine(canvas, "Inter size=18", _inter18);
        _drawLine(canvas, "Inter size=13 scaleX=1", _inter13_1_0);
        _drawLine(canvas, "Inter size=13 scaleX=2", _inter13_2_0);
        _drawLine(canvas, "Inter size=13 skewX=1", _inter13_1_1);
        _drawLine(canvas, "Inter size=13 skewX=-1", _inter13_1_neg1);

        assert Objects.equals(_defaultFont, _inter18) == false;

        boolean autoHintingForced = _inter13.isAutoHintingForced();
        _drawLine(canvas, "Inter 13px autoHintingForced=" + autoHintingForced + " (default)", _inter13);
        _inter13.setAutoHintingForced(!autoHintingForced);
        assert _inter13.isAutoHintingForced() == !autoHintingForced;
        _drawLine(canvas, "Inter 13px autoHintingForced=" + !autoHintingForced, _inter13);
        _inter13.setAutoHintingForced(autoHintingForced);
        assert _inter13.isAutoHintingForced() == autoHintingForced;
        
        boolean bitmapsEmbedded = _inter13.areBitmapsEmbedded();
        _drawLine(canvas, "Inter 13px bitmapsEmbedded=" + bitmapsEmbedded + " (default)", _inter13);
        _inter13.setBitmapsEmbedded(!bitmapsEmbedded);
        assert _inter13.areBitmapsEmbedded() == !bitmapsEmbedded;
        _drawLine(canvas, "Inter 13px bitmapsEmbedded=" + !bitmapsEmbedded, _inter13);
        _inter13.setBitmapsEmbedded(bitmapsEmbedded);
        assert _inter13.areBitmapsEmbedded() == bitmapsEmbedded;

        boolean subpixel = _inter13.isSubpixel();
        _drawLine(canvas, "Inter 13px subpixel=" + subpixel + " (default)", _inter13);
        _inter13.setSubpixel(!subpixel);
        assert _inter13.isSubpixel() == !subpixel;
        _drawLine(canvas, "Inter 13px subpixel=" + !subpixel, _inter13);
        _inter13.setSubpixel(subpixel);
        assert _inter13.isSubpixel() == subpixel;
        
        boolean metricsLinear = _inter13.areMetricsLinear();
        _drawLine(canvas, "Inter 13px metricsLinear=" + metricsLinear + " (default)", _inter13);
        _inter13.setMetricsLinear(!metricsLinear);
        assert _inter13.areMetricsLinear() == !metricsLinear;
        _drawLine(canvas, "Inter 13px metricsLinear=" + !metricsLinear, _inter13);
        _inter13.setMetricsLinear(metricsLinear);
        assert _inter13.areMetricsLinear() == metricsLinear;
        
        boolean emboldened = _inter13.isEmboldened();
        _drawLine(canvas, "Inter 13px emboldened=" + emboldened + " (default)", _inter13);
        _inter13.setEmboldened(!emboldened);
        assert _inter13.isEmboldened() == !emboldened;
        _drawLine(canvas, "Inter 13px emboldened=" + !emboldened, _inter13);
        _inter13.setEmboldened(emboldened);
        assert _inter13.isEmboldened() == emboldened;
        
        boolean baselineSnapped = _inter13.isBaselineSnapped();
        _drawLine(canvas, "Inter 13px baselineSnapped=" + baselineSnapped + " (default)", _inter13);
        _inter13.setBaselineSnapped(!baselineSnapped);
        assert _inter13.isBaselineSnapped() == !baselineSnapped;
        _drawLine(canvas, "Inter 13px baselineSnapped=" + !baselineSnapped, _inter13);
        _inter13.setBaselineSnapped(baselineSnapped);
        assert _inter13.isBaselineSnapped() == baselineSnapped;
    
        FontEdging defaultEdging = _inter13.getEdging();
        _drawLine(canvas, "Inter 13px edging=" + defaultEdging + " (default)", _inter13);
        for (FontEdging e: FontEdging.values()) {
            if (e != defaultEdging) {
                _inter13.setEdging(e);
                _drawLine(canvas, "Inter 13px edging=" + e, _inter13);
            }
        }
        _inter13.setEdging(defaultEdging);

        FontHinting defaultHinting = _inter13.getHinting();
        _drawLine(canvas, "Inter 13px hinting=" + defaultHinting + " (default)", _inter13);
        for (FontHinting e: FontHinting.values()) {
            if (e != defaultHinting) {
                _inter13.setHinting(e);
                _drawLine(canvas, "Inter 13px hinting=" + e, _inter13);
            }
        }
        _inter13.setHinting(defaultHinting);

        assert Objects.equals(_inter13, _inter18.makeWithSize(13));
        assert Objects.equals(null, _defaultFont.getTypeface());
        assert Objects.equals(_inter, _inter13.getTypeface());
        assert Objects.equals(Typeface.makeDefault(), _defaultFont.getTypefaceOrDefault());
        assert 13 == _inter13.getSize();
        assert 1 == _inter13.getScaleX();
        assert 0 == _inter13.getSkewX();

        _inter13.setTypeface(_jbMono);
        _inter13.setSize(18);
        _inter13.setScaleX(1.2f);
        _inter13.setSkewX(-0.2f);
        assert 18 == _inter13.getSize();
        assert 1.2f == _inter13.getScaleX();
        assert -0.2f == _inter13.getSkewX();
        _drawLine(canvas, "Inter 13px setTypeface=jbMono setSize=18 setScaleX=1.2 setSkewX=-0.2", _inter13);

        _inter13.setTypeface(_inter);
        _inter13.setSize(13);
        _inter13.setScaleX(1);
        _inter13.setSkewX(0);
        _drawLine(canvas, "Inter 13px setSize=13 setScaleX=1 setSkewX=0", _inter13);

        assert Arrays.equals(new short[] { 393, 709, 673, 701, 501 }, _inter13.getStringGlyphIds("Skija"));
        assert Arrays.equals(new short[] { 393, 709, 673, 701, 501 }, _inter13.getUTF32GlyphIds("Skija".codePoints().toArray()));
        assert 393 == _inter13.getUTF32GlyphId('S');
        assert 5 == _inter13.getStringGlyphsCount("Skija");

        String text = "Inter size=13 Font.measureText";
        float height = _drawLine(canvas, text, _inter18);
        Rect bounds = _inter18.measureText(text);
        canvas.drawRect(Rect.makeXYWH(bounds.getLeft(), -height, bounds.getWidth(), bounds.getHeight()), _boundaryPaint);
        
        text = "Inter size=13 Font.measureText(String, Paint)";
        height = _drawLine(canvas, text, _inter18);
        bounds = _inter18.measureText(text, _paint);
        canvas.drawRect(Rect.makeXYWH(bounds.getLeft(), -height, bounds.getWidth(), bounds.getHeight()), _boundaryPaint);
        
        text = "Inter size=13 Font.getWidths & Font.getXPositions";
        height = _drawLine(canvas, text, _inter18);
        bounds = _inter18.measureText(text, _paint);
        short[] glyphs = _inter18.getStringGlyphIds(text);
        float[] widths = _inter18.getWidths(glyphs);
        float[] xPositions = _inter18.getXPositions(glyphs, 5);
        for (int i = 0; i < glyphs.length; ++i) {
            canvas.drawRect(Rect.makeXYWH(xPositions[i] - 5, -height, widths[i], height), _boundaryPaint);
        }
        
        text = "Inter size=13 Font.getPositions";
        height = _drawLine(canvas, text, _inter18);
        glyphs = _inter18.getStringGlyphIds(text);
        widths = _inter18.getWidths(glyphs);
        Point[] positions = _inter18.getPositions(glyphs, new Point(3, -height));
        for (int i = 0; i < glyphs.length; ++i) {
            canvas.drawRect(Rect.makeXYWH(positions[i].getX() - 3, positions[i].getY(), widths[i], height), _boundaryPaint);
        }
        
        text = "Inter size=13 Font.getBounds";
        height = _drawLine(canvas, text, _inter18);
        glyphs = _inter18.getStringGlyphIds(text);        
        xPositions = _inter18.getXPositions(glyphs);
        Rect[] boundRects = _inter18.getBounds(glyphs);
        FontMetrics metrics = _inter18.getMetrics();
        for (int i = 0; i < glyphs.length; ++i) {
            canvas.drawRect(Rect.makeXYWH(xPositions[i] + boundRects[i].getLeft(), -metrics.getBottom() + boundRects[i].getTop(), boundRects[i].getWidth(), boundRects[i].getHeight()), _boundaryPaint);
        }

        text = "Inter size=13 Font.getBounds(short[], _Paint)";
        height = _drawLine(canvas, text, _inter18);
        glyphs = _inter18.getStringGlyphIds(text);        
        xPositions = _inter18.getXPositions(glyphs);
        boundRects = _inter18.getBounds(glyphs, _paint);
        for (int i = 0; i < glyphs.length; ++i) {
            canvas.drawRect(Rect.makeXYWH(xPositions[i] + boundRects[i].getLeft(), -metrics.getBottom() + boundRects[i].getTop(), boundRects[i].getWidth(), boundRects[i].getHeight()), _boundaryPaint);
        }

        canvas.translate(0, _inter48.getSpacing());
        canvas.save();
        try (Path p = _inter48.getPath(_inter48.getUTF32GlyphId('@'));) {
            bounds = p.computeTightBounds();
            canvas.drawPath(p, _stroke);
            canvas.translate(bounds.getWidth() + 10, 0);
        }

        for (Path p: _inter48.getPaths(_inter48.getStringGlyphIds("$&^"))) {
            bounds = p.computeTightBounds();
            canvas.drawPath(p, _stroke);
            canvas.translate(bounds.getWidth() + 10, 0);
            p.close();
        }
        canvas.restore();

        canvas.translate(0, _inter13.getSpacing());
        _drawLine(canvas, "FIN", _inter13);
    }
}
