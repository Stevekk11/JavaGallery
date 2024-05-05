package Tests;

import Editors.GrayScaleStrategy;
import Editors.ImageEditor;
import Editors.ZoomStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ImageEditorTest {
    private GrayScaleStrategy grayScaleStrategy;
    private Image image;
    private JLabel labelAfter;
    private JLabel labelBefore;
    private HashMap<String,Image> imageHashMap;

    @BeforeEach
    void setUp() throws IOException {
        grayScaleStrategy = new GrayScaleStrategy();
        labelAfter = new JLabel();
        labelBefore = new JLabel();
        image = ImageIO.read(new File("images(for testing)/Acer_Wallpaper_03_3840x2400.jpg"));
        labelBefore.setIcon(new ImageIcon(image));
        imageHashMap = new HashMap<>();
        imageHashMap.put("Acer_Wallpaper_03_3840x2400",image);
    }

    @Test
    void editImage() {
        grayScaleStrategy.editImage(imageHashMap,0,labelAfter);
        assertNotEquals(labelAfter,labelBefore);
    }
}