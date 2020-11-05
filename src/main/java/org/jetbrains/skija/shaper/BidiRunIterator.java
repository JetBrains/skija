package org.jetbrains.skija.shaper;

import java.text.*;

interface BidiRunIterator extends RunIterator {
    /** The unicode bidi embedding level (even ltr, odd rtl) */
    int getCurrentLevel();
}