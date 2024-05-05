package Tests;

import Loaders.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ImageLoaderTest {
    ImageLoader loader;
    ImageLoader invalidLoader;
    HashMap<String, Image> images;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        loader = new ImageLoader("images");
        invalidLoader = new ImageLoader("invalid");
        images = new HashMap<>();
    }

    /**
     * Testing if the loader correctly loads the image;
     */
    @org.junit.jupiter.api.Test
    void testLoad() {
        loader.load();
        assertNotNull(loader.getImages());
        System.out.println(loader.getImages());
        assertFalse(loader.getImages().isEmpty());
        assertEquals(13, loader.getImages().size());
    }

    @org.junit.jupiter.api.Test
    void testLoadInvalidDirectory() {
        assertThrows(RuntimeException.class, () -> invalidLoader.load());
    }

    /**
     * Testing if the image label is correctly displayed
     */
    @org.junit.jupiter.api.Test
    void testDisplayImage() {
        images.put("test", new ImageIcon("images/0023.jpg").getImage());
        images.put("test2", new ImageIcon("images/0024.jpg").getImage());
        JLabel testLabel = new JLabel();
        loader.displayImage(images, 1, testLabel);
        assertNotNull(testLabel.getIcon());
        loader.displayImage(images, 3, testLabel);
        assertFalse(testLabel.isDisplayable());
        assertNull(testLabel.getIcon());
        assertTrue(testLabel.isVisible());
    }

    @org.junit.jupiter.api.Test
    void testShowGrid() {
        loader.showGrid(loader);
        assertNotNull(getGridFrame());
        assertThrows(RuntimeException.class, () -> loader.showGrid(invalidLoader));
        JFrame grid = getGridFrame();
        Component[] components = grid.getContentPane().getComponents();
        assertEquals(13,components.length);

    }
    private JFrame getGridFrame() {
        for (Frame frame : Frame.getFrames()) {
            if (frame instanceof JFrame && frame.getTitle().equals("Overview of Images - Click an Image for Properties")) {
                return (JFrame) frame;
            }
        }
        return null;
    }
}