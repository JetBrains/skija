package org.jetbrains.skija.examples.scenes;

import lombok.*;

@Data @With
public class Pair<A, B> {
    public final A _first;
    public final B _second;

    public static <A, B> Pair<A, B>[] arrayOf(A a1, B b1) {
        return new Pair[] { new Pair(a1, b1) };
    }

    public static <A, B> Pair<A, B>[] arrayOf(A a1, B b1, A a2, B b2) {
        return new Pair[] { new Pair(a1, b1), new Pair(a2, b2) };
    }

    public static <A, B> Pair<A, B>[] arrayOf(A a1, B b1, A a2, B b2, A a3, B b3) {
        return new Pair[] { new Pair(a1, b1), new Pair(a2, b2), new Pair(a3, b3) };
    }

    public static <A, B> Pair<A, B>[] arrayOf(A a1, B b1, A a2, B b2, A a3, B b3, A a4, B b4) {
        return new Pair[] { new Pair(a1, b1), new Pair(a2, b2), new Pair(a3, b3), new Pair(a4, b4) };
    }
    
    public static <A, B> Pair<A, B>[] arrayOf(A a1, B b1, A a2, B b2, A a3, B b3, A a4, B b4, A a5, B b5) {
        return new Pair[] { new Pair(a1, b1), new Pair(a2, b2), new Pair(a3, b3), new Pair(a4, b4), new Pair(a5, b5) };
    }
}