package Loaders;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * A custom directory chooser dialog based on JFileChooser that allows users to select directories or files.
 * <p>
 * This class extends JFileChooser and provides methods to customize the behavior of the file chooser dialog.
 * </p>
 */
public class DirectoryChooser extends JFileChooser {

    /**
     * Constructs a new DirectoryChooser with the specified dialog title.
     *
     * @param title the title of the directory chooser dialog
     */
    public DirectoryChooser(String title) {
        super(title);
        setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        setLocation(new Point(0, 0));
    }

    /**
     * Constructs a new DirectoryChooser with the specified dialog title and mode.
     * <p>
     * If the saving parameter is true, the file chooser will be configured to select files only.
     * </p>
     *
     * @param title  the title of the directory chooser dialog
     * @param saving true if the file chooser is used for saving files, false otherwise
     */
    public DirectoryChooser(String title, boolean saving) {
        super(title);
        if (saving) {
            setFileSelectionMode(JFileChooser.FILES_ONLY);
            setSelectedFile(new File("changedImg.png"));
            setLocation(new Point(0, 0));
            int result = showSaveDialog(this);
            if (result == JFileChooser.CANCEL_OPTION) {
                setSelectedFile(null);
            }
        }
    }

    /**
     * Shows the directory chooser dialog and allows the user to choose a directory or file.
     * @return the absolute path of the selected directory or file, or null if no selection was made
     */
    public String chooseDirectory() {
        int result = showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            return getSelectedFile().getAbsolutePath();
        } else if (result == JFileChooser.CANCEL_OPTION) {
            setSelectedFile(null);
        }
        return null;
    }
}

