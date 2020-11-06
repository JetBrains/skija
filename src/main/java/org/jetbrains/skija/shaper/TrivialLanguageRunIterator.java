package org.jetbrains.skija.shaper;

import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;

public class TrivialLanguageRunIterator extends TrivialRunIterator implements LanguageRunIterator {
    @ApiStatus.Internal
    public final String _language;

    public TrivialLanguageRunIterator(int length, String Language) {
        super(length);
        _language = Language;
    }

    @Override
    public String getCurrentLanguage() {
        return _language;
    }
}