package Editors;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

public class ChangeContrastStrategy implements ImageEditStrategy {

    public ChangeContrastStrategy() {
    }

    @Override
    public void editImage(Map<String, Image> images, int index, JLabel label) {
        JFrame frame = new JFrame("Change Contrast");
        JSlider slider = new JSlider(JSlider.HORIZONTAL, -100, 100, 0);
        frame.add(slider);
        frame.setSize(250, 100);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        slider.addChangeListener(e -> {
            int value = slider.getValue();
            Image image;
            if (!slider.getValueIsAdjusting()) {
                if (index < images.size() && index >= 0) {
                    int i = 0;
                    for (Map.Entry<String, Image> entry : images.entrySet()) {
                        if (i == index) {
                            image = entry.getValue().getScaledInstance(entry.getValue().getWidth(null)/4, entry.getValue().getHeight(null)/4, Image.SCALE_SMOOTH);
                            Image changedImage = applyContrast(image, value);
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
