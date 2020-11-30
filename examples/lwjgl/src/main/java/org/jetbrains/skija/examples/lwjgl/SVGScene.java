package org.jetbrains.skija.examples.lwjgl;

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
        _variants = Files.lines(java.nio.file.Path.of("images/svgs.txt")).toArray(String[]::new);
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        if (_dom != null) {
            Point size = _dom.getContainerSize();
            if (size.isEmpty())
                _dom.setContainerSize(Math.min(width, height), Math.min(width, height));
            else if (size.getX() > width || size.getY() > height || (size.getX() < width - 50 && size.getY() < height - 50)) {
                float ratio = size.getX() / size.getY();
                float w = width - 40;
                float h = w / ratio;
                if (h > height - 40) {
                    w = w / h * (height - 40);
                    h = height - 40;
                }

                _dom.setContainerSize(w, h);
            }

            size = _dom.getContainerSize();
            canvas.translate((width - size.getX()) / 2, (height - size.getY()) / 2);
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
