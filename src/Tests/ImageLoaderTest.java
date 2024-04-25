package Tests;

import Loaders.ImageLoader;

import static org.junit.jupiter.api.Assertions.*;

class ImageLoaderTest {
    ImageLoader loader;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        loader = new ImageLoader("images");
    }

    /**
     * Testing if the loader correctly loads the image;
     */
    @org.junit.jupiter.api.Test
    void load() {
        loader.load();
        assertNotNull(loader.getImages());
        System.out.println(loader.getImages());
        assertFalse(loader.getImages().isEmpty());
    }
    @org.junit.jupiter.api.Test
    void testLoadInvalidDirectory() {
        loader = new ImageLoader("invalid");
        assertThrows(RuntimeException.class, () -> loader.load());
    }

    @org.junit.jupiter.api.Test
    void displayImage() {
    }

    @org.junit.jupiter.api.Test
    void showGrid() {
    }
}