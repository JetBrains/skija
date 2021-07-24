# Getting started

## Preparation

Add ONE of these dependencies to your ant/maven/gradle/bazel:

```
org.jetbrains.skija:skija-windows:${version}
org.jetbrains.skija:skija-linux:${version}
org.jetbrains.skija:skija-macos-x64:${version}
org.jetbrains.skija:skija-macos-arm64:${version}
```

(Replace `${version}` with ![version](https://img.shields.io/badge/dynamic/xml?style=flat-square&label=latest&color=success&url=https%3A%2F%2Fpackages.jetbrains.team%2Fmaven%2Fp%2Fskija%2Fmaven%2Forg%2Fjetbrains%2Fskija%2Fskija-macos-x64%2Fmaven-metadata.xml&query=//release))

The repository url is https://packages.jetbrains.team/maven/p/skija/maven

In your main class, import

```java
import org.jetbrains.skija.*;
import java.io.IOException;
```

## Rendering the first image

Most of the Skija value hides in [Canvas](/shared/src/main/java/org/jetbrains/skija/Canvas.java) class. Canvas let you actually draw.

Simplest way to obtain canvas is to create an in-memory, bitmap-backed one:

```java
Surface surface = Surface.makeRasterN32Premul(100, 100);
Canvas canvas = surface.getCanvas();
```

To draw something, you often need a [Paint](/shared/src/main/java/org/jetbrains/skija/Paint.java). It stores fill color, stroke settings and various effects. We’ll use a default Paint with red fill color:

```java
Paint paint = new Paint();
paint.setColor(0xFFFF0000);
```

The color is a 32-bit integer in ARGB format. `0xFF______` is fully opaque, `0x00______` is fully transparent. The colors are:

```java
Red   = 0xFFFF0000
Green = 0xFF00FF00
Blue  = 0xFF0000FF
```

Finally, we can draw something:

```java
canvas.drawCircle(50, 50, 30, paint);
```

Now the image only exists as a vector of bytes in memory. To extract it, we can ask Skija get us bitmap, encode it to PNG image format, and finally give us the bytes to write to file. Here:

```java
Image image = surface.makeImageSnapshot();
Data pngData = image.encodeToData(EncodedImageFormat.PNG);
ByteBuffer pngBytes = pngData.toByteBuffer();

try
{
    Path path = Path.of("output.png");
    ByteChannel channel = Files.newByteChannel(
        path,
        StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
    channel.write(pngBytes.toByteBuffer());
    channel.close();
}
catch (IOException e)
{
    System.out.println(e);
}
```

Voilà! You can now open `output.png` from the working directory to check if it actually contains what you just drew.

## API conventions

Skija APIs are mostly mutable.

Most of the setters support chaining:

```java
var paint = new Paint().setColor(0xFF1D7AA2).setMode(PaintMode.STROKE).setStrokeWidth(1f);
```

## Resource management

Most of Skija classes (those extending [RefCnt](/shared/src/main/java/org/jetbrains/skija/impl/RefCnt.java) or [Managed](/shared/src/main/java/org/jetbrains/skija/impl/Managed.java)) are backed by native pointers to C++ world of Skia. Don’t worry — Skija is smart enough to automatically manage everything for you. When java objects are collected, corresponding C++ structures are guaranteed to be freed as well. Programming in Java still feels like Java — safe by default.

```java
void drawCircle(Canvas c) {
    Paint p = new Paint();
    c.drawCircle(0, 0, 10, p);
}

// Totally OK, `p` will be freed at the next GC
```

Only as an additional bonus, all Managed descendants implement AutoClosable. If you want (and only if you want!), you can free short-lived objects immediately after use. This is not mandatory, but might help freeing more memory quicker.

```java
void drawCircle(Canvas c) {
    try (Paint p = new Paint()) {
        c.drawCircle(0, 0, 10, p);
    } // `p` will be freed right here, cleaning up C++ resources. Can’t be used after!
}
```

## Note on getters

You might notice that all fields in Skija are declared public. For example, [Color4f](/shared/src/main/java/org/jetbrains/skija/Color4f.java):

```java
@AllArgsConstructor
@Data
public class Color4f {
    public final float _r;
    public final float _g;
    public final float _b;
    public final float _a;

    public Color4f(float r, float g, float b) {
        this(r, g, b, 1f);
    }

    public Color4f(float[] rgba) {
        this(rgba[0], rgba[1], rgba[2], rgba[3]);
    }
}
```

This was done to work around Java’s very limited visibility control, and to give an escape hatch for very complex and rare needs, if those arise.

Please treat everything named with leading `_` as private and use getters instead:

```java
var color = new Color4f(1, 1, 1);

color._r; // bad: encapsulation breach. Don’t do this

color.getR(); // good: proper public API use
```

## Interactive apps

Skia can render to many backends. Skija currently support Bitmaps and OpenGL (Metal and Vulkan demos are coming).

To render to OpenGL, you need to create an OpenGL window. This part is not covered by Skija, but there are many ways and many examples online.

Using LWJGL library (also see [examples/lwjgl](/examples/lwjgl)):

```java
var width = 640;
var height = 480;

// Create window
glfwInit();
glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
long windowHandle = glfwCreateWindow(width, height, "Skija LWJGL Demo", NULL, NULL);
glfwMakeContextCurrent(windowHandle);
glfwSwapInterval(1); // Enable v-sync
glfwShowWindow(windowHandle);

// Initialize OpenGL
// Do once per app launch
GL.createCapabilities();

// Create Skia OpenGL context
// Do once per app launch
DirectContext context = DirectContext.makeGL();

// Create render target, surface and retrieve canvas from it
// .close() and recreate on window resize
int fbId = GL11.glGetInteger(0x8CA6); // GL_FRAMEBUFFER_BINDING
BackendRenderTarget renderTarget = BackendRenderTarget.makeGL(
    width,
    height,
    /*samples*/ 0,
    /*stencil*/ 8,
    fbId,
    FramebufferFormat.GR_GL_RGBA8);

// .close() and recreate on window resize
Surface surface = Surface.makeFromBackendRenderTarget(
    context,
    renderTarget,
    SurfaceOrigin.BOTTOM_LEFT,
    SurfaceColorFormat.RGBA_8888,
    ColorSpace.getSRGB());

// do not .close() — Surface manages its lifetime here
Canvas canvas = surface.getCanvas();

// Render loop
while (!glfwWindowShouldClose(windowHandle)) {

    // DRAW HERE!!!

    context.flush();
    glfwSwapBuffers(windowHandle); // wait for v-sync
    glfwPollEvents();
}
```

Using JOGL library: see [examples/jogl](/examples/jogl).

Embedding into AWT window: see [Skiko](https://github.com/jetbrains/skiko).

## Drawing text

For drawing text, there are two important concepts: Typeface and Font. 

[Typeface](/shared/src/main/java/org/jetbrains/skija/Typeface.java) corresponds to a font file and is relatively expensive to create. You can create typeface directly:

```java
Typeface face = Typeface.makeFromFile("Inter.ttf");
```

or ask operating system to locate one for you:

```java
Typeface face = FontMgr.getDefault().matchFamilyStyle("Menlo", FontStyle.NORMAL);
```

The [Font](/shared/src/main/java/org/jetbrains/skija/Font.java) contains specific settings the Typeface should be drawn with. The most important one is size:

```java
Font font = new Font(face, 13);
```

Finally, to specify color, we’ll need paint. All together:

```java
try (Typeface face = FontMgr.getDefault().matchFamilyStyle("Menlo", FontStyle.NORMAL);
     Font font  = new Font(face, 13);
     Paint fill = new Paint().setColor(0xFF000000);)
{
    canvas.drawString("Hello, world", 0, 0, font, fill);
}
```

For this example, we close all resources immediately after paint. In a real application, you would want to cache both Typeface and Font, as it is expensive to recreate them on every frame.

For advanced font rendering, see [Shaper](/shared/src/main/java/org/jetbrains/skija/shaper/Shaper.java) and [ParagraphBuilder](/shared/src/main/java/org/jetbrains/skija/paragraph/ParagraphBuilder.java).

## Discovering Skia API

I recommend studying these classes first:

- [Canvas](/shared/src/main/java/org/jetbrains/skija/Canvas.java)
- [Paint](/shared/src/main/java/org/jetbrains/skija/Paint.java)
- [Path](/shared/src/main/java/org/jetbrains/skija/Path.java)
- [Image](/shared/src/main/java/org/jetbrains/skija/Image.java)
- [Shader](/shared/src/main/java/org/jetbrains/skija/Shader.java)
- [Typeface](/shared/src/main/java/org/jetbrains/skija/Typeface.java)
- [Font](/shared/src/main/java/org/jetbrains/skija/Font.java)
- [ParagraphBuilder](/shared/src/main/java/org/jetbrains/skija/paragraph/ParagraphBuilder.java)

I found [SkiaSharp documentation](https://docs.microsoft.com/en-us/xamarin/xamarin-forms/user-interface/graphics/skiasharp/) to be excellent resource on what can be done in Skia. They have nice examples and visual explanations, too.

If Skija is missing a documentation for a particular method or class, check the same class in [Skia Documentation](https://api.skia.org/) instead. It might be that we didn’t move it over to Java yet. PRs are welcome!

Finally, [LWJGL demo app](/examples/lwjgl) has examples of most of the APIs that are currently implemented.

## Good luck!

Skia is a fantastic library. JVM is a fantastic platform. Java is a fantastic language. Together, amazing things become possible.

We are very exctited to share Skija with you. And if you build something cool, please, let us know — we would be very happy to know what are you building.
