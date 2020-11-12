package org.jetbrains.skija;

public interface FramebufferFormat {
    int GR_GL_STENCIL_INDEX                = 0x1901;
    int GR_GL_DEPTH_COMPONENT              = 0x1902;
    int GR_GL_DEPTH_STENCIL                = 0x84F9;
    int GR_GL_RED                          = 0x1903;
    int GR_GL_RED_INTEGER                  = 0x8D94;
    int GR_GL_GREEN                        = 0x1904;
    int GR_GL_BLUE                         = 0x1905;
    int GR_GL_ALPHA                        = 0x1906;
    int GR_GL_LUMINANCE                    = 0x1909;
    int GR_GL_LUMINANCE_ALPHA              = 0x190A;
    int GR_GL_RG_INTEGER                   = 0x8228;
    int GR_GL_RGB                          = 0x1907;
    int GR_GL_RGB_INTEGER                  = 0x8D98;
    int GR_GL_SRGB                         = 0x8C40;
    int GR_GL_RGBA                         = 0x1908;
    int GR_GL_RG                           = 0x8227;
    int GR_GL_SRGB_ALPHA                   = 0x8C42;
    int GR_GL_RGBA_INTEGER                 = 0x8D99;
    int GR_GL_BGRA                         = 0x80E1;

    /* Stencil index sized formats */
    int GR_GL_STENCIL_INDEX4               = 0x8D47;
    int GR_GL_STENCIL_INDEX8               = 0x8D48;
    int GR_GL_STENCIL_INDEX16              = 0x8D49;

    /* Depth component sized formats */
    int GR_GL_DEPTH_COMPONENT16            = 0x81A5;

    /* Depth stencil sized formats */
    int GR_GL_DEPTH24_STENCIL8             = 0x88F0;

    /* Red sized formats */
    int GR_GL_R8                           = 0x8229;
    int GR_GL_R16                          = 0x822A;
    int GR_GL_R16F                         = 0x822D;
    int GR_GL_R32F                         = 0x822E;

    /* Red integer sized formats */
    int GR_GL_R8I                          = 0x8231;
    int GR_GL_R8UI                         = 0x8232;
    int GR_GL_R16I                         = 0x8233;
    int GR_GL_R16UI                        = 0x8234;
    int GR_GL_R32I                         = 0x8235;
    int GR_GL_R32UI                        = 0x8236;

    /* Luminance sized formats */
    int GR_GL_LUMINANCE8                   = 0x8040;
    int GR_GL_LUMINANCE16F                 = 0x881E;

    /* Alpha sized formats */
    int GR_GL_ALPHA8                       = 0x803C;
    int GR_GL_ALPHA16                      = 0x803E;
    int GR_GL_ALPHA16F                     = 0x881C;
    int GR_GL_ALPHA32F                     = 0x8816;

    /* Alpha integer sized formats */
    int GR_GL_ALPHA8I                      = 0x8D90;
    int GR_GL_ALPHA8UI                     = 0x8D7E;
    int GR_GL_ALPHA16I                     = 0x8D8A;
    int GR_GL_ALPHA16UI                    = 0x8D78;
    int GR_GL_ALPHA32I                     = 0x8D84;
    int GR_GL_ALPHA32UI                    = 0x8D72;

    /* RG sized formats */
    int GR_GL_RG8                          = 0x822B;
    int GR_GL_RG16                         = 0x822C;
    // int GR_GL_R16F                         = 0x822D;
    // int GR_GL_R32F                         = 0x822E;
    int GR_GL_RG16F                        = 0x822F;

    /* RG sized integer formats */
    int GR_GL_RG8I                         = 0x8237;
    int GR_GL_RG8UI                        = 0x8238;
    int GR_GL_RG16I                        = 0x8239;
    int GR_GL_RG16UI                       = 0x823A;
    int GR_GL_RG32I                        = 0x823B;
    int GR_GL_RG32UI                       = 0x823C;

    /* RGB sized formats */
    int GR_GL_RGB5                         = 0x8050;
    int GR_GL_RGB565                       = 0x8D62;
    int GR_GL_RGB8                         = 0x8051;
    int GR_GL_SRGB8                        = 0x8C41;

    /* RGB integer sized formats */
    int GR_GL_RGB8I                        = 0x8D8F;
    int GR_GL_RGB8UI                       = 0x8D7D;
    int GR_GL_RGB16I                       = 0x8D89;
    int GR_GL_RGB16UI                      = 0x8D77;
    int GR_GL_RGB32I                       = 0x8D83;
    int GR_GL_RGB32UI                      = 0x8D71;

    /* RGBA sized formats */
    int GR_GL_RGBA4                        = 0x8056;
    int GR_GL_RGB5_A1                      = 0x8057;
    int GR_GL_RGBA8                        = 0x8058;
    int GR_GL_RGB10_A2                     = 0x8059;
    int GR_GL_SRGB8_ALPHA8                 = 0x8C43;
    int GR_GL_RGBA16F                      = 0x881A;
    int GR_GL_RGBA32F                      = 0x8814;
    int GR_GL_RG32F                        = 0x8230;
    int GR_GL_RGBA16                       = 0x805B;

    /* RGBA integer sized formats */
    int GR_GL_RGBA8I                       = 0x8D8E;
    int GR_GL_RGBA8UI                      = 0x8D7C;
    int GR_GL_RGBA16I                      = 0x8D88;
    int GR_GL_RGBA16UI                     = 0x8D76;
    int GR_GL_RGBA32I                      = 0x8D82;
    int GR_GL_RGBA32UI                     = 0x8D70;

    /* BGRA sized formats */
    int GR_GL_BGRA8                        = 0x93A1;
}
