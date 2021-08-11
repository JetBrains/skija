package org.jetbrains.skija.shaper;

import lombok.*;
import lombok.Data;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;

@Data @With
public class ShapingOptions {
    public static final ShapingOptions DEFAULT = new ShapingOptions(null, null, true, true, true);

    @ApiStatus.Internal @Nullable
    public final FontMgr _fontMgr;

    @ApiStatus.Internal @Nullable
    public final FontFeature[] _features;

    @ApiStatus.Internal
    public final boolean _leftToRight;

    /**
     * If enabled, fallback font runs will not be broken by whitespace from original font
     */
    @ApiStatus.Internal
    public final boolean _approximateSpaces;

    /**
     * If enabled, fallback font runs will not be broken by punctuation from original font
     */
    @ApiStatus.Internal
    public final boolean _approximatePunctuation;

    @NotNull
    public ShapingOptions withFeatures(@Nullable FontFeature[] features) {
        return new ShapingOptions(_fontMgr, features, _leftToRight, _approximateSpaces, _approximatePunctuation);
    }

    @NotNull
    public ShapingOptions withFeatures(@Nullable String featuresString) {
        return featuresString == null ? withFeatures((FontFeature[]) null) : withFeatures(FontFeature.parse(featuresString));
    }
}
