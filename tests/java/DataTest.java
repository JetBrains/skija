package org.jetbrains.skija.test;

import static org.jetbrains.skija.test.runner.TestRunner.assertArrayEquals;
import static org.jetbrains.skija.test.runner.TestRunner.assertEquals;
import static org.jetbrains.skija.test.runner.TestRunner.assertNotEquals;

import java.nio.charset.StandardCharsets;

import org.jetbrains.skija.Data;
import org.jetbrains.skija.test.runner.Executable;

public class DataTest implements Executable {
    @Override
    public void execute() throws Exception {
        try (var data = Data.makeEmpty()) {
            assertEquals(0L, data.getSize());
            assertArrayEquals(new byte[0], data.getBytes());
            try (var data2 = Data.makeEmpty()) {
                assertEquals(data, data2);
            }
        }

        byte[] bytes = "abcdef".getBytes(StandardCharsets.UTF_8);
        byte[] bytesSubset = "bcde".getBytes(StandardCharsets.UTF_8);
        try (var data = Data.makeFromBytes(bytes)) {
            assertEquals(6L, data.getSize());
            assertArrayEquals(bytes, data.getBytes());
            assertArrayEquals(bytesSubset, data.getBytes(1, 4));
            
            try (var data2 = Data.makeFromBytes(bytes)) {
                assertEquals(data, data2);
            }

            try (var data2 = data.makeCopy()) {
                assertEquals(data, data2);
            }

            try (var data3 = Data.makeFromBytes(bytes, 1, 4)) {
                assertEquals(4L, data3.getSize());
                assertArrayEquals(bytesSubset, data3.getBytes());
                assertNotEquals(data, data3);
            }

            try (var data4 = data.makeSubset(1, 4)) {
                assertEquals(4L, data4.getSize());
                assertArrayEquals(bytesSubset, data4.getBytes());
                assertNotEquals(data, data4);
            }
        }
    }
}