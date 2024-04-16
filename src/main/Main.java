package main;

import javax.swing.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                linesOfCode();
                Gallery g = new Gallery();
            } catch (UnsupportedLookAndFeelException | ClassNotFoundException |
                     InstantiationException | IllegalAccessException  | IOException e ) {
                JOptionPane.showMessageDialog(null, e);
            }
        });
    }
    /**
     *This method is used for counting the lines of code. NOT MY CREATION. See: <a href="https://github.com/AlDanial/cloc">Github</a> for more info.
     * @throws IOException
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