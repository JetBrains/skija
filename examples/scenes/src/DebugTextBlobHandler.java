package org.jetbrains.skija.examples.scenes;

import java.util.*;
import java.util.stream.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.shaper.*;

public class DebugTextBlobHandler implements RunHandler, AutoCloseable {
    public final TextBlobBuilder _builder;
    public float _maxRunAscent = 0;
    public float _maxRunDescent = 0;
    public float _maxRunLeading = 0;
    public float _xPos = 0;
    public float _yPos = 0;
    public List<DebugTextRun> _runs = null;

    public DebugTextBlobHandler() {
        _builder = new TextBlobBuilder();
    }

    public DebugTextBlobHandler withRuns() {
        _runs = new ArrayList<>();
        return this;
    }

    @Override
    public void close() {
        if (_runs != null)
            for (var info: _runs)
                info._font.close();

        _builder.close();
    }

    @Override
    public void beginLine() {
        _xPos = 0;
        _maxRunAscent = 0;
        _maxRunDescent = 0;
        _maxRunLeading = 0;
    }

    @Override
    public void runInfo(RunInfo info) {
        var font       = new Font(info._fontPtr, false);
        var metrics    = font.getMetrics();
        _maxRunAscent  = Math.min(_maxRunAscent,  metrics.getAscent());
        _maxRunDescent = Math.max(_maxRunDescent, metrics.getDescent());
        _maxRunLeading = Math.max(_maxRunLeading, metrics.getLeading());
    }

    @Override
    public void commitRunInfo() {
        _yPos += -_maxRunAscent;
    }

    @Override
    public Point runOffset(RunInfo info) {
        return new Point(_xPos, _yPos);
    }

    @Override
    public void commitRun(RunInfo info, short[] glyphs, Point[] positions, int[] clusters) {
        // System.out.println("advance=" + info._advanceX
        //                    + " glyphCount=" + info._glyphCount
        //                    + " utf8Range=" + info._rangeBegin + ".." + info.getRangeEnd() 
        //                    + " positions=" + Arrays.stream(positions).map(Point::getX).collect(Collectors.toList()));
        var font = new Font(info._fontPtr, false);
        _builder.appendRunPos(font, glyphs, positions);
        if (_runs != null)
            _runs.add(new DebugTextRun(
                info,
                info.getFont(), 
                Rect.makeXYWH(_xPos, _yPos - (-_maxRunAscent), info.getAdvance().getX(), (-_maxRunAscent) + _maxRunDescent),
                glyphs,
                positions,
                clusters));
        _xPos += info.getAdvance().getX();
    }

    @Override
    public void commitLine() {
        _yPos += _maxRunDescent + _maxRunLeading;
    }

    public TextBlob makeBlob() {
        return _builder.build();
    }
}