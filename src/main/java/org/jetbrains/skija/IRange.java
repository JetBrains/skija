package org.jetbrains.skija;

import java.util.Objects;

public class IRange {
    private final int start;
    private final int end;

    public IRange(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IRange iRange = (IRange) o;
        return start == iRange.start &&
                end == iRange.end;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public String toString() {
        return "IRange[" + start + ".." + end + "]";
    }
}