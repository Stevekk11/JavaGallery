package main;
import Editors.ImageEditor;
import Loaders.ImageLoader;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

//all the needed components
public class Gallery extends JFrame {
    protected HashMap<String, Image> images;
    protected JButton load;
    protected JButton exit;
    protected JButton next;
    protected JButton previous;
    protected JButton showGrid;
    protected JButton delete;
    protected JButton properties;
    protected JButton edit;
    protected JButton pack;
    protected AtomicInteger currentIndex;
    protected JFrame dialog;
    protected String path;
    protected ImageLoader imageList;

    /**
     * This constructor is used for initialising the Frame
     */
    public Gallery() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        images = new HashMap<>();
        load = new JButton("Load");
        exit = new JButton("Exit");
        next = new JButton("Next");
        previous = new JButton("Previous");
        showGrid = new JButton("Show Grid");
        delete = new JButton("Delete image");
        edit = new JButton("Edit");
        properties = new JButton("Properties");
        currentIndex = new AtomicInteger(0);
        pack = new JButton("Pack");
        init();
        showImages();
    }
    /**
     * Helper method
     */
    private void init() {
        setTitle("Image Gallery");
        setSize(750, 470);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setIconImage(new ImageIcon("icons/photo.png").getImage());
        //The panel which will have the buttons
        JPanel buttonPanel = new JPanel(new GridLayout(3, 3));
        add(buttonPanel, BorderLayout.CENTER);
        //add all things
        buttonPanel.add(load);
        load.setFont(new Font("Arial", Font.BOLD, 20));
        load.setIcon(new ImageIcon("icons/load.png"));
        load.setHorizontalTextPosition(SwingConstants.CENTER);

        buttonPanel.add(exit);
        exit.setIcon(new ImageIcon("icons/exit.png"));
        exit.setFont(new Font("Arial", Font.BOLD, 20));

        buttonPanel.add(next);
        next.setFont(new Font("Arial", Font.BOLD, 20));
        next.setIcon(new ImageIcon("icons/next.png"));
        next.setVerticalTextPosition(SwingConstants.TOP);
        next.setHorizontalTextPosition(SwingConstants.CENTER);

        buttonPanel.add(previous);
        previous.setFont(new Font("Arial", Font.BOLD, 20));
        previous.setIcon(new ImageIcon("icons/prev.png"));
        previous.setVerticalTextPosition(SwingConstants.TOP);
        previous.setHorizontalTextPosition(SwingConstants.CENTER);

        buttonPanel.add(showGrid);
        showGrid.setFont(new Font("Arial", Font.BOLD, 20));

        buttonPanel.add(delete);
        delete.setBorder(BorderFactory.createLineBorder(Color.RED,3,true));
        delete.setIcon(new ImageIcon("icons/delete.png"));
        delete.setVerticalTextPosition(SwingConstants.TOP);
        delete.setHorizontalTextPosition(SwingConstants.CENTER);
        delete.setFont(new Font("Arial", Font.BOLD, 13));

        buttonPanel.add(edit);
        edit.setFont(new Font("Arial", Font.BOLD, 20));
        edit.setIcon(new ImageIcon("icons/edit.png"));

        buttonPanel.add(properties);
        properties.setFont(new Font("Arial", Font.BOLD, 20));
        properties.setIcon(new ImageIcon("icons/properties.png"));

        buttonPanel.add(pack);
        pack.setFont(new Font("Arial", Font.BOLD, 20));
        pack.setIcon(new ImageIcon("icons/pack.png"));

        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = chooser.getSelectedFile();
            path = selectedDirectory.getAbsolutePath();
            imageList = new ImageLoader(path);
        } else System.exit(0);
    }
    /**
     * This method is used to initialize the components and to load the images and buttons
     */
    private void showImages() {
        load.addActionListener(e -> {
            //load the images form folder
            imageList.load();
            images = imageList.getImages();
            //check if the folder is empty
            if (!images.isEmpty()) {
                dialog = new JFrame();
                dialog.setIconImage(new ImageIcon("icons/photo.png").getImage());
                JPanel imagePanel = new JPanel(new BorderLayout());
                JLabel imgLabel = new JLabel();
                imagePanel.add(imgLabel, BorderLayout.CENTER);
                imageList.displayImage(images, currentIndex.get(), imgLabel);
                //previous image
                previous.addActionListener(e1 -> {
                    if (currentIndex.get() > 0) {
                        currentIndex.decrementAndGet();
                        imageList.displayImage(images, currentIndex.get(), imgLabel);
                        dialog.pack();
                        dialog.setTitle("Image: " + currentIndex);
                    }
                });
                //next image
                next.addActionListener(e1 -> {
                    if (currentIndex.get() < images.size() - 1) {
                        currentIndex.incrementAndGet();
                        imageList.displayImage(images, currentIndex.get(), imgLabel);
                        dialog.pack();
                        dialog.setTitle("Image: " + currentIndex);
                    }
                });
                //delete the image
                delete.addActionListener(e1 -> {
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
                            dialog.pack();
                        }
                    }
                });
                //edit the image
                edit.addActionListener(e1 -> {
                    SwingUtilities.invokeLater(() -> {
                        ImageEditor editor = new ImageEditor();
                        editor.editImage(images, currentIndex.get(), imgLabel);
                    });
                });

                JScrollPane scrollPane = new JScrollPane(imagePanel);
                dialog.add(scrollPane);
                dialog.pack();
                dialog.setDefaultCloseOperation(EXIT_ON_CLOSE);
                dialog.setVisible(true);
                dialog.setTitle("Image: " + currentIndex);
            } else {
                JOptionPane.showMessageDialog(null, "No images found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        //display the properties such as filename, size
        properties.addActionListener(e -> {
            String filename;
            int index = currentIndex.get();
            if (index >= 0) {
                int i = 0;
                for (Map.Entry<String, Image> entry : images.entrySet()) {
                    if (i == index) {
                        filename = entry.getKey();
                        imageList.displayImageProperties(filename);
                        break;
                    }
                    i++;
                }
            }
        });
        //Exit
        exit.addActionListener(e -> System.exit(0));
        //show grid of images
        showGrid.addActionListener(e -> {
            ImageLoader imageList = new ImageLoader("images");
            imageList.load();
            images = imageList.getImages();
            JFrame grid = new JFrame("Overview of images - click an image for properties");
            grid.setLayout(new GridLayout(0, 5)); // Adjust the number of columns as needed
            grid.setIconImage(new ImageIcon("icons/photo.png").getImage());

            if (!images.isEmpty()) {
                for (Map.Entry<String, Image> entry : images.entrySet()) {
                    // Resize the image to fit within the JLabel
                    ImageIcon imageIcon = new ImageIcon(entry.getValue().getScaledInstance(entry.getValue().getWidth(null) / 10, entry.getValue().getHeight(null) / 10, Image.SCALE_SMOOTH)); // Adjust the width and height as needed
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

                grid.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                grid.pack(); // Adjust the size of the JFrame to fit the components
                grid.setLocationRelativeTo(null); // Center the JFrame on the screen
                grid.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "No images found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        pack.addActionListener(e -> dialog.pack());//pack the image if user needs
    }
}