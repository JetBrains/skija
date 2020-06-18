package org.jetbrains.skija;

import org.jetbrains.skija.test.TestRunner;

public class TestSuite {
    public static void main(String[] args) {
        Library.load("/", "skija");
        TestRunner.startTesting();
        TestRunner.testClass(PathTest.class);
        TestRunner.testClass(FontTest.class);
        TestRunner.testClass(DataTest.class);
        // TestRunner.testClass(TestTest.class);
        int res = TestRunner.finishTesting();
        System.exit(res > 0 ? 1 : 0);
    }
}