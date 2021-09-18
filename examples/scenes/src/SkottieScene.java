package org.jetbrains.skija.examples.scenes;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.nio.file.Path;
import java.util.stream.*;
import lombok.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.resources.*;
import org.jetbrains.skija.sksg.*;
import org.jetbrains.skija.skottie.*;

public class SkottieScene extends Scene {
    private Animation animation;
    private InvalidationController ic = new InvalidationController();
    private String animationVariant;
    private String error;

    private Logger logger = new Logger() {
        @Override
        public void log(LogLevel level, String message, String json) {
            error = "[" + level + "] " + message + " in " + json;
        }
    };

    @SneakyThrows
    public SkottieScene() {
        _variants = Files.list(Path.of(file("animations"))).map(Path::getFileName).map(Path::toString).sorted().toArray(String[]::new);
        _variantIdx = 7;
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        if (animationVariant != _variants[_variantIdx]) {
            if (animation != null)
                animation.close();
            animation = null;
            error = null;

            try {
                var dir = file("animations");
                var resourceProvider = CachingResourceProvider.make(DataURIResourceProviderProxy.make(FileResourceProvider.make(dir, false), false));
                animation = new AnimationBuilder(AnimationBuilderFlag.DEFER_IMAGE_LOADING, AnimationBuilderFlag.PREFER_EMBEDDED_FONTS)
                        .setLogger(logger)
                        .setResourceProvider(resourceProvider)
                        .buildFromFile(dir + "/" + _variants[_variantIdx]);
            } catch (IllegalArgumentException e) {
            }
            animationVariant = _variants[_variantIdx];
        }

        if (animation == null) {
            drawStringCentered(canvas, "animations/" + _variants[_variantIdx] + ": " + error, width / 2, height / 2, inter13, blackFill);
            return;
        }

        float progress = (System.currentTimeMillis() % (long) (1000 * animation.getDuration())) / (1000 * animation.getDuration());
        ic.reset();
        animation.seek(progress, ic);

        var animationWidth = width - 20;
        var animationHeight = animationWidth / animation.getWidth() * animation.getHeight();
        if (animationHeight > height - 20) {
            animationWidth /= animationHeight / (height - 20);
            animationHeight = height - 20;
        }
        var scale = animationWidth / animation.getWidth();

        var bounds = Rect.makeXYWH(
                        (width - animationWidth) / 2f, 
                        (height - animationHeight) / 2f - 2,
                        animationWidth,
                        animationHeight);
        try (var paint = new Paint().setColor(0xFF64C7BE).setMode(PaintMode.STROKE).setStrokeWidth(1)) {
            canvas.drawRect(Rect.makeXYWH(
                              bounds.getLeft() - 0.5f,
                              bounds.getTop() - 0.5f,
                              bounds.getWidth() + 1f,
                              bounds.getHeight() + 1f),
                            paint);

            paint.setMode(PaintMode.FILL);
            canvas.drawRect(Rect.makeXYWH(
                              bounds.getLeft() - 1,
                              bounds.getBottom(),
                              (float) ((bounds.getWidth() + 2) * progress),
                              4), paint);

            animation.render(canvas, bounds, RenderFlag.SKIP_TOP_LEVEL_ISOLATION);

            paint.setColor(0x40CC3333).setMode(PaintMode.STROKE).setStrokeWidth(4);
            Rect dirtyRect = ic.getBounds().scale(scale).offset(bounds.getLeft(), bounds.getTop()).intersect(bounds);
            if (dirtyRect != null)
                canvas.drawRect(dirtyRect, paint);
        }
    }
}