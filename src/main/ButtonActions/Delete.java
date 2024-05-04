package main.ButtonActions;

import Loaders.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Delete {

    private Delete() {
    }

    /**
     * Method for deleting images
     * @param currentIndex index of the image
     * @param images images hashmap
     * @param imgLabel JLabel
     * @param imageLoader class for loading images
     * @param dialog JFrame for displaying image
     * @param path filepath
     */
    public static void deleteImage(AtomicInteger currentIndex, HashMap<String, Image> images, JLabel imgLabel, ImageLoader imageLoader, JFrame dialog, String path){
        if (currentIndex.get() >= 0 && currentIndex.get() < images.size()) {
            String filenameToDelete = "";
            int i = 0;
            for (Map.Entry<String, Image> entry : images.entrySet()) {
                if (i == currentIndex.get()) {
                    filenameToDelete = entry.getKey();
                    break;
                }
                i++;
            }
            if (!filenameToDelete.isEmpty()) {
                images.remove(filenameToDelete);
                File fileToDelete = new File(path + "/" + filenameToDelete);
                if (fileToDelete.exists()) {
                    if (fileToDelete.delete()) {
                        JOptionPane.showMessageDialog(null, "Image deleted successfully");
                    } else {
                        JOptionPane.showMessageDialog(null, "Image could not be deleted");
                    }
                } else JOptionPane.showMessageDialog(null, "File does not exist");
                currentIndex.set(currentIndex.get() - 1);
                imageLoader.displayImage(images, currentIndex.get(), imgLabel);
                dialog.pack();
            }
        }
    }
}
