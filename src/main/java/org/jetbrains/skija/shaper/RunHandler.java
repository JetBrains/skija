package org.jetbrains.skija.shaper;

import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;

public interface RunHandler {
    /**
     * Called when beginning a line.
     */
    void beginLine();

    /**
     * Called once for each run in a line. Can compute baselines and offsets.
     */
    void runInfo(RunInfo info);

    /**
     * Called after all {@link #runInfo} calls for a line.
     */
    void commitRunInfo();

    /**
     * Called for each run in a line after {@link #commitRunInfo}.
     * 
     * @return  an offset to add to every position
     */
    Point runOffset(RunInfo info);

    /**
     * Called for each run in a line after {@link #runBuffer}.
     *
     * @param positions  put glyphs[i] at positions[i]
     * @param clusters   utf8+clusters[i] starts run which produced glyphs[i]
     */
    void commitRun(RunInfo info, short[] glyphs, Point[] positions, int[] clusters);

    /**
     * Called when ending a line.
     */
    void commitLine();
}