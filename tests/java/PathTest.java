package org.jetbrains.skija.test;

import static org.jetbrains.skija.test.runner.TestRunner.assertArrayEquals;
import static org.jetbrains.skija.test.runner.TestRunner.assertEquals;
import static org.jetbrains.skija.test.runner.TestRunner.assertNotEquals;
import static org.jetbrains.skija.test.runner.TestRunner.assertThrows;

import java.util.NoSuchElementException;

import org.jetbrains.skija.Path;
import org.jetbrains.skija.PathDirection;
import org.jetbrains.skija.PathFillMode;
import org.jetbrains.skija.PathSegment;
import org.jetbrains.skija.PathSegmentMask;
import org.jetbrains.skija.PathVerb;
import org.jetbrains.skija.Point;
import org.jetbrains.skija.RRect;
import org.jetbrains.skija.Rect;
import org.jetbrains.skija.test.runner.Executable;
import org.jetbrains.skija.test.runner.TestRunner;

public class PathTest implements Executable {
    @Override
    public void execute() throws Exception {
        TestRunner.testMethod(this, "iter");
        TestRunner.testMethod(this, "convexity");
        TestRunner.testMethod(this, "isShape");
        TestRunner.testMethod(this, "checks");
        TestRunner.testMethod(this, "storage");
        TestRunner.testMethod(this, "swap");
        TestRunner.testMethod(this, "contains");
        TestRunner.testMethod(this, "utils");
        TestRunner.testMethod(this, "serialize");
    }

    public void iter() {
        try (Path p = new Path().moveTo(10, 10).lineTo(20, 0).lineTo(20, 20).closePath();
             var i = p.iterator();) {
            assertEquals(true, i.hasNext());
            PathSegment s = i.next();
            assertEquals(PathVerb.MOVE, s.getVerb());
            assertEquals(new Point(10, 10), s.getP0());
            assertEquals(true, s.isClosedContour());

            assertEquals(true, i.hasNext());
            s = i.next();
            assertEquals(PathVerb.LINE, s.getVerb());
            assertEquals(new Point(10, 10), s.getP0());
            assertEquals(new Point(20, 0), s.getP1());
            assertEquals(false, s.isCloseLine());

            assertEquals(true, i.hasNext());
            s = i.next();
            assertEquals(PathVerb.LINE, s.getVerb());
            assertEquals(new Point(20, 0), s.getP0());
            assertEquals(new Point(20, 20), s.getP1());
            assertEquals(false, s.isCloseLine());

            assertEquals(true, i.hasNext());
            s = i.next();
            assertEquals(PathVerb.LINE, s.getVerb());
            assertEquals(new Point(20, 20), s.getP0());
            assertEquals(new Point(10, 10), s.getP1());
            assertEquals(true, s.isCloseLine());

            assertEquals(true, i.hasNext());
            s = i.next();
            assertEquals(PathVerb.CLOSE, s.getVerb());
            assertEquals(new Point(10, 10), s.getP0());

            assertEquals(false, i.hasNext());
            assertThrows(NoSuchElementException.class, () -> i.next());
        }
    }


    public void convexity() {
        try (Path p = new Path().lineTo(40, 20).lineTo(0, 40).lineTo(0, 0).closePath()) {
            assertEquals(true, p.isConvex());
        }

        try (Path p = new Path().lineTo(40, 40).lineTo(40, 0).lineTo(0, 40).lineTo(0, 0).closePath()) {
            assertEquals(false, p.isConvex());
        }
    }

    public void isShape() {
        for (var dir: PathDirection.values()) {
            for (int start = 0; start < 4; ++start) {
                try (Path p = new Path().addRect(Rect.makeLTRB(0, 0, 40, 20), dir, start)) {
                    assertEquals(Rect.makeLTRB(0, 0, 40, 20), p.isRect());
                    assertEquals(null, p.isOval());
                    assertEquals(null, p.isRRect());
                }
            }
        }

        for (var dir: PathDirection.values()) {
            for (int start = 0; start < 4; ++start) {
                try (Path p = new Path().addOval(Rect.makeLTRB(0, 0, 40, 20), dir, start)) {
                    assertEquals(null, p.isRect());
                    assertEquals(Rect.makeLTRB(0, 0, 40, 20), p.isOval());
                    assertEquals(null, p.isRRect());
                }
            }
        }

        for (var dir: PathDirection.values()) {
            try (Path p = new Path().addCircle(20, 20, 20, dir)) {
                assertEquals(null, p.isRect());
                assertEquals(Rect.makeLTRB(0, 0, 40, 40), p.isOval());
                assertEquals(null, p.isRRect());
            }
        }

        for (var dir: PathDirection.values()) {
            for (int start = 0; start < 8; ++start) {
                try (Path p = new Path().addRRect(RRect.makeLTRB(0, 0, 40, 20, 5), dir, start)) {
                    assertEquals(null, p.isRect());
                    assertEquals(null, p.isOval());
                    assertEquals(RRect.makeLTRB(0, 0, 40, 20, 5), p.isRRect());
                }

                try (Path p = new Path().addRRect(RRect.makeLTRB(0, 0, 40, 20, 0), dir, start)) {
                    assertEquals(Rect.makeLTRB(0, 0, 40, 20), p.isRect());
                    assertEquals(null, p.isOval());
                    assertEquals(null, p.isRRect());
                }

                try (Path p = new Path().addRRect(RRect.makeLTRB(0f, 0f, 40f, 20f, 20f, 10f), dir, start)) {
                    assertEquals(null, p.isRect());
                    assertEquals(Rect.makeLTRB(0, 0, 40, 20), p.isOval());
                    assertEquals(null, p.isRRect());
                }
            }
        }

        try (Path p = new Path().lineTo(40, 40).lineTo(40, 0).lineTo(0, 40).lineTo(0, 0).closePath()) {
            assertEquals(null, p.isRect());
            assertEquals(null, p.isOval());
            assertEquals(null, p.isRRect());
        }
    }

    public void checks() {
        try (Path p = new Path().lineTo(40, 40).lineTo(40, 0).lineTo(0, 40).lineTo(0, 0).closePath()) {
            assertEquals(false, p.isEmpty());
            p.reset();
            assertEquals(true, p.isEmpty());
        }

        try (Path p = new Path().lineTo(40, 40).lineTo(40, 0).lineTo(0, 40).lineTo(0, 0).closePath()) {
            assertEquals(false, p.isEmpty());
            p.rewind();
            assertEquals(true, p.isEmpty());
        }

        try (Path p = new Path().lineTo(40, 40).lineTo(40, 0).lineTo(0, 40).lineTo(0, 0)) {
            assertEquals(false, p.isLastContourClosed());
            p.closePath();
            assertEquals(true, p.isLastContourClosed());
            p.moveTo(100, 100).lineTo(140, 140).lineTo(140, 100).lineTo(100, 140);
            assertEquals(false, p.isLastContourClosed());
            p.closePath();
            assertEquals(true, p.isLastContourClosed());
        }

        try (Path p = new Path().lineTo(40, 40).lineTo(40, 0).lineTo(0, 40).lineTo(0, 0)) {
            assertEquals(true, p.isFinite());
        }

        try (Path p = new Path().lineTo(40, 40).lineTo(Float.POSITIVE_INFINITY, 0).lineTo(0, 40).lineTo(0, 0).closePath()) {
            assertEquals(false, p.isFinite());
        }

        try (Path p = new Path().lineTo(40, 40).lineTo(40, 0).lineTo(0, 40).lineTo(0, 0)) {
            assertEquals(false, p.isVolatile());
            p.setVolatile(true);
            assertEquals(true, p.isVolatile());
            p.setVolatile(false);
            assertEquals(false, p.isVolatile());
        }

        try (Path p = new Path().lineTo(40, 40).lineTo(40, 0).lineTo(0, 40).lineTo(0, 0).closePath()) {
            assertEquals(null, p.getAsLine());
        }

        try (Path p = new Path().moveTo(20, 20).lineTo(40, 40)) {
            assertArrayEquals(new Point[] { new Point(20, 20), new Point(40, 40) }, p.getAsLine());
        }
    }

    public void storage() {
        Path subpath = new Path().lineTo(40, 40).lineTo(40, 0).lineTo(0, 40).lineTo(0, 0).closePath();
        for (Path p: new Path[] {
                       new Path().addPath(subpath),
                       new Path().incReserve(10).addPath(subpath).closePath()
                     })
        {
            TestRunner.pushStack(p.toString());

            Point p0 = new Point(0, 0);
            Point p1 = new Point(40, 40);
            Point p2 = new Point(40, 0);
            Point p3 = new Point(0, 40);
            Point p4 = new Point(0, 0);
            Point p5 = new Point(10, 10);

            assertEquals(5, p.getPointsCount());
            assertEquals(p0, p.getPoint(0));
            assertEquals(p1, p.getPoint(1));
            assertEquals(p2, p.getPoint(2));
            assertEquals(p3, p.getPoint(3));
            assertEquals(p4, p.getPoint(4));
            assertEquals(p4, p.getLastPt());
            p.setLastPt(p5);
            assertEquals(p5, p.getPoint(4));
            assertEquals(p5, p.getLastPt());

            assertEquals(5, p.getPoints(null, 0));
            
            Point[] pts = new Point[5];
            p.getPoints(pts, 5);
            assertArrayEquals(new Point[] { p0, p1, p2, p3, p5 }, pts);

            pts = new Point[3];
            p.getPoints(pts, 3);
            assertArrayEquals(new Point[] { p0, p1, p2 }, pts);

            pts = new Point[5];
            p.getPoints(pts, 3);
            assertArrayEquals(new Point[] { p0, p1, p2, null, null }, pts);

            pts = new Point[10];
            p.getPoints(pts, 10);
            assertArrayEquals(new Point[] { p0, p1, p2, p3, p5, null, null, null, null, null }, pts);

            assertEquals(6, p.getVerbsCount());
            assertEquals(6, p.getVerbs(null, 0));

            PathVerb[] verbs = new PathVerb[6];
            p.getVerbs(verbs, 6);
            assertArrayEquals(new PathVerb[] { PathVerb.MOVE, PathVerb.LINE, PathVerb.LINE, PathVerb.LINE, PathVerb.LINE, PathVerb.CLOSE }, verbs);

            verbs = new PathVerb[3];
            p.getVerbs(verbs, 3);
            assertArrayEquals(new PathVerb[] { PathVerb.MOVE, PathVerb.LINE, PathVerb.LINE }, verbs);

            verbs = new PathVerb[6];
            p.getVerbs(verbs, 3);
            assertArrayEquals(new PathVerb[] { PathVerb.MOVE, PathVerb.LINE, PathVerb.LINE, null, null, null }, verbs);

            verbs = new PathVerb[10];
            p.getVerbs(verbs, 10);
            assertArrayEquals(new PathVerb[] { PathVerb.MOVE, PathVerb.LINE, PathVerb.LINE, PathVerb.LINE, PathVerb.LINE, PathVerb.CLOSE, null, null, null, null }, verbs);

            assertNotEquals(0L, p.getApproximateBytesUsed());

            assertEquals(PathSegmentMask.LINE, p.getSegmentMasks());

            TestRunner.popStack();
        }
    }

    public void swap() {
        try (Path p1 = new Path().lineTo(40, 40).lineTo(40, 0).lineTo(0, 40).lineTo(0, 0).closePath();
             Path p2 = new Path().lineTo(0, 0).lineTo(20, 20);) {
            p1.swap(p2);
            assertEquals(new Path().lineTo(0, 0).lineTo(20, 20), p1);
            assertEquals(new Path().lineTo(40, 40).lineTo(40, 0).lineTo(0, 40).lineTo(0, 0).closePath(), p2);
        }
    }

    public void contains() {
        try (Path p = new Path().addRRect(RRect.makeLTRB(10, 20, 54, 120, 10, 20))) {
            assertEquals(true, p.conservativelyContainsRect(Rect.makeLTRB(10, 40, 54, 80)));
            assertEquals(true, p.conservativelyContainsRect(Rect.makeLTRB(25, 20, 39, 120)));
            assertEquals(true, p.conservativelyContainsRect(Rect.makeLTRB(15, 25, 49, 115)));
            assertEquals(true, p.conservativelyContainsRect(Rect.makeLTRB(13, 27, 51, 113)));

            assertEquals(false, p.conservativelyContainsRect(Rect.makeLTRB(0, 40, 60, 80)));

            assertEquals(true, p.contains(30, 70));
            assertEquals(false, p.contains(0, 0));
        }
    }

    public void utils() {
        assertEquals(false, Path.isLineDegenerate(new Point(0, 0), new Point(10, 0), false));
        assertEquals(true, Path.isLineDegenerate(new Point(0, 0), new Point(0, 0), true));
        assertEquals(true, Path.isLineDegenerate(new Point(0, 0), new Point(0, 0), false));
        assertEquals(false, Path.isLineDegenerate(new Point(0, 0), new Point(0, 1e-13f), true));
        // assertEquals(true, Path.isLineDegenerate(new Point(0, 0), new Point(0, 1e-13f), false));

        assertEquals(false, Path.isQuadDegenerate(new Point(0, 0), new Point(10, 0), new Point(0, 0), false));
        assertEquals(true, Path.isQuadDegenerate(new Point(0, 0), new Point(0, 0), new Point(0, 0), false));

        assertEquals(false, Path.isCubicDegenerate(new Point(0, 0), new Point(10, 0), new Point(0, 0), new Point(0, 0), false));
        assertEquals(true, Path.isCubicDegenerate(new Point(0, 0), new Point(0, 0), new Point(0, 0), new Point(0, 0), false));        

        assertArrayEquals(new Point[] {new Point(0, 20), new Point(6.666667f, 13.333334f)},
            Path.convertConicToQuads(new Point(0, 20), new Point(20, 0), new Point(40, 20), 0.5f, 1));
        assertArrayEquals(new Point[] {new Point(0, 20), new Point(3.0940108f, 16.905989f), new Point(8.452994f, 15.119661f), new Point(13.811978f, 13.333334f)},
            Path.convertConicToQuads(new Point(0, 20), new Point(20, 0), new Point(40, 20), 0.5f, 2));

        try (Path p = new Path().lineTo(40, 40)) {
            var g1 = p.getGenerationId();
            p.lineTo(10, 40);
            var g2 = p.getGenerationId();
            assertNotEquals(g1, g2);
            p.setFillMode(PathFillMode.EVEN_ODD);
            var g3 = p.getGenerationId();
            assertEquals(g2, g3);
        }
    }

    public void serialize() {
        try (Path p = new Path().lineTo(40, 40).lineTo(40, 0).lineTo(0, 40).lineTo(0, 0).closePath();) {
            Path p2 = Path.makeFromBytes(p.serializeToBytes());
            assertEquals(p, p2);
        }
    }
}