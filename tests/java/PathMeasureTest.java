package org.jetbrains.skija.test;

import static org.jetbrains.skija.test.runner.TestRunner.*;

import org.jetbrains.skija.Matrix33;
import org.jetbrains.skija.Path;
import org.jetbrains.skija.PathMeasure;
import org.jetbrains.skija.Point;
import org.jetbrains.skija.test.runner.*;

public class PathMeasureTest implements Executable {
    @Override
    public void execute() throws Exception {
        try (var path = new Path().moveTo(0, 0).lineTo(40, 0).moveTo(0, 40).lineTo(10, 50);
             var measure = new PathMeasure(path, false);
             var path2 = new Path().lineTo(10, 10);)
        {
            assertEquals(40f, measure.getLength());
            assertClose(new Point(0, 0), measure.getPosition(0));
            assertClose(new Point(1, 0), measure.getTangent(0));
            assertClose(new Point(20, 0), measure.getPosition(20));
            assertClose(new Point(1, 0), measure.getTangent(20));
            assertEquals(false, measure.isClosed());

            assertClose(Matrix33.makeTranslate(20, 0), measure.getMatrix(20, true, false));
            assertClose(Matrix33.makeRotate(0), measure.getMatrix(20, false, true));
            assertClose(Matrix33.makeTranslate(20, 0).makeConcat(Matrix33.makeRotate(0)), measure.getMatrix(20, true, true));

            measure.nextContour();    
            assertClose(14.14213f, measure.getLength());
            assertClose(new Point(0, 40), measure.getPosition(0));
            assertClose(new Point(0.70710677f, 0.70710677f), measure.getTangent(0));
            assertClose(new Point(4.949747f, 44.949745f), measure.getPosition(7));
            assertClose(new Point(0.70710677f, 0.70710677f), measure.getTangent(7));

            assertClose(Matrix33.makeTranslate(4.949747f, 44.949745f), measure.getMatrix(7, true, false));
            assertClose(Matrix33.makeRotate(45), measure.getMatrix(7, false, true));
            assertClose(Matrix33.makeTranslate(4.949747f, 44.949745f).makeConcat(Matrix33.makeRotate(45)), measure.getMatrix(7, true, true));

            measure.setPath(path2, false);
            assertClose(14.142136f, measure.getLength());
        }
    }
}