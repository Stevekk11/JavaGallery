package main;

import Loaders.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Gallery extends JFrame {
    protected HashMap<String, Image> images;
    protected JButton load;
    protected JButton exit;
    protected JButton next;
    protected JButton previous;
    protected JButton showGrid;
    protected JButton delete;

    /**
     * This constructor is used for initialising the Frame
     */
    public Gallery() {
        images = new HashMap<>();
        load = new JButton("Load");
        exit = new JButton("Exit");
        next = new JButton("Next");
        previous = new JButton("Previous");
        showGrid = new JButton("Show Grid");
        delete = new JButton("Delete image");
    }

    /**
     * This method initialises the components
     */

    public void init() {
        setTitle("Image Gallery");
        setSize(400, 400);
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2));
        add(buttonPanel, BorderLayout.CENTER);

        buttonPanel.add(load);
        buttonPanel.add(exit);
        buttonPanel.add(next);
        buttonPanel.add(previous);
        buttonPanel.add(showGrid);
        buttonPanel.add(delete);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        //shows the images
        showImages();
    }

    private void showImages() {
        load.addActionListener(e -> {
            ImageLoader imageList = new ImageLoader("images");
            imageList.load();
            images = imageList.getImages();

            if (!images.isEmpty()) {
                JFrame dialog = new JFrame();
                JPanel panel = new JPanel(new BorderLayout());
                JLabel imgLabel = new JLabel();
                panel.add(imgLabel, BorderLayout.CENTER);
                AtomicInteger currentIndex = new AtomicInteger(0);
                imageList.displayImage(images, currentIndex.get(), imgLabel);

                previous.addActionListener(e1 -> {
                    if (currentIndex.get() > 0) {
                        currentIndex.decrementAndGet();
                        imageList.displayImage(images, currentIndex.get(), imgLabel);
                    }
                });

                next.addActionListener(e2 -> {
                    if (currentIndex.get() < images.size() - 1) {
                        currentIndex.incrementAndGet();
                        imageList.displayImage(images, currentIndex.get(), imgLabel);
                    }
                });

                delete.addActionListener(e3 -> {
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
                            File fileToDelete = new File("images/" + filenameToDelete);
                            if (fileToDelete.exists()) {
                                if (fileToDelete.delete()) {
                                    JOptionPane.showMessageDialog(null, "Image deleted successfully");
                                } else {
                                    JOptionPane.showMessageDialog(null, "Image could not be deleted");
                                }
                            } else JOptionPane.showMessageDialog(null, "File does not exist");
                            currentIndex.set(0);
                            imageList.displayImage(images, currentIndex.get(), imgLabel);
                        }
                    }
                });

                JScrollPane scrollPane = new JScrollPane(panel);
                dialog.add(scrollPane);
                dialog.setSize(imageList.getWidth(), imageList.getHeight());
                dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "No images found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        //Exit
        exit.addActionListener(e4 -> System.exit(0));

        showGrid.addActionListener(e -> {
            ImageLoader imageList = new ImageLoader("images");
            imageList.load();
            images = imageList.getImages();
            JFrame grid = new JFrame("Overview of images - click an image for properties and editing");
            grid.setLayout(new GridLayout(0, 5)); // Adjust the number of columns as needed

            if (!images.isEmpty()) {
                for (Map.Entry<String, Image> entry : images.entrySet()) {
                    // Resize the image to fit within the JLabel
                    ImageIcon imageIcon = new ImageIcon(entry.getValue().getScaledInstance(entry.getValue().getWidth(null)/10, entry.getValue().getHeight(null)/10, Image.SCALE_SMOOTH)); // Adjust the width and height as needed
                    JLabel imgLabel = new JLabel(imageIcon);
                    imgLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the image within the label
                    imgLabel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            imageList.displayImageProperties(entry.getKey());
                        }
                    });
                    grid.add(imgLabel);
                }
                // Set JFrame properties outside the loop
                grid.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                grid.pack(); // Adjust the size of the JFrame to fit the components
                grid.setLocationRelativeTo(null); // Center the JFrame on the screen
                grid.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "No images found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Gallery gallery = new Gallery();
            gallery.init();
        });
    }
}
