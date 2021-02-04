package org.jetbrains.skija.examples.scenes;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.stream.*;
import lombok.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.svg.*;

public class SVGScene extends Scene {
    public Thread _thread = null;
    public DOM _dom = null;
    public Throwable _error = null;

    @SneakyThrows
    public SVGScene() {
        _variants = Files.lines(java.nio.file.Path.of(file("images/svgs.txt"))).toArray(String[]::new);
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        if (_dom != null) {
            Point size = _dom.getContainerSize();
            if (size.isEmpty()) {
                size = new Point(width - 50, height - 50);
                _dom.setContainerSize(size);
            }
            float scale = Math.min((width - 50) / size.getX(), (height - 50) / size.getY());
            canvas.translate((width - size.getX() * scale) / 2f, (height - size.getY() * scale) / 2f);
            canvas.scale(scale, scale);
            _dom.render(canvas);
        } else if (_error != null) {
            Scene.drawStringCentered(canvas, _error.getMessage(), width / 2, height / 2, Scene.inter13, Scene.blackFill);
        } else if (_thread != null) {
            Scene.drawStringCentered(canvas, "Loading " + _variants[_variantIdx] + "...", width / 2, height / 2, Scene.inter13, Scene.blackFill);
        } else {
            _thread = new Fetch(_variants[_variantIdx]);
            _thread.start();
        }
    }

    class Fetch extends Thread {
        public String _url;

        public Fetch(String url) {
            this._url = url;
        }

        @Override
        public void run() {
            try (var in = new BufferedInputStream(new URL(_url).openStream());
                 var out = new ByteArrayOutputStream();)
            {
                in.transferTo(out);
                if (_thread == this) {
                    try (var data = org.jetbrains.skija.Data.makeFromBytes(out.toByteArray());) {
                        _dom = new DOM(data);
                    }
                }
            } catch (Exception e) {
                if (_thread == this)
                    _error = e;
            } finally {
                if (_thread == this)
                    _thread = null;
            }
        }
    }

    @Override
    public void changeVariant(int delta) {
        super.changeVariant(delta);
        _thread = null;
        _dom = null;
        _error = null;
    }

    @Override
    public String variantTitle() {
        String url = _variants[_variantIdx];
        return url.substring(url.lastIndexOf("/") + 1);
    }
}
