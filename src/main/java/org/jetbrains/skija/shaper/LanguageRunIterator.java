package org.jetbrains.skija.shaper;

public interface LanguageRunIterator extends RunIterator {
    /** Should be BCP-47, c locale names may also work. */
    String getCurrentLanguage();
}