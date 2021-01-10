package org.jetbrains.skija.examples.lwjgl;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.stream.*;
import lombok.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.skottie.*;

public class SkottieScene extends Scene {
    private Animation animation;
    private Logger logger = new Logger() {
        @Override
        public void log(Logger.Level level, String message, String json) {
            System.out.println("Lottie logger: [" + level.name() + "] message " + message + " json: " + json);
        }
    };

    private long animationDurationNs;
    private double animationProgress;
    private long animationStartTime = -1;
    private float animationWidth = 0f;
    private float animationHeight = 0f;

    private Rect backgroundRect;
    private Paint backgroundPaint;

    public SkottieScene() {
        animation = new Animation.Builder()
                .setLogger(logger)
                .makeFromFile(java.nio.file.Path.of("animations/lottie-logo-animation.json").toString());
        animationDurationNs = (long) (animation.getDuration()) * 1_000_000_000;
        Point size = animation.getSize();
        animationWidth = size._x;
        animationHeight = size._y;
        animationProgress = 0;
        backgroundPaint = new Paint().setColor(0xFF000000);
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        if (animationStartTime == -1) {
            animationStartTime = System.nanoTime();
        }
        if (backgroundRect == null) {
            backgroundRect = new Rect(0.f, 0.f, (float) width, (float) height);
        }

        // draw background
        canvas.drawRect(backgroundRect, backgroundPaint);

        advanceAnimation();

        // draw animation
        float left = (float)(width - animationWidth) / 2f;
        float right = left + animationWidth;
        float top = (float)(height - animationHeight) / 2f;
        float bottom = top + animationHeight;
        animation.render(canvas, left, top, right, bottom);
    }

    private void advanceAnimation() {
        long timeSinceAnimationStartNs = System.nanoTime() - animationStartTime;
        long animationProgressNs = timeSinceAnimationStartNs % animationDurationNs;
        animationProgress = animationProgressNs / (double)animationDurationNs;
        if (timeSinceAnimationStartNs > animationDurationNs) {
            animationStartTime += animationDurationNs;  // prevents overflow
        }
        animation.seek((float) animationProgress, null);
    }
}