package org.jetbrains.skija.examples.jwm;

import java.util.*;
import java.util.function.*;
import lombok.*;
import org.jetbrains.annotations.*;
import org.jetbrains.jwm.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.examples.scenes.*;
import org.jetbrains.skija.impl.*;

public class Main implements Consumer<Event> {
    public Window _window;
    public SkijaLayer _layer;
    public int _xpos = 720, _ypos = 405, _width = 0, _height = 0;
    public float _scale = 1;

    public String[] _layers = new String[] {
        "Metal",
        "OpenGL",
    };
    public int _layerIdx = 0;

    public Main() {
        Stats.enabled = true;
        App.makeWindow((window) -> {
            _window = window;
            _window.setEventListener(this);
            changeLayer();
            var scale = _window.getScale();
            _window.resize((int) (1440 * scale), (int) (810 * scale));
            _window.move((int) (240 * scale), (int) (135 * scale));
            _window.show();
            accept(EventReconfigure.INSTANCE);
            _window.requestFrame();
        });
    }

    public void paint() {
        if (_layer == null)
            return;

        var canvas = _layer.beforePaint();
        Scenes.draw(canvas, _width, _height, _scale, Math.max(0, Math.min(_xpos, _width)), Math.max(0, Math.min(_ypos, _height)));
        _layer.afterPaint();
    }

    public void changeLayer() {
        if (_layer != null)
            _layer.close();

        if (HUD.extras.size() < 1)
            HUD.extras.add(null);

        if ("Metal".equals(_layers[_layerIdx])) {
            _layer = new SkijaLayerMetal();
            HUD.extras.set(0, new Pair("L", "Layer: Metal"));
        } else if ("OpenGL".equals(_layers[_layerIdx])) {
            _layer = new SkijaLayerGL();
            HUD.extras.set(0, new Pair("L", "Layer: OpenGL"));
        }

        _layer.attach(_window);
        _layer.reconfigure();
        _layer.resize(_window.getWidth(), _window.getHeight());
    }

    @Override
    public void accept(Event e) {
        if (e instanceof EventReconfigure) {
            _layer.reconfigure();
            _scale = _window.getScale();
            accept(new EventResize(_window.getWidth(), _window.getHeight()));
        } else if (e instanceof EventResize) {
            EventResize er = (EventResize) e;
            _width = (int) (er.getWidth() / _scale);
            _height = (int) (er.getHeight() / _scale);
            _layer.resize(er.getWidth(), er.getHeight());
            paint();
        } else if (e instanceof EventMouseMove) {
            _xpos = (int) (((EventMouseMove) e).getX() / _window.getScale());
            _ypos = (int) (((EventMouseMove) e).getY() / _window.getScale());
        } else if (e instanceof EventKeyboard eventKeyboard) {
            if (eventKeyboard.isPressed() == true) {
                switch (eventKeyboard.getKey()) {
                    case S -> {
                        Scenes.stats = !Scenes.stats;
                        Stats.enabled = Scenes.stats;
                    }
                    case G -> {
                        System.out.println("Before GC " + Stats.allocated);
                        System.gc();
                    }
                    case L -> {
                        _layerIdx = (_layerIdx + _layers.length - 1) % _layers.length;
                        changeLayer();
                    }
                    case LEFT ->
                        Scenes.prevScene();

                    case RIGHT ->
                        Scenes.nextScene();

                    case DOWN ->
                        Scenes.currentScene().changeVariant(1);

                    case UP ->
                        Scenes.currentScene().changeVariant(-1);
                    default ->
                        System.out.println("Key pressed: " + eventKeyboard.getKey());
                }
            }
        } else if (e instanceof EventFrame) {
            paint();
            _window.requestFrame();
        } else if (e instanceof EventClose) {
            _layer.close();
            _window.close();
            App.terminate();
        }
    }

    public static void main(String[] args) {
        App.init();
        new Main();
        App.start();
    }
}
