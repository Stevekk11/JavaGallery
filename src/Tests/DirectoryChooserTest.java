package Tests;

import Loaders.DirectoryChooser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

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
        assertEquals("changedImg.png",savingDirectoryChooser.getSelectedFile().getName());
        assertNotNull(directoryChooser);
        assertEquals(JFileChooser.DIRECTORIES_ONLY, directoryChooser.getFileSelectionMode());
        assertEquals(JFileChooser.FILES_ONLY, savingDirectoryChooser.getFileSelectionMode());
    }
}