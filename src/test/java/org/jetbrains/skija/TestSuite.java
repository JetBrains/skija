package org.jetbrains.skija;

public class TestSuite {
    public static void main(String[] args) {
        JNI.loadLibrary("/", "skija");
        TestRunner.startTesting();
        TestRunner.testClass(PathTests.class);
        // TestRunner.testClass(TestsTest.class);
        int res = TestRunner.finishTesting();
        System.exit(res > 0 ? 1 : 0);
    }
}