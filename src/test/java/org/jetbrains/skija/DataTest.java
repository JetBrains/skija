package org.jetbrains.skija;

import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import org.jetbrains.skija.test.Executable;
import org.jetbrains.skija.test.TestRunner;

import static org.jetbrains.skija.test.TestRunner.assertArrayEquals;
import static org.jetbrains.skija.test.TestRunner.assertEquals;
import static org.jetbrains.skija.test.TestRunner.assertNotEquals;
import static org.jetbrains.skija.test.TestRunner.assertThrows;

public class DataTest implements Executable {
    @Override
    public void execute() throws Exception {
        try (var data = Data.makeEmpty()) {
            assertEquals(0L, data.size());
            assertArrayEquals(new byte[0], data.bytes());
            try (var data2 = Data.makeEmpty()) {
                assertEquals(data, data2);
            }
        }

        byte[] bytes = "abcdef".getBytes(StandardCharsets.UTF_8);
        byte[] bytesSubset = "bcde".getBytes(StandardCharsets.UTF_8);
        try (var data = Data.makeFromBytes(bytes)) {
            assertEquals(6L, data.size());
            assertArrayEquals(bytes, data.bytes());
            assertArrayEquals(bytesSubset, data.bytes(1, 4));
            
            try (var data2 = Data.makeFromBytes(bytes)) {
                assertEquals(data, data2);
            }

            try (var data2 = data.makeCopy()) {
                assertEquals(data, data2);
            }

            try (var data3 = Data.makeFromBytes(bytes, 1, 4)) {
                assertEquals(4L, data3.size());
                assertArrayEquals(bytesSubset, data3.bytes());
                assertNotEquals(data, data3);
            }

            try (var data4 = data.makeSubset(1, 4)) {
                assertEquals(4L, data4.size());
                assertArrayEquals(bytesSubset, data4.bytes());
                assertNotEquals(data, data4);
            }
        }
    }
}