package Logger;

import java.io.IOException;
import java.util.logging.*;

/**
 * This class is used for logging exceptions and errors
 */
public class GalleryLogger {
    private static final Logger logger = Logger.getLogger(GalleryLogger.class.getName());
    private static FileHandler fh;

    static {
        try {
            fh = new FileHandler("log.log",true);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.addHandler(fh);
            logger.setLevel(Level.ALL);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error initializing logger", e);
        }
    }

    /**
     * Method that logs the message (error)
     * @param message the string to log
     */
    public static void logError(String message) {
        logger.severe(message);
    }

    /**
     * Method to change the log output file
     * @param fileName the name of the output file
     */
    public static void chooseLogFile(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            logger.warning("Log file name is empty or null. Using default log file.");
            return;
        }
        try {
            if (fh != null) {
                logger.removeHandler(fh);
                fh.close();
            }
            fh = new FileHandler(fileName, true);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.addHandler(fh);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error setting log file", e);
        }
    }
}
