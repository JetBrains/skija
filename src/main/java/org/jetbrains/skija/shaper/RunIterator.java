package org.jetbrains.skija.shaper;

public interface RunIterator {
    void consume();
    int getEndOfCurrentRun();
    boolean isAtEnd();
}