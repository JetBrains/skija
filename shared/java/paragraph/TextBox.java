package org.jetbrains.skija.paragraph;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.skija.Rect;

@AllArgsConstructor
@Data
public class TextBox {
    public final Rect _rect;
    public final Direction _direction;

    public TextBox(float l, float t, float r, float b, int direction) {
        this(Rect.makeLTRB(l, t, r, b), Direction._values[direction]);
    }
}
