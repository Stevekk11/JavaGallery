package Editors;

import Loaders.DirectoryChooser;
import Loaders.SetImageSize;
import Logger.GalleryLogger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

/**
 * Implements the ImageEditStrategy interface to provide contrast adjustment functionality for images.
 */
public class ChangeContrastStrategy implements ImageEditStrategy {
    private Image changedImage;
    private DirectoryChooser directoryChooser;

    public ChangeContrastStrategy() {
    }

    /**
     * Edits the image by allowing the user to adjust its contrast.
     *
     * @param images A map containing image filenames as keys and Image objects as values.
     * @param index  The index of the image to be edited.
     * @param label  The JLabel component where the edited image will be displayed.
     */
    @Override
    public void editImage(Map<String, Image> images, int index, JLabel label) {
        JFrame frame = new JFrame("Change Contrast");
        JSlider slider = new JSlider(JSlider.HORIZONTAL, -100, 100, 0);
        JLabel valueLabel = new JLabel("Current Value: 0");
        JButton saveButton = new JButton("Save");
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();

        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setMajorTickSpacing(50);
        slider.setMinorTickSpacing(20);

        labelTable.put(-100, new JLabel("-100"));
        labelTable.put(0, new JLabel("0"));
        labelTable.put(100, new JLabel("100"));
        slider.setLabelTable(labelTable);

        frame.add(slider, BorderLayout.CENTER);
        frame.add(valueLabel, BorderLayout.SOUTH);
        frame.add(saveButton, BorderLayout.NORTH);
        frame.setSize(300, 150);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        slider.addChangeListener(e -> {
            int value = slider.getValue();
            valueLabel.setText("Current Value: " + value);
            Image image;
            if (!slider.getValueIsAdjusting()) {
                if (index < images.size() && index >= 0) {
                    int i = 0;
                    for (Map.Entry<String, Image> entry : images.entrySet()) {
                        if (i == index) {
                            image = SetImageSize.setImageSize(entry.getValue().getWidth(null),entry.getValue().getHeight(null),entry.getValue());
                            changedImage = applyContrast(image, value);
                            ImageIcon icon = new ImageIcon(changedImage);

                            label.setIcon(icon);
                            label.repaint();//update
                            break;// Exit the loop after applying contrast to the selected image
                        }
                        i++;
                    }
                }
            }
        });
        saveButton.addActionListener(e1 -> {
            //save
            directoryChooser = new DirectoryChooser("Select where to save the image", true);
            File fileToSave = directoryChooser.getSelectedFile();
            try {
                // Write the changedImage to the selected file as a PNG image
                ImageIO.write((RenderedImage) changedImage, "png", fileToSave);
                JOptionPane.showMessageDialog(null, "Image saved successfully!");
            } catch (IOException | IllegalArgumentException ex) {
                GalleryLogger.logError(ex.toString());
                if (ex instanceof IllegalArgumentException) {
                    JOptionPane.showMessageDialog(frame, "Saving cancelled by user!");

                } else JOptionPane.showMessageDialog(frame, "Error saving image: " + ex.getMessage());
            }
        });
    }

    private Image applyContrast(Image image, int contrastValue) {
        // Convert the Image to BufferedImage to allow editing
        BufferedImage bufferedImage = toBufferedImage(image);

        // Get width and height of the image
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        // Loop through each pixel to adjust the contrast
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // Get the current pixel
                int pixel = bufferedImage.getRGB(x, y);
                Color color = new Color(pixel, true);

                // Adjust the contrast of each channel (red, green, blue)
                int r = adjustContrast(color.getRed(), contrastValue);
                int g = adjustContrast(color.getGreen(), contrastValue);
                int b = adjustContrast(color.getBlue(), contrastValue);

                // Set the new pixel color
                Color newColor = new Color(clamp(r), clamp(g), clamp(b));
                bufferedImage.setRGB(x, y, newColor.getRGB());
            }
        }

        return bufferedImage;
    }

    // Helper method to convert Image to BufferedImage
    private BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    // Helper method to adjust the contrast of a single color channel
    private int adjustContrast(int colorValue, int contrastValue) {
        double factor = (double) (259 * (contrastValue + 255)) / (255 * (259 - contrastValue));
        return (int) (factor * (colorValue - 128) + 128);
    }

    // Helper method to clamp color values to the range 0-255
    private int clamp(int value) {
        return Math.min(Math.max(value, 0), 255);
    }
}
