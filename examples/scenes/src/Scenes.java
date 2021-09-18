package org.jetbrains.skija.examples.scenes;

import java.util.*;
import lombok.*;
import org.jetbrains.skija.*;

public class Scenes {
    public static TreeMap<String, Scene> scenes;
    public static String currentScene = "Skottie";
    public static HUD hud = new HUD();
    public static boolean stats = true;

    static {
        scenes = new TreeMap<>(Comparator.comparing(s -> s.toLowerCase()));
        scenes.put("Bitmap", null);
        scenes.put("Bitmap Image", null);
        scenes.put("Blends", null);
        scenes.put("Break Iterator", null);
        scenes.put("Codec", null);
        scenes.put("Color Filters", null);
        scenes.put("Decorations Bench", null);
        scenes.put("Drawable", null);
        scenes.put("Empty", null);
        scenes.put("Figma", null);
        scenes.put("Font", null);
        scenes.put("Font Rendering", null);
        scenes.put("Font Size", null);
        scenes.put("Font Variations", null);
        scenes.put("Geometry", null);
        scenes.put("Images", null);
        scenes.put("Image Bench", null);
        scenes.put("Image Codecs", null);
        scenes.put("Image Filters", null);
        scenes.put("Mask Filters", null);
        scenes.put("Matrix", null);
        scenes.put("Paragraph", null);
        scenes.put("Paragraph Metrics", null);
        scenes.put("Paragraph Style", null);
        scenes.put("Path Effects", null);
        scenes.put("Paths", null);
        scenes.put("Picture Recorder", null);
        scenes.put("Pixel Grid", null);
        scenes.put("Pythagoras", null);
        scenes.put("Run Handler", null);
        scenes.put("Run Iterator", null);
        scenes.put("Runtime Effect", null);
        scenes.put("SVG", null);
        scenes.put("SVG Scaling", null);
        scenes.put("Shaders", null);
        scenes.put("Shadows", null);
        scenes.put("Shadow Utils", null);
        scenes.put("Shapers", null);
        scenes.put("Skottie", null);
        scenes.put("Squares", null);
        scenes.put("Swing", null);
        scenes.put("Text Shape Bench", null);
        scenes.put("Text Style", null);
        scenes.put("Text Blob", null);
        scenes.put("Text Line", null);
        scenes.put("Text Line Decorations", null);
        scenes.put("Wall Of Text", null);
        scenes.put("Watches", null);
        setScene(currentScene);
    }

    @SneakyThrows
    public static Scene newScene(String name) {
        String className = "org.jetbrains.skija.examples.scenes." + name.replaceAll(" ", "") + "Scene";
        Class<Scene> cls = (Class<Scene>) Scenes.class.forName(className);
        return cls.newInstance();
    }

    public static Scene nextScene() {
        return setScene(Optional.ofNullable(scenes.higherKey(currentScene)).orElse(scenes.firstKey()));
    }

    public static Scene prevScene() {
        return setScene(Optional.ofNullable(scenes.lowerKey(currentScene)).orElse(scenes.lastKey()));
    }

    public static Scene setScene(String scene) {
        currentScene = scene;
        if (!scenes.containsKey(scene))
            throw new IllegalArgumentException("Unknown scene: " + scene);
        if (scenes.get(scene) == null)
            scenes.put(scene, newScene(scene));
        return scenes.get(scene);
    }

    public static Scene currentScene() {
        return scenes.get(currentScene);
    }

    public static void draw(Canvas canvas, int width, int height, float scale, int mouseX, int mouseY) {
        canvas.clear(0xFFFFFFFF);
        int layer = canvas.save();
        var scene = currentScene();
        if (scene.scale())
            canvas.scale(scale, scale);
        scene.draw(canvas, width, height, scale, mouseX, mouseY);
        canvas.restoreToCount(layer);

        hud.tick();
        if (stats) {
            layer = canvas.save();
            canvas.scale(scale, scale);
            hud.draw(canvas, scene, width, height);
            canvas.restoreToCount(layer);
        } else
            hud.log();
    }
}
