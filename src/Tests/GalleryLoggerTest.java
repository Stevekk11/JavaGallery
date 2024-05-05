package Tests;

import Logger.GalleryLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class GalleryLoggerTest {
    BufferedReader br;

    @BeforeEach
    void setUp() throws FileNotFoundException {
        GalleryLogger.chooseLogFile("testlog.log");
        br = new BufferedReader(new FileReader("testlog.log"));
    }

    @Test
    void testLogError() throws IOException {
        String testMessage = "This is a test";
        try {
            throw new IllegalStateException(testMessage);
        } catch (Exception e) {
            GalleryLogger.logError(e.getMessage());
            // Read the first line from the log file
            br.readLine();
            String line = br.readLine();
            String hour = LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm"));
            // Check if the line contains the test message
            assertTrue(line.contains(testMessage));
            assertTrue(line.contains("SEVERE"));
            assertFalse(line.isBlank());
        }
    }
}