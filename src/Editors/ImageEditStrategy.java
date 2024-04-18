package Editors;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public interface ImageEditStrategy {
    /**
     * This interface method is used for editing the image.
     * It can be reused
     * Strategy design pattern
     * @param images a HashMap of images
     * @param index the current image
     * @param label the image label
     */
    void editImage(Map<String, Image> images, int index, JLabel label);
}
