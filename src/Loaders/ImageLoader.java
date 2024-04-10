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
                        } else JOptionPane.showMessageDialog(null, "Error loading image: "+file.getName(), "Error", JOptionPane.ERROR_MESSAGE);
                        // Store the filename along with the Image object in the HashMap
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    public void displayImage(Map<String, Image> images, int index, JLabel label) {
        String filename = "";
        Image image = null;
        if (index < images.size() && index >= 0) {
            int i = 0;
            for (Map.Entry<String, Image> entry : images.entrySet()) {
                if (i == index) {
                    filename = entry.getKey();
                    image = entry.getValue();
                    width = image.getWidth(null)/4;
                    height = image.getHeight(null)/4;
                    break;
                }
                i++;
            }
        }
        if (image != null) {
            ImageIcon icon = new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_SMOOTH));
            label.setIcon(icon);
            label.setText(filename + (width == 0 ? "" : " (" + width + ")")+"x"+(height == 0 ? "" : " (" + height + ")"));
        } else label.setIcon(null);
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
