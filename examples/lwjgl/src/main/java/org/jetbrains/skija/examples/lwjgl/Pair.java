package org.jetbrains.skija.examples.lwjgl;

import lombok.*;

@Data @With
public class Pair<A, B> {
    public final A _first;
    public final B _second;
}