package org.jetbrains.skija;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor
@Getter
public class PathSegment {
    public final PathVerb _verb;
    public final Point   _p0;
    public final Point   _p1;
    public final Point   _p2;
    public final Point   _p3;
    public final float   _conicWeight;
    public final boolean _closeLine;
    public final boolean _closedContour;

    public PathSegment() {
        this(PathVerb.DONE, null, null, null, null, 0f, false, false);
    }

    public PathSegment(int verbOrdinal, float x0, float y0, boolean isClosedContour) {
        this(PathVerb._values[verbOrdinal], new Point(x0, y0), null, null, null, 0f, false, isClosedContour);
        assert verbOrdinal == PathVerb.MOVE.ordinal() || verbOrdinal == PathVerb.CLOSE.ordinal() : "Expected MOVE or CLOSE, got " + PathVerb._values[verbOrdinal];
    }

    public PathSegment(float x0, float y0, float x1, float y1, boolean isCloseLine, boolean isClosedContour) {
        this(PathVerb.LINE, new Point(x0, y0), new Point(x1, y1), null, null, 0f, isCloseLine, isClosedContour);
    }

    public PathSegment(float x0, float y0, float x1, float y1, float x2, float y2, boolean isClosedContour) {
        this(PathVerb.QUAD, new Point(x0, y0), new Point(x1, y1), new Point(x2, y2), null, 0f, false, isClosedContour);
    }

    public PathSegment(float x0, float y0, float x1, float y1, float x2, float y2, float conicWeight, boolean isClosedContour) {
        this(PathVerb.CONIC, new Point(x0, y0), new Point(x1, y1), new Point(x2, y2), null, conicWeight, false, isClosedContour);
    }

    public PathSegment(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3, boolean isClosedContour) {
        this(PathVerb.CUBIC, new Point(x0, y0), new Point(x1, y1), new Point(x2, y2), new Point(x3, y3), 0f, false, isClosedContour);
    }

    @Override
    public String toString() {
        return "Segment(" +
                "verb=" + _verb +
                (_verb != PathVerb.DONE ? ", p0=" + _p0 : "") +
                (_verb == PathVerb.LINE || _verb == PathVerb.QUAD || _verb == PathVerb.CONIC || _verb == PathVerb.CUBIC ? ", p1=" + _p1 : "") +
                (_verb == PathVerb.QUAD || _verb == PathVerb.CONIC || _verb == PathVerb.CUBIC ? ", p2=" + _p2 : "") +
                (_verb == PathVerb.CUBIC ? ", p3=" + _p3 : "") +
                (_verb == PathVerb.CONIC ? ", conicWeight=" + _conicWeight : "") +
                (_verb == PathVerb.LINE  ? ", closeLine=" + _closeLine : "") +
                (_verb != PathVerb.DONE ? ", closedContour=" + _closedContour : "") +
                ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PathSegment segment = (PathSegment) o;
        return _verb == segment._verb &&
               (_verb != PathVerb.DONE ? Objects.equals(_p0, segment._p0) : true) &&
               (_verb == PathVerb.LINE || _verb == PathVerb.QUAD || _verb == PathVerb.CONIC || _verb == PathVerb.CUBIC ? Objects.equals(_p1, segment._p1) : true) &&
               (_verb == PathVerb.QUAD || _verb == PathVerb.CONIC || _verb == PathVerb.CUBIC ? Objects.equals(_p2, segment._p2) : true) &&
               (_verb == PathVerb.CUBIC ? Objects.equals(_p3, segment._p3) : true) &&
               (_verb == PathVerb.CONIC ? Float.compare(segment._conicWeight, _conicWeight) == 0 : true) &&
               (_verb == PathVerb.LINE  ? _closeLine == segment._closeLine : true) &&
               (_verb != PathVerb.DONE ? _closedContour == segment._closedContour : true);
    }

    @Override
    public int hashCode() {
        switch (_verb) {
            case DONE:
                return Objects.hash(_verb);
            case MOVE:
                return Objects.hash(_verb, _p0, _closedContour);
            case LINE:
                return Objects.hash(_verb, _p0, _p1, _closeLine, _closedContour);
            case QUAD:
                return Objects.hash(_verb, _p0, _p1, _p2, _closedContour);
            case CONIC:
                return Objects.hash(_verb, _p0, _p1, _p2, _conicWeight, _closedContour);
            case CUBIC:
                return Objects.hash(_verb, _p0, _p1, _p2, _p3, _closedContour);
            default:
                throw new RuntimeException("Unreachable");
        }
    }
}
