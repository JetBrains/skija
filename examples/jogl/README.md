# Using Skija with Kotlin and JOGL

## Prerequisites

Use at least JDK11 or later (up to JDK14) it's known to work. In the `lib` folder this demo
ships prebuilt binaries of JOGL 2.4.0-rc-20200307 from https://jogamp.org/deployment/archive/rc/v2.4.0-rc-20200307/jar/,
as most recent (at the moment of writing) stable version 2.3.1 is known to have problems with macosOS 10.14 and later and JDK > version 8.

## Building

Use the regular

    ./gradlew build
    ./gradlew run

 sequence, and assuming the Skija project has been built it shall compile and run example
 showing how to use Skija in JOGL context from Kotlin.

 You could also use `SkiaWindow` class from your application code ass simple as

 ```$kotlin
    var radius = 50f
    val frame = SkiaWindow(width = 1024, height = 768, fps = 120) { canvas, w, h ->
        val fill = Paint().setColor(0xFF000000).setStyle(Paint.Style.STROKE).setStrokeWidth(4f)
        canvas.drawOval(Rect.makeXYWH( w / 2f - radius, h / 2f - radius, radius, radius), fill)
    }
    frame.glCanvas.addMouseMotionListener(object: MouseMotionAdapter() {
        var lastX = 0
        override fun mouseDragged(e: MouseEvent) {
            radius += if (e.x > lastX) -2f else 2f
            lastX = e.x
        }
    })
    frame.setLocation(400, 400)
    frame.setVisible(true)
```

