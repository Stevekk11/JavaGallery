package Loaders;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImageLoader {
    private HashMap<String, Image> images;
    private String path;
    private int width, height;

    public ImageLoader(String path) {
        this.path = path;
        this.images = new HashMap<>();
    }

    /**
     * This method is used to load the image directory
     */
    public void load() {
        File dir = new File(path);
        if (!dir.exists()) {
            throw new RuntimeException("Directory does not exist");
        }
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    try {
                        Image image = ImageIO.read(file);
                        if (image != null) {
                            images.put(file.getName(), image);
                        } else
                            JOptionPane.showMessageDialog(null, "Error loading image: " + file.getName(), "Error", JOptionPane.ERROR_MESSAGE);
                        // Store the filename along with the Image object in the HashMap
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * This method is used to display the image
     *
     * @param images the list of images
     * @param index  the current image
     * @param label
     */
    public void displayImage(Map<String, Image> images, int index, JLabel label) {
        Image image = null;
        if (index < images.size() && index >= 0) {
            int i = 0;
            for (Map.Entry<String, Image> entry : images.entrySet()) {
                if (i == index) {
                    image = entry.getValue();
                    width = image.getWidth(null) / 4;
                    height = image.getHeight(null) / 4;
                    break;
                }
                i++;
            }
        }
        if (image != null) {
            ImageIcon icon = new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_SMOOTH));
            label.setIcon(icon);
        } else label.setIcon(null);
    }

    private void displayImage(String filename) {
        Image image = images.get(filename);
        JFrame frame = new JFrame(filename);
        if (image != null) {
            ImageIcon icon = new ImageIcon(image.getScaledInstance(width/5, height/5, Image.SCALE_SMOOTH));
            frame.add(new JLabel(icon));
            frame.pack();
            frame.setVisible(true);
        }
    }

    public void displayImageProperties(String filename) {
        try {
            File file = new File(path, filename);
            Image image = images.get(filename);
            width = image.getWidth(null);
            height = image.getHeight(null);
            long fileSize = file.length();

            StringBuilder properties = new StringBuilder();
            properties.append("Filename: ").append(filename).append("\n");
            properties.append("Resolution: ").append(width).append("x").append(height).append("\n");
            properties.append("File Size: ").append(fileSize / 1024).append(" kB").append("\n");
            properties.append("Megapixels: ").append((width * height) / 1000000.0).append("MP").append("\n");

            JFrame info = new JFrame("Image properties");
            JTextArea text = new JTextArea(properties.toString());
            JButton showImageButton = new JButton("Show Image");
            showImageButton.addActionListener(e -> displayImage(filename));

            info.add(showImageButton, BorderLayout.SOUTH);
            info.add(text, BorderLayout.CENTER);
            info.setSize(300, 150);
            info.setLocationRelativeTo(null);
            info.setVisible(true);
            text.setEditable(false);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error reading image properties", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public HashMap<String, Image> getImages() {
        return images;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

