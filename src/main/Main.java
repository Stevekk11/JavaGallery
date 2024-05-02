package main;

import Logger.GalleryLogger;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    /**
     * Starts the Gallery class with an instance of it.
     * @param args not used
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::runApplication);
    }

    /**
     * Runs the code
     */
    private static void runApplication() {
        setGlobalExceptionHandler();
        try {
            linesOfCode();
            Gallery gallery = new Gallery();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Sets what happens in case an uncaught exception happens.
     */
    private static void setGlobalExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler((t, e) ->
            JOptionPane.showMessageDialog(null, e, "An unhandled exception occurred in thread: " + t, JOptionPane.ERROR_MESSAGE)
        );
    }

    /**
     * This method is used for counting the lines of code. NOT MY CREATION. See: <a href="https://github.com/AlDanial/cloc">Github</a> for more info.
     * @throws IOException exception
     */
    public static void linesOfCode() throws IOException {
        String command = "cloc.exe";
        ProcessBuilder pb = new ProcessBuilder(command, "src");
        Process process = pb.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }
}
