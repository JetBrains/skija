package org.jetbrains.skija.examples.jwm;

import org.jetbrains.jwm.*;
import org.jetbrains.skija.*;

public class SkijaLayerGL extends LayerGL implements SkijaLayer {
    public DirectContext _directContext;
    public BackendRenderTarget _renderTarget;
    public Surface _surface;

    @Override
    public Canvas beforePaint() {
        if (_directContext == null)
            _directContext = DirectContext.makeGL();

        if (_renderTarget == null)
            _renderTarget = BackendRenderTarget.makeGL(
                             getWidth(),
                             getHeight(),
                             /*samples*/0,
                             /*stencil*/8,
                             /*fbId*/0,
                             FramebufferFormat.GR_GL_RGBA8);

        if (_surface == null)
            _surface = Surface.makeFromBackendRenderTarget(
                        _directContext,
                        _renderTarget,
                        SurfaceOrigin.BOTTOM_LEFT,
                        SurfaceColorFormat.RGBA_8888,
                        ColorSpace.getDisplayP3(),  // TODO load monitor profile
                        new SurfaceProps(PixelGeometry.RGB_H));

        return _surface.getCanvas();
    }

    @Override
    public void afterPaint() {
        _surface.flushAndSubmit();
        swapBuffers();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        if (_surface != null) {
            _surface.close();
            _surface = null;
        }

        if (_renderTarget != null) {
            _renderTarget.close();
            _renderTarget = null;
        }

        if (_directContext != null) {
            _directContext.abandon();
            _directContext.close();
            _directContext = null;
        }
    }

    @Override
    public void close() {
        if (_directContext != null) {
            _directContext.abandon();
            _directContext.close();
            _directContext = null;
        }

        if (_surface != null) {
            _surface.close();
            _surface = null;
        }

        if (_renderTarget != null) {
            _renderTarget.close();
            _renderTarget = null;
        }

        super.close();
    }
}