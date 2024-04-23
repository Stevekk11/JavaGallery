package Editors;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;
/**
 * Implements the ImageEditStrategy interface to provide grayscale conversion for images.
 */
public class GrayScaleStrategy implements ImageEditStrategy {
    /**
     * Converts the specified image to grayscale and updates the label with the new image.
     *
     * @param images A map containing image filenames as keys and Image objects as values.
     * @param index  The index of the image to be edited.
     * @param label  The JLabel component where the edited image will be displayed.
     */
    @Override
    public void editImage(Map<String, Image> images, int index, JLabel label) {
        if (index < images.size()) {
            int i = 0;
            for (Map.Entry<String, Image> entry : images.entrySet()) {
                if (i == index) {
                    //convert the image to gray-scale
                    Image image = entry.getValue().getScaledInstance(entry.getValue().getWidth(null) / 4, entry.getValue().getHeight(null) / 4, Image.SCALE_SMOOTH);
                    BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_BYTE_GRAY);
                    Graphics graphics = bufferedImage.getGraphics();
                    graphics.drawImage(image, 0, 0, null);
                    graphics.dispose();
                    //update the label with the new image
                    ImageIcon grayscale = new ImageIcon(bufferedImage);
                    label.setIcon(grayscale);
                    break;
                }
                i++;
            }
        }
    }
}
