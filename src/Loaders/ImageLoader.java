package Loaders;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class ImageLoader {
    private HashMap<String, Image> images;
    private final String path;
    private int width, height;

    public ImageLoader(String path) {
        this.path = path;
        this.images = new HashMap<>();
    }

    /**
     * This method is used to load the image directory <p>
     *     using a for loop and adds failed images to the <code>fails ArrayList</code>
     * </p>
     */
    public void load() {
    File dir = new File(path);
    if (!dir.exists()) {
        throw new RuntimeException("Directory does not exist");
    }

    ArrayList<String> fails = new ArrayList<>();
    File[] files = dir.listFiles();

    if (files != null) {
        for (File file : files) {
            if (file.isFile()) {
                try {
                    Image image = ImageIO.read(file);
                    if (image != null) {
                        images.put(file.getName(), image);
                    } else {
                        fails.add(file.getName());
                    }
                } catch (IOException e) {
                    fails.add(file.getName());
                    e.printStackTrace();
                }
            }
        }
    }
    if (!fails.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Error loading image(s): " + String.join("\n", fails), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    /**
     * This method is used to display the image
     *
     * @param images the list of images
     * @param index  the current image
     * @param label  image label
     */
    public void displayImage(Map<String, Image> images, int index, JLabel label) {
        Image image = null;
        if (index < images.size() && index >= 0) {
            int i = 0;
            for (Map.Entry<String, Image> entry : images.entrySet()) {
                if (i == index) {
                    image = entry.getValue();
                    width =(image.getWidth(null));
                    height =(image.getHeight(null));
                    break;
                }
                i++;
            }
        }
        SetImageSize.setImageSize(width,height,image,label);
    }

    /**
     * Helper method
     *
     * @param filename filename of the image
     */
    private void displayImage(String filename) {
        Image image = images.get(filename);
        JFrame frame = new JFrame(filename);
        if (image != null) {
            ImageIcon icon = new ImageIcon(image.getScaledInstance(width / 3, height / 3, Image.SCALE_SMOOTH));
            frame.add(new JLabel(icon));
            frame.pack();
            frame.setVisible(true);
        }
    }

    /**
     * Method for displaying the image properties
     *
     * @param filename filename of the image
     */
    public void displayImageProperties(String filename) {
        try {
            File file = new File(path, filename);
            Image image = images.get(filename);
            width = image.getWidth(null);
            height = image.getHeight(null);
            long fileSize = file.length();
            //Create a StringBuilder
            StringBuilder properties = new StringBuilder();
            properties.append("Filename: ").append(filename).append("\n");
            properties.append("Resolution: ").append(width).append("x").append(height).append("\n");
            properties.append("File Size: ").append(fileSize / 1024).append(" kB").append("\n");
            properties.append("Megapixels: ").append((width * height) / 1000000.0).append("MP").append("\n");

            JFrame info = new JFrame("Image properties");
            JTextArea text = new JTextArea(properties.toString());
            text.setFont(new Font("Monospaced", Font.PLAIN, 16));
            JButton showImageButton = new JButton("Show Image");
            showImageButton.addActionListener(e -> displayImage(filename));

            info.add(showImageButton, BorderLayout.SOUTH);
            info.add(text, BorderLayout.CENTER);
            info.setSize(300, 150);
            info.setFont(new Font("Arial", Font.PLAIN, 14));
            info.setLocationRelativeTo(null);
            info.setVisible(true);
            text.setEditable(false);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error reading image properties", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Displays a grid layout of images from the provided HashMap and allows users to click on images
     * to view their properties.
     *
     * @param images    A HashMap containing image filenames as keys and Image objects as values.
     * @param imageList An ImageLoader object to load and retrieve images.
     */
    public void showGrid(HashMap<String, Image> images, ImageLoader imageList) {
        imageList.load(); // Load the images
        images = imageList.getImages();
        JFrame grid = new JFrame("Overview of Images - Click an Image for Properties");
        grid.setLayout(new GridLayout(3, 0));
        grid.setIconImage(new ImageIcon("icons/photo.png").getImage());

        if (!images.isEmpty()) {
            for (Map.Entry<String, Image> entry : images.entrySet()) {
                // Determine the resize dimensions while maintaining the aspect ratio
                int maxWidth = 400; // Maximum width for the images
                int maxHeight = 400; // Maximum height for the images
                double aspectRatio = (double) entry.getValue().getWidth(null) / entry.getValue().getHeight(null);
                int width, height;

                // Resize based on the longest dimension
                if (entry.getValue().getWidth(null) > entry.getValue().getHeight(null)) {
                    width = maxWidth;
                    height = (int) (maxWidth / aspectRatio);
                } else {
                    width = (int) (maxHeight * aspectRatio);
                    height = maxHeight;
                }

                ImageIcon imageIcon = new ImageIcon(entry.getValue().getScaledInstance(width, height, Image.SCALE_SMOOTH));
                JLabel imgLabel = new JLabel(imageIcon);
                imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
                imgLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                imgLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        imageList.displayImageProperties(entry.getKey());
                    }
                });
                grid.add(imgLabel);
            }

            grid.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            grid.pack();
            grid.setLocationRelativeTo(null);
            grid.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No images found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    //getter
    public HashMap<String, Image> getImages() {
        return images;
    }
}

