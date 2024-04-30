package Editors;

import Loaders.DirectoryChooser;
import Loaders.SetImageSize;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Implements the ImageEditStrategy interface to provide grayscale conversion for images.
 */
public class GrayScaleStrategy implements ImageEditStrategy {
    private BufferedImage bufferedImage;

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
                    Image image = SetImageSize.setImageSize(entry.getValue().getWidth(null),entry.getValue().getHeight(null),entry.getValue());
                    bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_BYTE_GRAY);
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

    /**
     * Method to save the image
     */
    public void saveImg() {
        DirectoryChooser directoryChooser = new DirectoryChooser("Save image", true);
        File toSave = directoryChooser.getSelectedFile();
        try {
            // Save the compressed image to the selected file
            ImageIO.write(bufferedImage, "jpg", toSave);
            JOptionPane.showMessageDialog(null, "Image saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
