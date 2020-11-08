package org.jetbrains.skija.examples.lwjgl;

import lombok.*;

@Data @With
public class Triple<A, B, C> {
    public final A _first;
    public final B _second;
    public final C _third;
}