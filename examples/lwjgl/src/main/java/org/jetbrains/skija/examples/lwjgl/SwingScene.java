package org.jetbrains.skija.examples.lwjgl;

import lombok.SneakyThrows;
import org.jetbrains.skija.*;
import org.jetbrains.skija.Canvas;
import org.jetbrains.skija.Font;
import org.jetbrains.skija.Image;
import org.jetbrains.skija.Paint;
import org.jetbrains.skija.Point;

import java.awt.*;
import java.util.Arrays;
import java.util.Map;

public class SwingScene implements Scene {
    // public final javax.swing.JPanel panel;

    // @SneakyThrows
    public SwingScene() {
        // System.out.println(Arrays.toString(UIManager.getInstalledLookAndFeels()));

        // javax.swing.UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        // javax.swing.UIManager.setLookAndFeel("com.apple.laf.AquaLookAndFeel");
        // javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
        // javax.swing.UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

        // javax.swing.JFrame f=new javax.swing.JFrame();//creating instance of JFrame
        // var b=new javax.swing.JComboBox<String>(new String[] {"Swing Button"});//creating instance of JButton
        // b.setBounds(130,100,200, 50);//x axis, y axis, width, height
        // f.add(b);//adding button in JFrame
        // f.setSize(400,500);//400 width and 500 height
        // f.setLayout(null);//using no layout managers
        // f.setVisible(true);//making the frame visible
    }

    @Override
    @SneakyThrows
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        canvas.translate(20, 20);

        for (var lfi: javax.swing.UIManager.getInstalledLookAndFeels()) {
            if (lfi.getName() != "Mac OS X") {
            // if (lfi.getName() == "Nimbus") {
                javax.swing.UIManager.setLookAndFeel(lfi.getClassName());
                System.out.println("\n↓↓↓ " + lfi.getName() + " via " + lfi.getClassName());

                var panel = new javax.swing.JPanel();
                panel.setLayout(new javax.swing.BoxLayout(panel, javax.swing.BoxLayout.LINE_AXIS));
                panel.setSize(500, 40);
                var combobox = new javax.swing.JComboBox<>(new String[]{"javax.swing.JComboBox"});
                combobox.setSize(300, 25);
                panel.add(combobox);
                // var checkbox = new javax.swing.JCheckBox("Checkbox");
                // checkbox.doClick();
                // checkbox.setSize(100, 25);

                canvas.save();
                var g = new SkiaGraphics(canvas);
                panel.paint(g);
                g.dispose();
                canvas.restore();

                System.out.println("↑↑↑");
                canvas.translate(0, 50);
           }
       }
    }
}

class SkiaGraphics extends java.awt.Graphics2D {
    public java.awt.Color color;
    public java.awt.Color backgroundColor;
    private static final java.awt.Font defaultFont = new java.awt.Font(java.awt.Font.DIALOG, java.awt.Font.PLAIN, 12);
    protected java.awt.Font font;

    public final Canvas canvas;
    public final Paint paint = new Paint().setColor(0xFFFFFFFF).setStrokeWidth(1).setStrokeCap(PaintStrokeCap.SQUARE);
    public final Paint backgroundPaint = new Paint().setColor(0xFF000000);

    public final String indent;
    public Font skiaFont = null;
    public Matrix33 matrix;
    public java.awt.Shape clip = null;

    public static Matrix33 canvasMatrix = Matrix33.IDENTITY;

    public SkiaGraphics(Canvas canvas) { this(canvas, "", Matrix33.IDENTITY, null, null, null); }

    public SkiaGraphics(Canvas canvas, String indent, Matrix33 matrix, java.awt.Shape clip, java.awt.Font font, Font skiaFont) {
        this.canvas = canvas;
        this.indent = indent;
        this.matrix = matrix;
        this.clip = clip;
        this.font = font;
        this.skiaFont = skiaFont;
    }

    public void beforeDraw() {
        // System.out.println(matrix);
        if (canvasMatrix != matrix) {
            canvas.restore();
            canvas.save();
            canvas.concat(matrix);
            canvasMatrix = matrix;
        }
    }

    @Override
    public void draw(java.awt.Shape s) {
        System.out.println(indent + "draw");
    }

    @Override
    public boolean drawImage(java.awt.Image img, java.awt.geom.AffineTransform xform, java.awt.image.ImageObserver obs) {
        System.out.println(indent + "drawImage");    
        return false;
    }

    @Override
    public void drawImage(java.awt.image.BufferedImage img, java.awt.image.BufferedImageOp op, int x, int y) {
        System.out.println(indent + "drawImage");

    }

    @Override
    public void drawRenderedImage(java.awt.image.RenderedImage img, java.awt.geom.AffineTransform xform) {
        System.out.println(indent + "drawRenderedImage");

    }

    @Override
    public void drawRenderableImage(java.awt.image.renderable.RenderableImage img, java.awt.geom.AffineTransform xform) {
        System.out.println(indent + "drawRenderableImage");

    }

    @Override
    public void drawString(String str, int x, int y) {
        drawString(str, (float) x, (float) y);
    }

    @Override
    public void drawString(String str, float x, float y) {
        System.out.println(indent + "[+] drawString");
        if (font == null)
            setFont(defaultFont);
        beforeDraw();
        canvas.drawString(str, x, y, skiaFont, paint);
    }

    @Override
    public void drawString(java.text.AttributedCharacterIterator iterator, int x, int y) {
        System.out.println(indent + "drawString");
    }

    @Override
    public void drawString(java.text.AttributedCharacterIterator iterator, float x, float y) {
        System.out.println(indent + "drawString");

    }

    @Override
    public void drawGlyphVector(java.awt.font.GlyphVector g, float x, float y) {
        System.out.println(indent + "drawGlyphVector");

    }

    @Override
    public void fill(java.awt.Shape s) {
        System.out.println(indent + "[+] fill " + s);
        beforeDraw();

        if (s instanceof java.awt.geom.Path2D) {
           try (var path = new Path();) {
               var iter = s.getPathIterator(null);
               float[] segment = new float[6];
               while (!iter.isDone()) {
                   int type = iter.currentSegment(segment);
                   switch (type) {
                       case java.awt.geom.PathIterator.SEG_MOVETO:
                           path.moveTo(new Point(segment[0], segment[1]));
                           // System.out.println("MOVETO " + segment[0] + " " + segment[1]);
                           break;
                       case java.awt.geom.PathIterator.SEG_LINETO:
                           path.lineTo(new Point(segment[0], segment[1]));
                           // System.out.println("LINETO " + segment[0] + " " + segment[1]);
                           break;
                       case java.awt.geom.PathIterator.SEG_QUADTO:
                           path.quadTo(segment[0], segment[1], segment[2], segment[3]);
                           // System.out.println("QUADTO " + segment[0] + " " + segment[1] + " " + segment[2] + " " + segment[3]);
                           break;
                       case java.awt.geom.PathIterator.SEG_CUBICTO:
                           path.cubicTo(segment[0], segment[1], segment[2], segment[3], segment[4], segment[5]);
                           // System.out.println("CUBICTO " + segment[0] + " " + segment[1] + " " + segment[2] + " " + segment[3] + " " + segment[4] + " " + segment[5]);
                           break;
                       case java.awt.geom.PathIterator.SEG_CLOSE:
                           path.closePath();
                           break;
                   }
                   iter.next();
               }

               switch (((java.awt.geom.Path2D) s).getWindingRule()) {
                   case java.awt.geom.Path2D.WIND_EVEN_ODD:
                       path.setFillMode(PathFillMode.EVEN_ODD);
                   case java.awt.geom.Path2D.WIND_NON_ZERO:
                       path.setFillMode(PathFillMode.WINDING);
               }

               canvas.drawPath(path, paint);
           }
        }
    }

    @Override
    public boolean hit(java.awt.Rectangle rect, java.awt.Shape s, boolean onStroke) {
        System.out.println(indent + "hit");    
        return false;
    }

    @Override
    public java.awt.GraphicsConfiguration getDeviceConfiguration() {
        System.out.println(indent + "[+] getDeviceConfiguration");    
        return SkiaGraphicsConfig.INSTANCE;
    }

    @Override
    public void setComposite(java.awt.Composite comp) {
        System.out.println(indent + "[/] setComposite " + comp);
        if (comp == AlphaComposite.Clear)
            paint.setBlendMode(BlendMode.CLEAR);
        else if (comp == AlphaComposite.SrcOver)
            paint.setBlendMode(BlendMode.SRC_OVER);
        else if (comp instanceof AlphaComposite)
            System.out.println(indent + " UNKNOWN COMPOSITE MODE " + ((AlphaComposite) comp).getRule());
    }

    @Override
    public void setPaint(java.awt.Paint paint) {
        if (paint instanceof java.awt.Color) {
            System.out.println(indent + "[+] setPaint " + paint);
            setColor((java.awt.Color) paint);
        } else if (paint instanceof java.awt.LinearGradientPaint) {
            var gr = (java.awt.LinearGradientPaint) paint;

            int[] colors = new int[gr.getColors().length];
            for (int i = 0; i < colors.length; ++i)
                colors[i] = gr.getColors()[i].getRGB();

            System.out.println(indent + "[+] setPaint " + gr.getStartPoint() + " " + gr.getEndPoint() + " " + Arrays.toString(colors) + " " + Arrays.toString(gr.getFractions()));

            var shader = Shader.makeLinearGradient((float) gr.getStartPoint().getX(),
                    (float) gr.getStartPoint().getY(),
                    (float) gr.getEndPoint().getX(),
                    (float) gr.getEndPoint().getY(),
                    colors,
                    gr.getFractions());
            this.paint.setShader(shader);
        } else {
            System.out.println(indent + "setPaint " + paint);
        }
    }

    @Override
    public void setStroke(java.awt.Stroke s) {
        System.out.println(indent + "setStroke");

    }

    @Override
    public void setRenderingHint(java.awt.RenderingHints.Key hintKey, Object hintValue) {
        System.out.println(indent + "setRenderingHint " + hintKey + "=" + hintValue);

    }

    @Override
    public Object getRenderingHint(java.awt.RenderingHints.Key hintKey) {
        System.out.println(indent + "getRenderingHint " + hintKey);    
        return null;
    }

    @Override
    public void setRenderingHints(Map<?, ?> hints) {
        System.out.println(indent + "setRenderingHints");

    }

    @Override
    public void addRenderingHints(Map<?, ?> hints) {
        System.out.println(indent + "addRenderingHints");

    }

    @Override
    public java.awt.RenderingHints getRenderingHints() {
        System.out.println(indent + "getRenderingHints");    
        return null;
    }

    @Override
    public void translate(int x, int y) {
        translate((double) x, (double) y);
    }

    @Override
    public void translate(double tx, double ty) {
        System.out.println(String.format(indent + "[+] translate %f, %f", tx, ty));
        // canvas.translate((float) tx, (float) ty);
        matrix = matrix.makeConcat(Matrix33.makeTranslate((float) tx, (float) ty));
        // matrix = Matrix33.makeTranslate((float) tx, (float) ty).makeConcat(matrix);
        if (clip != null && clip instanceof java.awt.Rectangle)
            ((java.awt.Rectangle) clip).translate((int) -tx, (int) -ty);
    }

    @Override
    public void rotate(double theta) {
        System.out.println(indent + "rotate " + theta );

    }

    @Override
    public void rotate(double theta, double x, double y) {
        System.out.println(indent + "rotate");

    }

    @Override
    public void scale(double sx, double sy) {
        System.out.println(indent + "scale");

    }

    @Override
    public void shear(double shx, double shy) {
        System.out.println(indent + "shear");

    }

    @Override
    public void transform(java.awt.geom.AffineTransform Tx) {
        System.out.println(indent + "transform");

    }

    @Override
    public void setTransform(java.awt.geom.AffineTransform Tx) {
        System.out.println(indent + "setTransform");

    }

    @Override
    public java.awt.geom.AffineTransform getTransform() {
        System.out.println(indent + "getTransform");    
        return null;
    }

    @Override
    public java.awt.Paint getPaint() {
        System.out.println(indent + "getPaint");    
        return null;
    }

    @Override
    public java.awt.Composite getComposite() {
        System.out.println(indent + "getComposite");    
        return null;
    }

    @Override
    public void setBackground(java.awt.Color color) {
        System.out.println(indent + "[+] setBackground " + color);
        this.backgroundColor = color;
        backgroundPaint.setColor(color.getRGB());
    }

    @Override
    public java.awt.Color getBackground() {
        System.out.println(indent + "[+] getBackground");    
        return backgroundColor;
    }

    @Override
    public java.awt.Stroke getStroke() {
        System.out.println(indent + "getStroke");    
        return null;
    }

    @Override
    public void clip(java.awt.Shape s) {
        System.out.println(indent + "clip");

    }

    @Override
    public java.awt.font.FontRenderContext getFontRenderContext() {
        System.out.println(indent + "getFontRenderContext");    
        return null;
    }

    @Override
    public java.awt.Graphics create() {
        System.out.println(indent + "[+] create");
        return new SkiaGraphics(canvas, indent + "  ", matrix, clip, font, skiaFont);
    }

    @Override
    public java.awt.Color getColor() {
        System.out.println(indent + "[+] getColor");    
        return color;
    }

    @Override
    public void setColor(java.awt.Color c) {
        System.out.println(indent + "[+] setColor " + c);
        this.color = c;
        paint.setColor(c.getRGB());
        paint.setShader(null);
    }

    @Override
    public void setPaintMode() {
        System.out.println(indent + "setPaintMode");

    }

    @Override
    public void setXORMode(java.awt.Color c1) {
        System.out.println(indent + "setXORMode");

    }

    @Override
    public java.awt.Font getFont() {
        System.out.println(indent + "[+] getFont");    
        if (font == null)
            setFont(defaultFont);
        return font;
    }

    @Override
    public void setFont(java.awt.Font font) {
        System.out.println(indent + "[+] setFont " + font);
        this.font = font;

        FontStyle style;
        if (font.getStyle() == java.awt.Font.PLAIN)
            style = FontStyle.NORMAL;
        else if (font.getStyle() == java.awt.Font.BOLD)
            style = FontStyle.BOLD;
        else if (font.getStyle() == java.awt.Font.ITALIC)
            style = FontStyle.ITALIC;
        else if (font.getStyle() == java.awt.Font.BOLD + java.awt.Font.ITALIC)
            style = FontStyle.BOLD_ITALIC;
        else
            throw new RuntimeException("Unknown font style: " + font.getStyle() + " in " + font);

        var typeface = FontMgr.getDefault().matchFamilyStyle("System Font", style); // TODO family
        if (skiaFont != null)
            skiaFont.close();
        skiaFont = new Font(typeface, font.getSize());
    }

    @Override
    public java.awt.FontMetrics getFontMetrics(java.awt.Font f) {
        System.out.println(indent + "getFontMetrics");    
        return null;
    }

    @Override
    public java.awt.Rectangle getClipBounds() {
        System.out.println(indent + "[+] getClipBounds");    
        return clip == null ? null : clip instanceof java.awt.Rectangle ? (java.awt.Rectangle) clip : clip.getBounds();
    }

    @Override
    public void clipRect(int x, int y, int width, int height) {
        System.out.println(String.format(indent + "[+] clipRect %d %d %d %d", x, y, width, height));
        // canvas.clipRect(Rect.makeXYWH(x, y, width, height));
        if (clip == null)
            clip = new java.awt.Rectangle(x, y, width, height);
        else if (clip instanceof java.awt.Rectangle)
            clip = ((java.awt.Rectangle) clip).intersection(new java.awt.Rectangle(x, y, width, height));
        else
            clip = new java.awt.Rectangle(x, y, width, height);
    }

    @Override
    public void setClip(int x, int y, int width, int height) {
        System.out.println(String.format(indent + "[/] setClip %d %d %d %d", x, y, width, height));
        // canvas.clipRect(Rect.makeXYWH(x, y, width, height)); // TODO must reset
        clip = new java.awt.Rectangle(x, y, width, height);
    }

    @Override
    public java.awt.Shape getClip() {
        System.out.println(indent + "[+] getClip");
        return clip;
    }

    @Override
    public void setClip(java.awt.Shape clip) {
        System.out.println(indent + "setClip");
        // TODO non-rectangular clips
        this.clip = clip;
    }

    @Override
    public void copyArea(int x, int y, int width, int height, int dx, int dy) {
        System.out.println(indent + "copyArea");

    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        System.out.println(String.format(indent + "[+] drawLine %d, %d -> %d, %d", x1, y1, x2, y2));
        beforeDraw();
        canvas.drawLine(x1, y1, x2, y2, paint);
    }

    @Override
    public void fillRect(int x, int y, int width, int height) {
        System.out.println(String.format(indent + "[+] fillRect %d, %d, %d, %d", x, y, width, height));
        beforeDraw();
        canvas.drawRect(Rect.makeXYWH(x, y, width, height), paint);
    }

    @Override
    public void clearRect(int x, int y, int width, int height) {
        System.out.println(String.format(indent + "clearRect %d, %d, %d, %d", x, y, width, height));

    }

    @Override
    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        System.out.println(indent + "drawRoundRect");

    }

    @Override
    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        System.out.println(indent + "fillRoundRect");

    }

    @Override
    public void drawOval(int x, int y, int width, int height) {
        System.out.println(indent + "drawOval");

    }

    @Override
    public void fillOval(int x, int y, int width, int height) {
        System.out.println(indent + "fillOval");

    }

    @Override
    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        System.out.println(indent + "drawArc");

    }

    @Override
    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        System.out.println(indent + "fillArc");

    }

    @Override
    public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
        System.out.println(indent + "drawPolyline");

    }

    @Override
    public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        System.out.println(indent + "drawPolygon");

    }

    @Override
    public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        System.out.println(indent + "[+] fillPolygon " + Arrays.toString(xPoints) + " " + Arrays.toString(yPoints) + " " + nPoints);
        beforeDraw();
        try (var path = new Path()) {
            path.moveTo(xPoints[0], yPoints[0]);
            for (int i = 1; i < nPoints; ++i)
                path.lineTo(xPoints[i], yPoints[i]);
            path.lineTo(xPoints[0], yPoints[0]);
            path.closePath();
            canvas.drawPath(path, paint);
        }
    }

    @Override
    public boolean drawImage(java.awt.Image img, int x, int y, java.awt.image.ImageObserver observer) {
        return drawImage(img, x, y, img.getWidth(null), img.getHeight(null), null, observer);
    }

    @Override
    public boolean drawImage(java.awt.Image img, int x, int y, int width, int height, java.awt.image.ImageObserver observer) {
        return drawImage(img, x, y, x + width, y + height, 0, 0, width, height, null, observer);
    }

    @Override
    public boolean drawImage(java.awt.Image img, int x, int y, java.awt.Color bgcolor, java.awt.image.ImageObserver observer) {
        return drawImage(img, x, y, img.getWidth(null), img.getHeight(null), bgcolor, observer);
    }

    @Override
    public boolean drawImage(java.awt.Image img, int x, int y, int width, int height, java.awt.Color bgcolor, java.awt.image.ImageObserver observer) {
        return drawImage(img, x, y, x + width, y + height, 0, 0, width, height, bgcolor, observer);
    }

    @Override
    public boolean drawImage(java.awt.Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, java.awt.image.ImageObserver observer) {
        return drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null, observer);
    }

    @Override
    public boolean drawImage(java.awt.Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, java.awt.Color bgcolor, java.awt.image.ImageObserver observer) {
        if (img instanceof SkiaVolatileImage) {
            System.out.println(indent + String.format("[+] drawImage dx1=%d dy1=%d dx2=%d dy2=%d sx1=%d sy1=%d sx2=%d sy2=%d color=%s observer=%s", dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, bgcolor, observer));
            beforeDraw();
            try (Image image = ((SkiaVolatileImage) img).surface.makeImageSnapshot();) {
                canvas.drawImageIRect(image, IRect.makeLTRB(sx1, sy1, sx2, sy2), Rect.makeLTRB(dx1, dy1, dx2, dy2));
            }
        } else {
            System.out.println(indent + String.format("drawImage %s dx1=%d dy1=%d dx2=%d dy2=%d sx1=%d sy1=%d sx2=%d sy2=%d color=%s observer=%s", img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, bgcolor, observer));
        }
        return false;
    }

    @Override
    public void dispose() {
        System.out.println(indent + "[+] dispose");
    }
}

class SkiaGraphicsConfig extends java.awt.GraphicsConfiguration {
    public static final SkiaGraphicsConfig INSTANCE = new SkiaGraphicsConfig();

    @Override
    public java.awt.GraphicsDevice getDevice() {
        System.out.println("GC getDevice");
        return null;
    }

    @Override
    public java.awt.image.ColorModel getColorModel() {
        System.out.println("GC getColorModel");
        return null;
    }

    @Override
    public java.awt.image.ColorModel getColorModel(int transparency) {
        System.out.println("GC getColorModel(int)");
        return null;
    }

    @Override
    public java.awt.geom.AffineTransform getDefaultTransform() {
        System.out.println("GC getDefaultTransform");
        return null;
    }

    @Override
    public java.awt.geom.AffineTransform getNormalizingTransform() {
        System.out.println("GC getNormalizingTransform");
        return null;
    }

    @Override
    public java.awt.Rectangle getBounds() {
        System.out.println("GC getBounds");
        return null;
    }

    @Override
    public java.awt.image.VolatileImage createCompatibleVolatileImage(int width, int height, java.awt.ImageCapabilities caps, int transparency) throws java.awt.AWTException {
        System.out.println("GC [+] createCompatibleVolatileImage " + width + "x" + height + " " + caps + " " + transparency);
        return new SkiaVolatileImage(width, height, caps, transparency);
    }
}

class SkiaVolatileImage extends java.awt.image.VolatileImage {
    public final int width;
    public final int height;
    public final java.awt.ImageCapabilities caps;
    public final Surface surface;

    public SkiaVolatileImage(int width, int height, java.awt.ImageCapabilities caps, int transparency) {
        this.width = width;
        this.height = height;
        this.caps = caps;
        this.surface = Surface.makeRasterN32Premul(width, height);
    }

    @Override
    public java.awt.image.BufferedImage getSnapshot() {
        System.out.println("GC getSnapshot");
        return null;
    }

    @Override
    public int getWidth() {
        System.out.println("GC [+] getWidth");
        return width;
    }

    @Override
    public int getHeight() {
        System.out.println("GC [+] getHeight");
        return height;
    }

    @Override
    public java.awt.Graphics2D createGraphics() {
        System.out.println("GC createGraphics");
        return new SkiaGraphics(surface.getCanvas());
    }

    @Override
    public int validate(java.awt.GraphicsConfiguration gc) {
        System.out.println("GC validate");
        return 0;
    }

    @Override
    public boolean contentsLost() {
        System.out.println("GC contentLost");
        return false;
    }

    @Override
    public java.awt.ImageCapabilities getCapabilities() {
        System.out.println("GC [+] getCapabilities");
        return caps;
    }

    @Override
    public int getWidth(java.awt.image.ImageObserver observer) {
        System.out.println("GC [+] getWidth(ImageObserver)");
        return width;
    }

    @Override
    public int getHeight(java.awt.image.ImageObserver observer) {
        System.out.println("GC [+] getHeight(ImageObserver)");
        return height;
    }

    @Override
    public Object getProperty(String name, java.awt.image.ImageObserver observer) {
        System.out.println("GC getProperty(" + name + ", ImageObserver)");
        return null;
    }
}