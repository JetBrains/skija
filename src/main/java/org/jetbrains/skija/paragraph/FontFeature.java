package org.jetbrains.skija.paragraph;

import lombok.Data;
import lombok.With;

// TODO unify with FontFeature?
@Data @With
public class FontFeature {
    public final String _name;
    public final int    _value;
}