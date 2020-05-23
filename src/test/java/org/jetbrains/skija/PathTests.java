package org.jetbrains.skija;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PathTests {
    @BeforeAll
    public static void loadSkija() {
        JNI.loadLibrary("/", "skija");
    }

    @Test
    public void iter() {
        try (Path p = new Path().moveTo(10, 10).lineTo(20, 0).lineTo(20, 20).closePath();
             var i = p.iterator();) {
            assertEquals(true, i.hasNext());
            Path.Segment s = i.next();
            assertEquals(Path.Verb.MOVE, s.verb);
            assertEquals(new Point(10, 10), s.p0);
            assertEquals(true, s.isClosedContour);

            assertEquals(true, i.hasNext());
            s = i.next();
            assertEquals(Path.Verb.LINE, s.verb);
            assertEquals(new Point(10, 10), s.p0);
            assertEquals(new Point(20, 0), s.p1);
            assertEquals(false, s.isCloseLine);

            assertEquals(true, i.hasNext());
            s = i.next();
            assertEquals(Path.Verb.LINE, s.verb);
            assertEquals(new Point(20, 0), s.p0);
            assertEquals(new Point(20, 20), s.p1);
            assertEquals(false, s.isCloseLine);

            assertEquals(true, i.hasNext());
            s = i.next();
            assertEquals(Path.Verb.LINE, s.verb);
            assertEquals(new Point(20, 20), s.p0);
            assertEquals(new Point(10, 10), s.p1);
            assertEquals(true, s.isCloseLine);

            assertEquals(true, i.hasNext());
            s = i.next();
            assertEquals(Path.Verb.CLOSE, s.verb);
            assertEquals(new Point(10, 10), s.p0);

            assertEquals(false, i.hasNext());
            assertThrows(NoSuchElementException.class, () -> i.next());
        }
    }


    @Test
    public void convex() {
        try (Path p = new Path().lineTo(40, 20).lineTo(0, 40).lineTo(0, 0).closePath()) {
            assertEquals(Path.ConvexityType.UNKNOWN, p.getConvexityTypeOrUnknown());
            assertEquals(Path.ConvexityType.CONVEX, p.getConvexityType());
            assertEquals(Path.ConvexityType.CONVEX, p.getConvexityTypeOrUnknown());
            assertEquals(true, p.isConvex());
            p.setConvexityType(Path.ConvexityType.CONCAVE);
            assertEquals(Path.ConvexityType.CONCAVE, p.getConvexityTypeOrUnknown());
            assertEquals(Path.ConvexityType.CONCAVE, p.getConvexityType());
            assertEquals(false, p.isConvex());
        }

        try (Path p = new Path().lineTo(40, 40).lineTo(40, 0).lineTo(0, 40).lineTo(0, 0).closePath()) {
            assertEquals(Path.ConvexityType.UNKNOWN, p.getConvexityTypeOrUnknown());
            assertEquals(Path.ConvexityType.CONCAVE, p.getConvexityType());
            assertEquals(Path.ConvexityType.CONCAVE, p.getConvexityTypeOrUnknown());
            assertEquals(false, p.isConvex());
        }
    }

    @Test
    public void isShape() {
        for (var dir: Path.Direction.values()) {
            for (int start = 0; start < 4; ++start) {
                try (Path p = new Path().addRect(Rect.makeLTRB(0, 0, 40, 20), dir, start)) {
                    assertEquals(Rect.makeLTRB(0, 0, 40, 20), p.isRect());
                    assertEquals(null, p.isOval());
                    assertEquals(null, p.isRoundedRect());
                }
            }
        }

        for (var dir: Path.Direction.values()) {
            for (int start = 0; start < 4; ++start) {
                try (Path p = new Path().addOval(Rect.makeLTRB(0, 0, 40, 20), dir, start)) {
                    assertEquals(null, p.isRect());
                    assertEquals(Rect.makeLTRB(0, 0, 40, 20), p.isOval());
                    assertEquals(null, p.isRoundedRect());
                }
            }
        }

        for (var dir: Path.Direction.values()) {
            try (Path p = new Path().addCircle(20, 20, 20, dir)) {
                assertEquals(null, p.isRect());
                assertEquals(Rect.makeLTRB(0, 0, 40, 40), p.isOval());
                assertEquals(null, p.isRoundedRect());
            }
        }

        for (var dir: Path.Direction.values()) {
            for (int start = 0; start < 8; ++start) {
                try (Path p = new Path().addRoundedRect(RoundedRect.makeLTRB(0, 0, 40, 20, 5), dir, start)) {
                    assertEquals(null, p.isRect());
                    assertEquals(null, p.isOval());
                    assertEquals(RoundedRect.makeLTRB(0, 0, 40, 20, 5), p.isRoundedRect());
                }

                try (Path p = new Path().addRoundedRect(RoundedRect.makeLTRB(0, 0, 40, 20, 0), dir, start)) {
                    assertEquals(Rect.makeLTRB(0, 0, 40, 20), p.isRect());
                    assertEquals(null, p.isOval());
                    assertEquals(null, p.isRoundedRect());
                }

                try (Path p = new Path().addRoundedRect(RoundedRect.makeLTRB(0f, 0f, 40f, 20f, 20f, 10f), dir, start)) {
                    assertEquals(null, p.isRect());
                    assertEquals(Rect.makeLTRB(0, 0, 40, 20), p.isOval());
                    assertEquals(null, p.isRoundedRect());
                }
            }
        }

        try (Path p = new Path().lineTo(40, 40).lineTo(40, 0).lineTo(0, 40).lineTo(0, 0).closePath()) {
            assertEquals(null, p.isRect());
            assertEquals(null, p.isOval());
            assertEquals(null, p.isRoundedRect());
        }
    }

    @Test
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
            assertEquals(null, p.isLine());
        }

        try (Path p = new Path().moveTo(20, 20).lineTo(40, 40)) {
            assertArrayEquals(new Point[] { new Point(20, 20), new Point(40, 40) }, p.isLine());
        }
    }

    @Test
    public void storage() {
        Path subpath = new Path().lineTo(40, 40).lineTo(40, 0).lineTo(0, 40).lineTo(0, 0).closePath();
        for (Path p: new Path[] {
                       new Path().addPath(subpath),
                       new Path().incReserve(10).addPath(subpath).closePath(),
                       new Path().incReserve(10).addPath(subpath).shrinkToFit()
                     })
        {
            Point p0 = new Point(0, 0);
            Point p1 = new Point(40, 40);
            Point p2 = new Point(40, 0);
            Point p3 = new Point(0, 40);
            Point p4 = new Point(0, 0);

            assertEquals(5, p.countPoints());
            assertEquals(p0, p.getPoint(0));
            assertEquals(p1, p.getPoint(1));
            assertEquals(p2, p.getPoint(2));
            assertEquals(p3, p.getPoint(3));
            assertEquals(p4, p.getPoint(4));

            assertEquals(5, p.getPoints(null, 0));
            
            Point[] pts = new Point[5];
            p.getPoints(pts, 5);
            assertArrayEquals(new Point[] { p0, p1, p2, p3, p4 }, pts);

            pts = new Point[3];
            p.getPoints(pts, 3);
            assertArrayEquals(new Point[] { p0, p1, p2 }, pts);

            pts = new Point[5];
            p.getPoints(pts, 3);
            assertArrayEquals(new Point[] { p0, p1, p2, null, null }, pts);

            pts = new Point[10];
            p.getPoints(pts, 10);
            assertArrayEquals(new Point[] { p0, p1, p2, p3, p4, null, null, null, null, null }, pts);

            assertEquals(6, p.countVerbs());
            assertEquals(6, p.getVerbs(null, 0));

            Path.Verb[] verbs = new Path.Verb[6];
            p.getVerbs(verbs, 6);
            assertArrayEquals(new Path.Verb[] { Path.Verb.MOVE, Path.Verb.LINE, Path.Verb.LINE, Path.Verb.LINE, Path.Verb.LINE, Path.Verb.CLOSE }, verbs);

            verbs = new Path.Verb[3];
            p.getVerbs(verbs, 3);
            assertArrayEquals(new Path.Verb[] { Path.Verb.MOVE, Path.Verb.LINE, Path.Verb.LINE }, verbs);

            verbs = new Path.Verb[6];
            p.getVerbs(verbs, 3);
            assertArrayEquals(new Path.Verb[] { Path.Verb.MOVE, Path.Verb.LINE, Path.Verb.LINE, null, null, null }, verbs);

            verbs = new Path.Verb[10];
            p.getVerbs(verbs, 10);
            assertArrayEquals(new Path.Verb[] { Path.Verb.MOVE, Path.Verb.LINE, Path.Verb.LINE, Path.Verb.LINE, Path.Verb.LINE, Path.Verb.CLOSE, null, null, null, null }, verbs);

            assertEquals(62, p.approximateBytesUsed());
        }
    }

    @Test
    public void swap() {
        try (Path p1 = new Path().lineTo(40, 40).lineTo(40, 0).lineTo(0, 40).lineTo(0, 0).closePath();
             Path p2 = new Path().lineTo(0, 0).lineTo(20, 20);) {
            p1.swap(p2);
            assertEquals(new Path().lineTo(0, 0).lineTo(20, 20), p1);
            assertEquals(new Path().lineTo(40, 40).lineTo(40, 0).lineTo(0, 40).lineTo(0, 0).closePath(), p2);
        }
    }

    @Test
    public void contains() {
        try (Path p = new Path().addRoundedRect(RoundedRect.makeLTRB(10, 20, 54, 120, 10, 20))) {
            assertEquals(true, p.conservativelyContainsRect(Rect.makeLTRB(10, 40, 54, 80)));
            assertEquals(true, p.conservativelyContainsRect(Rect.makeLTRB(25, 20, 39, 120)));
            assertEquals(true, p.conservativelyContainsRect(Rect.makeLTRB(15, 25, 49, 115)));
            assertEquals(true, p.conservativelyContainsRect(Rect.makeLTRB(13, 27, 51, 113)));

            assertEquals(false, p.conservativelyContainsRect(Rect.makeLTRB(0, 40, 60, 80)));
        }
    }

    @Test
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
    }
}