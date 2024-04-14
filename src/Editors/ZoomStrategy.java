package Editors;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

public class ZoomStrategy implements ImageEditStrategy {

    public ZoomStrategy() {
    }

    @Override
    public void editImage(Map<String, Image> images, int index, JLabel label) {
        JFrame zoomLevelFrame = new JFrame("Zoom Level");
        JButton zoomLevelButton = new JButton("Zoom");
        JTextArea zoomLevelField = new JTextArea();
        zoomLevelField.setEditable(true);
        zoomLevelFrame.add(zoomLevelField, BorderLayout.CENTER);
        zoomLevelFrame.setSize(250, 100);
        zoomLevelFrame.setVisible(true);
        zoomLevelFrame.setLocationRelativeTo(null);
        zoomLevelFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        zoomLevelButton.addActionListener(e -> {
            String zoomLevelText = zoomLevelField.getText();
            try {
                Image image;
                double zoomLevel = Double.parseDouble(zoomLevelText);
                if (index < images.size() && index >= 0) {
                    int i = 0;
                    for (Map.Entry<String, Image> entry : images.entrySet()) {
                        if (i == index) {
                            image = entry.getValue().getScaledInstance(entry.getValue().getWidth(null)/4, entry.getValue().getHeight(null)/4, Image.SCALE_SMOOTH);
                            // Perform zoom operation on the image
                            Image zoomedImage = zoomImage(image, zoomLevel);
                            ImageIcon icon = new ImageIcon(zoomedImage);
                            label.setIcon(icon);
                            label.repaint();// Update the label
                            break;
                        }
                        i++;
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(zoomLevelFrame, "Invalid zoom level. Please enter a valid number.");
            }
        });
        zoomLevelFrame.add(zoomLevelButton, BorderLayout.SOUTH);
    }

    /**
     * Method to zoom the image
     * @param image the image to be zoomed
     * @param zoomLevel the zoom factor
     * @return the zoomed image
     */
    private Image zoomImage(Image image, double zoomLevel) {
        int newWidth = (int) (image.getWidth(null) * zoomLevel);
        int newHeight = (int) (image.getHeight(null) * zoomLevel);
        BufferedImage zoomedImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = zoomedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(image, 0, 0, newWidth, newHeight, null);
        g2.dispose();
        return zoomedImg;
    }
}

