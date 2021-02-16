package org.jetbrains.skija.examples.swt;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.opengl.*;
import org.eclipse.swt.widgets.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.Canvas;

public class Main {
    public static void main(String[] args) throws Exception {
        new Main().run();
    }

    private Display display;
    private GLCanvas glCanvas;
    private DirectContext context;
    private Surface surface;
    private BackendRenderTarget renderTarget;

    protected void run() throws Exception {
        display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Skija SWT Demo");
        shell.setLayout(new FillLayout());
        
        GLData data = new GLData();
        data.doubleBuffer = true;
        
        glCanvas = new GLCanvas(shell, SWT.NO_BACKGROUND | SWT.NO_REDRAW_RESIZE, data);
        glCanvas.setCurrent();
        context = DirectContext.makeGL();
        
        Listener listener = event -> {
            switch (event.type) {
                case SWT.Paint:
                    onPaint(event);
                    break;
                case SWT.Resize:
                    onResize(event);
                    break;
                case SWT.Dispose:
                    onDispose();
                    break;
            }
        };
        glCanvas.addListener(SWT.Paint, listener);
        glCanvas.addListener(SWT.Resize, listener);
        shell.addListener(SWT.Dispose, listener);
        
        shell.open();
    
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        
        display.dispose();
    }

    protected void release() {
        if (surface != null) {
            surface.close();
            surface = null;
        }
        if (renderTarget != null) {
            renderTarget.close();
            renderTarget = null;
        }
    }
    
    protected void onResize(Event event) {
        release();
        Rectangle rect = glCanvas.getClientArea();
        renderTarget = BackendRenderTarget.makeGL(rect.width, rect.height, /*samples*/0, /*stencil*/8, /*fbid*/0, FramebufferFormat.GR_GL_RGBA8);
        surface = Surface.makeFromBackendRenderTarget(context, renderTarget, SurfaceOrigin.BOTTOM_LEFT, SurfaceColorFormat.RGBA_8888, ColorSpace.getDisplayP3(), new SurfaceProps(PixelGeometry.RGB_H));
    }
    
    protected void onPaint(Event event) {
        if (surface == null)
            return;
        Canvas canvas = surface.getCanvas();
        paint(canvas);
        context.flush();
        glCanvas.swapBuffers();
    }
    
    protected void onDispose() {        
        release();
        context.close();
    }
    
    protected void paint(Canvas canvas) {
        canvas.clear(0xFFFFFFFF);
        canvas.save();
        canvas.translate(100, 100);
        canvas.rotate(System.currentTimeMillis() % 1000 / 1000f * 360f);
        canvas.drawRect(Rect.makeLTRB(-100, -100, 100, 100), new Paint().setColor(0xFFCC3333));
        canvas.restore();
    }
}