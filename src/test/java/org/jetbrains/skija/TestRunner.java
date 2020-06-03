package org.jetbrains.skija;

import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TestRunner {
    public int asserts   = 0;
    public int failures  = 0;
    public int errors    = 0;
    public Deque<String> stack = new ArrayDeque<String>();
    public List<String> messages = new ArrayList<String>();

    public static TestRunner runner = new TestRunner();

    public String location() {
        var st = Thread.currentThread().getStackTrace();
        var ste = st[1];
        for (int i = 1; i < st.length; ++i) {
            if (st[i].getClassName() != "org.jetbrains.skija.TestRunner") {
                ste = st[i];
                break;
            }
        }
        return stack.stream().collect(Collectors.joining(" > ")) + " (" + ste.getFileName() + ":" + ste.getLineNumber() + ")";
    }

    public void fail(String message) {
        failures++;
        messages.add("[ FAIL ] " + message + "\n  at " + location());
        System.out.print("F");
    }

    public void error(Throwable t) {
        errors++;
        messages.add("[ ERROR ] Unexpected exception '" + t + "'\n  at " + location());
        System.out.print("E");
    }

    public static void assertEquals(Object expected, Object actual) {
        runner.asserts++;
        try {
            if (!Objects.equals(expected, actual))
                runner.fail("Expected '" + expected + "' == '" + actual + "'");
            else
                System.out.print(".");
        } catch(Exception e) {
            runner.error(e);
        }
    }

    public static void assertNotEquals(Object expected, Object actual) {
        runner.asserts++;
        try {
            if (Objects.equals(expected, actual))
                runner.fail("Expected '" + expected + "' != '" + actual + "'");
            else
                System.out.print(".");
        } catch(Exception e) {
            runner.error(e);
        }
    }

    public static void assertArrayEquals(Object[] expected, Object[] actual) {
        runner.asserts++;
        try {
            if (!Arrays.equals(expected, actual))
                runner.fail("Expected " + Arrays.toString(expected) + " == " + Arrays.toString(actual));
            else
                System.out.print(".");
        } catch(Exception e) {
            runner.error(e);
        }
    }

    public static void assertThrows(Class<? extends Throwable> expected, Executable executable) {
        runner.asserts++;
        try {
            executable.execute();
            runner.fail("Expected '" + expected.getName() + "', caught nothing");
        } catch(Exception e) {
            if (expected.isInstance(e))
                System.out.print(".");
            else
                runner.fail("Expected '" + expected.getName() + "', caught '" + e + "'");
        }
    }

    public static void testClass(Class<? extends Executable> cls) {
        pushStack(cls.getName());
        try {
            Executable test = cls.getDeclaredConstructor().newInstance();
            test.execute();
        } catch(Exception e) {
            runner.error(e);
        }
        popStack();
    }

    public static void testMethod(Object o, String methodName) {
        pushStack(methodName);
        try {
            Method m = o.getClass().getMethod(methodName);
            m.invoke(o);
        } catch(Exception e) {
            runner.error(e);
        }
        popStack();
    }

    public static void pushStack(String s) {
        runner.stack.addLast(s);
    }

    public static void popStack() {
        runner.stack.removeLast();
    }

    public static void startTesting() {
        runner = new TestRunner();
        System.out.print("[");
    }

    public static int finishTesting() {
        assert runner.stack.isEmpty() : "Expected stack to be empty, got: " + runner.stack.toString();
        System.out.println("]");
        for (String message: runner.messages) {
            System.err.println(message + "\n");
        }
        System.out.println("[ DONE ] Asserts: " + runner.asserts + ", failures: " + runner.failures + ", errors: " + runner.errors);
        return runner.failures + runner.errors;
    }
}