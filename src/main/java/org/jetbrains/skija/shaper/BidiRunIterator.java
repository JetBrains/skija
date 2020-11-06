package org.jetbrains.skija.shaper;

import java.text.*;

public interface BidiRunIterator extends RunIterator {
    /** The unicode bidi embedding level (even ltr, odd rtl) */
    int getCurrentLevel();
}