package org.jetbrains.skija.impl;

public class ReferenceUtil {

    /**
     * @see java.lang.ref.Reference#reachabilityFence(Object)
     */
    public static void reachabilityFence(Object ref) {
        java.lang.ref.Reference.reachabilityFence(ref);
    }
}
