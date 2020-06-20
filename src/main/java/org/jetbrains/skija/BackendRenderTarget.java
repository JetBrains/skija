package org.jetbrains.skija;

import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.Managed;
import org.jetbrains.skija.impl.Stats;

public class BackendRenderTarget extends Managed {
    public static class FramebufferFormat {
        public static int GR_GL_STENCIL_INDEX                = 0x1901;
        public static int GR_GL_DEPTH_COMPONENT              = 0x1902;
        public static int GR_GL_DEPTH_STENCIL                = 0x84F9;
        public static int GR_GL_RED                          = 0x1903;
        public static int GR_GL_RED_INTEGER                  = 0x8D94;
        public static int GR_GL_GREEN                        = 0x1904;
        public static int GR_GL_BLUE                         = 0x1905;
        public static int GR_GL_ALPHA                        = 0x1906;
        public static int GR_GL_LUMINANCE                    = 0x1909;
        public static int GR_GL_LUMINANCE_ALPHA              = 0x190A;
        public static int GR_GL_RG_INTEGER                   = 0x8228;
        public static int GR_GL_RGB                          = 0x1907;
        public static int GR_GL_RGB_INTEGER                  = 0x8D98;
        public static int GR_GL_SRGB                         = 0x8C40;
        public static int GR_GL_RGBA                         = 0x1908;
        public static int GR_GL_RG                           = 0x8227;
        public static int GR_GL_SRGB_ALPHA                   = 0x8C42;
        public static int GR_GL_RGBA_INTEGER                 = 0x8D99;
        public static int GR_GL_BGRA                         = 0x80E1;

        /* Stencil index sized formats */
        public static int GR_GL_STENCIL_INDEX4               = 0x8D47;
        public static int GR_GL_STENCIL_INDEX8               = 0x8D48;
        public static int GR_GL_STENCIL_INDEX16              = 0x8D49;

        /* Depth component sized formats */
        public static int GR_GL_DEPTH_COMPONENT16            = 0x81A5;

        /* Depth stencil sized formats */
        public static int GR_GL_DEPTH24_STENCIL8             = 0x88F0;

        /* Red sized formats */
        public static int GR_GL_R8                           = 0x8229;
        public static int GR_GL_R16                          = 0x822A;
        public static int GR_GL_R16F                         = 0x822D;
        public static int GR_GL_R32F                         = 0x822E;

        /* Red integer sized formats */
        public static int GR_GL_R8I                          = 0x8231;
        public static int GR_GL_R8UI                         = 0x8232;
        public static int GR_GL_R16I                         = 0x8233;
        public static int GR_GL_R16UI                        = 0x8234;
        public static int GR_GL_R32I                         = 0x8235;
        public static int GR_GL_R32UI                        = 0x8236;

        /* Luminance sized formats */
        public static int GR_GL_LUMINANCE8                   = 0x8040;
        public static int GR_GL_LUMINANCE16F                 = 0x881E;

        /* Alpha sized formats */
        public static int GR_GL_ALPHA8                       = 0x803C;
        public static int GR_GL_ALPHA16                      = 0x803E;
        public static int GR_GL_ALPHA16F                     = 0x881C;
        public static int GR_GL_ALPHA32F                     = 0x8816;

        /* Alpha integer sized formats */
        public static int GR_GL_ALPHA8I                      = 0x8D90;
        public static int GR_GL_ALPHA8UI                     = 0x8D7E;
        public static int GR_GL_ALPHA16I                     = 0x8D8A;
        public static int GR_GL_ALPHA16UI                    = 0x8D78;
        public static int GR_GL_ALPHA32I                     = 0x8D84;
        public static int GR_GL_ALPHA32UI                    = 0x8D72;

        /* RG sized formats */
        public static int GR_GL_RG8                          = 0x822B;
        public static int GR_GL_RG16                         = 0x822C;
        // public static int GR_GL_R16F                         = 0x822D;
        // public static int GR_GL_R32F                         = 0x822E;
        public static int GR_GL_RG16F                        = 0x822F;

        /* RG sized integer formats */
        public static int GR_GL_RG8I                         = 0x8237;
        public static int GR_GL_RG8UI                        = 0x8238;
        public static int GR_GL_RG16I                        = 0x8239;
        public static int GR_GL_RG16UI                       = 0x823A;
        public static int GR_GL_RG32I                        = 0x823B;
        public static int GR_GL_RG32UI                       = 0x823C;

        /* RGB sized formats */
        public static int GR_GL_RGB5                         = 0x8050;
        public static int GR_GL_RGB565                       = 0x8D62;
        public static int GR_GL_RGB8                         = 0x8051;
        public static int GR_GL_SRGB8                        = 0x8C41;

        /* RGB integer sized formats */
        public static int GR_GL_RGB8I                        = 0x8D8F;
        public static int GR_GL_RGB8UI                       = 0x8D7D;
        public static int GR_GL_RGB16I                       = 0x8D89;
        public static int GR_GL_RGB16UI                      = 0x8D77;
        public static int GR_GL_RGB32I                       = 0x8D83;
        public static int GR_GL_RGB32UI                      = 0x8D71;

        /* RGBA sized formats */
        public static int GR_GL_RGBA4                        = 0x8056;
        public static int GR_GL_RGB5_A1                      = 0x8057;
        public static int GR_GL_RGBA8                        = 0x8058;
        public static int GR_GL_RGB10_A2                     = 0x8059;
        public static int GR_GL_SRGB8_ALPHA8                 = 0x8C43;
        public static int GR_GL_RGBA16F                      = 0x881A;
        public static int GR_GL_RGBA32F                      = 0x8814;
        public static int GR_GL_RG32F                        = 0x8230;
        public static int GR_GL_RGBA16                       = 0x805B;

        /* RGBA integer sized formats */
        public static int GR_GL_RGBA8I                       = 0x8D8E;
        public static int GR_GL_RGBA8UI                      = 0x8D7C;
        public static int GR_GL_RGBA16I                      = 0x8D88;
        public static int GR_GL_RGBA16UI                     = 0x8D76;
        public static int GR_GL_RGBA32I                      = 0x8D82;
        public static int GR_GL_RGBA32UI                     = 0x8D70;

        /* BGRA sized formats */
        public static int GR_GL_BGRA8                        = 0x93A1;
    }

    public static BackendRenderTarget makeGL(int width, int height, int sampleCnt, int stencilBits, int fbId, int fbFormat) {
        Stats.onNativeCall();
        return new BackendRenderTarget(_nMakeGL(width, height, sampleCnt, stencilBits, fbId, fbFormat));
    }

    @ApiStatus.Internal
    public BackendRenderTarget(long ptr) {
        super(ptr, _finalizerPtr);
    }

    public static final long _finalizerPtr = _nGetFinalizer();
    public static native long _nGetFinalizer();
    public static native long _nMakeGL(int width, int height, int sampleCnt, int stencilBits, int fbId, int fbFormat);
}