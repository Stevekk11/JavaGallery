package Loaders;

import javax.swing.*;
import java.awt.*;

public class SetImageSize {
    /**
     * No instantiation
     */
    private SetImageSize() {
    }

    /**
     * These methods are used to set the size of the image according to its dimensions.
     * @param width the width of the image
     * @param height the height of the image
     * @param image the image to be set
     * @param label the JLabel to use
     */
    public static void setImageSize(int width, int height, Image image, JLabel label) {
        if (width*height /1000000 > 2 && image != null) {
            ImageIcon icon = new ImageIcon(image.getScaledInstance(width/4, height/4, Image.SCALE_SMOOTH));
            label.setIcon(icon);
        } else {
            assert image != null;
            ImageIcon icon = new ImageIcon(image.getScaledInstance(width/2, height/2, Image.SCALE_SMOOTH));
            label.setIcon(icon);
        }
    }

    /**
     * Same as the first method
     * @return the scaled Image
     */
    public static Image setImageSize(int width, int height, Image image) {
        if (width*height /1000000 > 2 && image != null) {
            ImageIcon icon = new ImageIcon(image.getScaledInstance(width/4, height/4, Image.SCALE_SMOOTH));
            return icon.getImage();
        } else {
            assert image != null;
            ImageIcon icon = new ImageIcon(image.getScaledInstance(width/2, height/2, Image.SCALE_SMOOTH));
            return icon.getImage();
        }
    }
}
