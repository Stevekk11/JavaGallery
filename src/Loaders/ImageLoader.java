package Loaders;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import main.Gallery;

public class ImageLoader {
    private HashMap<String, Image> images;
    private String path;

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

    public HashMap<String, Image> getImages() {
        return images;
    }
}
