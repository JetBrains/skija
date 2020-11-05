package org.jetbrains.skija.shaper;

public interface RunIterator {
    void consume();
    long getEndOfCurrentRun();
    boolean isAtEnd();
}