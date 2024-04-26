package main;
import javax.swing.*;
import java.io.*;

public class Main {
    /**
     * instantiate the Gallery class so open the Frame
     * @param args cli - not used
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            //set to open a message prompt
            Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
                JOptionPane.showMessageDialog(null, e, "An unhandled exception occurred in thread: "+t, JOptionPane.ERROR_MESSAGE);
            });
            try {
                linesOfCode();
                //do not select the libraries folder!!
                Gallery gallery = new Gallery();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        });
    }


    /**
     *This method is used for counting the lines of code. NOT MY CREATION. See: <a href="https://github.com/AlDanial/cloc">Github</a> for more info.
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