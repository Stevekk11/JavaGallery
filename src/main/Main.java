package main;

import javax.swing.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                linesOfCode();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void linesOfCode() throws IOException, InterruptedException {
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