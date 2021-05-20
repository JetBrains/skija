package org.jetbrains.skija.examples.scenes;

import java.util.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;

public class HUD {
    public long t0 = System.nanoTime();
    public double[] times = new double[155];
    public static List<Pair<String, String>> extras = new ArrayList<>();
    public int timesIdx = 0;

    public void tick() {
        long t1 = System.nanoTime();
        times[timesIdx] = (t1 - t0) / 1000000.0;
        t0 = t1;
        timesIdx = (timesIdx + 1) % times.length;
    }

    public void log() {
        if (timesIdx % 100 == 0)
            System.out.println(String.format("%.0f fps", 1000.0 / Arrays.stream(times).takeWhile(t->t>0).average().getAsDouble()));
    }

    public void draw(Canvas canvas, Scene scene, int width, int height) {
        long nativeCalls = Stats.nativeCalls;
        Stats.nativeCalls = 0;
        int allocated = Stats.allocated.values().stream().reduce(0, Integer::sum);

        Paint bg = new Paint().setColor(0x90000000);
        Paint fg = new Paint().setColor(0xFFFFFFFF);
        Paint graph = new Paint().setColor(0xFF00FF00).setStrokeWidth(1);
        Paint graphPast = new Paint().setColor(0x9000FF00).setStrokeWidth(1);
        Paint graphLimit = new Paint().setColor(0xFFcc3333).setStrokeWidth(1);
        FontMetrics metrics = Scene.inter13.getMetrics();

        float variantsHeight = scene._variants.length > 1 ? 25 : 0;

        // Background
        canvas.translate(width - 230, height - 160 - variantsHeight - extras.size() * 25);
        canvas.drawRRect(RRect.makeLTRB(0, 0, 225, 180 + variantsHeight, 7), bg);
        canvas.translate(5, 5);

        // Scene
        RRect buttonBounds = RRect.makeLTRB(0, 0, 30, 20, 2);
        Rect  labelBounds = Rect.makeLTRB(35, 0, 225, 20);
        canvas.drawRRect(buttonBounds, bg);
        Scene.drawStringCentered(canvas, "←→", 15, 10, Scene.inter13, fg);
        int sceneIdx = 1;
        for (String sceneKey: Scenes.scenes.keySet()) {
            if (sceneKey.equals(Scenes.currentScene)) break;
            sceneIdx++;
        }
        Scene.drawStringLeft(canvas, sceneIdx + "/" + Scenes.scenes.size() + " " + Scenes.currentScene, labelBounds, Scene.inter13, fg);
        canvas.translate(0, 25);

        // Variants
        if (scene._variants.length > 1) {
            labelBounds = Rect.makeLTRB(35, 0, 225, 20);
            canvas.drawRRect(buttonBounds, bg);
            Scene.drawStringCentered(canvas, "↑↓", 15, 10, Scene.inter13, fg);
            Scene.drawStringLeft(canvas, (scene._variantIdx + 1) + "/" + scene._variants.length + " " + scene.variantTitle(), labelBounds, Scene.inter13, fg);
            canvas.translate(0, 25);
        }

        // Extras
        for (var pair: extras) {
            String key = pair.getFirst();
            String label = pair.getSecond();
            buttonBounds = RRect.makeXYWH(5, 0, 20, 20, 2);
            canvas.drawRRect(buttonBounds, bg);
            Scene.drawStringCentered(canvas, key, 15, 10, Scene.inter13, fg);
            Scene.drawStringLeft(canvas, label, labelBounds, Scene.inter13, fg);
            canvas.translate(0, 25);
        }

        // Stats
        canvas.drawRRect(buttonBounds, bg);
        Scene.drawStringCentered(canvas, "S", 15, 10, Scene.inter13, fg);
        Scene.drawStringLeft(canvas, "Stats: " + (Scenes.stats ? "ON" : "OFF"), labelBounds, Scene.inter13, fg);
        canvas.translate(0, 25);

        // GC
        canvas.drawRRect(buttonBounds, bg);
        Scene.drawStringCentered(canvas, "G", 15, 10, Scene.inter13, fg);
        Scene.drawStringLeft(canvas, "GC objects: " + allocated, labelBounds, Scene.inter13, fg);
        canvas.translate(0, 25);

        // Native calls
        Scene.drawStringLeft(canvas, "Native calls: " + nativeCalls, labelBounds, Scene.inter13, fg);
        canvas.translate(0, 25);

        // fps
        canvas.drawRRect(RRect.makeLTRB(0, 0, times.length, 45, 2), bg);
        for (int i = 0; i < times.length; ++i) {
            canvas.drawLine(i, 45, i, 45 - (float) times[i], i > timesIdx ? graphPast : graph);
        }

        for (int refreshRate: new int[] {30, 60, 120}) {
            float frameTime = 1000f / refreshRate;
            canvas.drawLine(0, 45 - frameTime, times.length, 45 - frameTime, graphLimit);
        }

        String time = String.format("%.1fms", Arrays.stream(times).takeWhile(t->t>0).average().getAsDouble());
        Scene.drawStringLeft(canvas, time, Rect.makeLTRB(times.length + 5, 0, 225, 20), Scene.inter13, fg);
        String fps = String.format("%.0f fps", 1000.0 / Arrays.stream(times).takeWhile(t->t>0).average().getAsDouble());
        Scene.drawStringLeft(canvas, fps, Rect.makeLTRB(times.length + 5, 25, 225, 40), Scene.inter13, fg);
        canvas.translate(0, 25);

        bg.close();
        fg.close();
        graph.close();
        graphPast.close();
        graphLimit.close();
    }
}
