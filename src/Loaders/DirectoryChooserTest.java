package Loaders;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DirectoryChooserTest {
    DirectoryChooser directoryChooser;
    DirectoryChooser savingDirectoryChooser;

    @BeforeEach
    void setUp() {
        directoryChooser = new DirectoryChooser("Chooser");
        directoryChooser.chooseDirectory();
        savingDirectoryChooser = new DirectoryChooser("Saving directory",true);
    }

    @Test
    void chooseDirectory() {
        assertNotEquals(directoryChooser.getFileSelectionMode(),savingDirectoryChooser.getFileSelectionMode());
    }
}