package main;

import Editors.ImageEditor;
import Loaders.DirectoryChooser;
import Loaders.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The Gallery class represents an image gallery application.
 * It allows users to load, view, edit, delete, and navigate through images.
 */
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
    private boolean isEditorOpen = false;
    private DirectoryChooser chooser;

    /**
     * Constructs a new Gallery object.
     * Initializes the frame and sets up the UI components.
     *
     * @throws UnsupportedLookAndFeelException If the system look and feel is not supported.
     * @throws ClassNotFoundException          If the specified class for look and feel is not found.
     * @throws InstantiationException          If the specified class for look and feel cannot be instantiated.
     * @throws IllegalAccessException          If the specified class for look and feel cannot be accessed.
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
        pack = new JButton("<html>Pack or change image<br> directory if you're on the first img</html>");
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
        delete.setBorder(BorderFactory.createLineBorder(Color.RED, 3, true));
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
        //directory with images chooser
        chooser = new DirectoryChooser("Choose a folder with images");
        path = chooser.chooseDirectory();
    }

    /**
     * This method is used to initialize the components and to load the images and buttons
     */
    private void showImages() {
        JLabel imgLabel = new JLabel();
        JPanel imagePanel = new JPanel(new BorderLayout());

        load.addActionListener(e -> {
            imageList = new ImageLoader(path);
            //load the images from folder
            imageList.load();
            images = imageList.getImages();
            //check if the folder is empty
            if (!images.isEmpty()) {
                dialog = new JFrame();
                dialog.setIconImage(new ImageIcon("icons/photo.png").getImage());
                imagePanel.add(imgLabel, BorderLayout.CENTER);
                imageList.displayImage(images, currentIndex.get(), imgLabel);
                JScrollPane scrollPane = new JScrollPane(imagePanel);
                dialog.add(scrollPane);
                dialog.pack();
                dialog.setDefaultCloseOperation(EXIT_ON_CLOSE);
                dialog.setVisible(true);
                dialog.setTitle("Image: " + currentIndex);
            } else {
                JOptionPane.showMessageDialog(null, "No images found.", "Error", JOptionPane.ERROR_MESSAGE);
                chooser.chooseDirectory();
            }
        });

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
                    File fileToDelete = new File(path + "/" + filenameToDelete);
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
            if (!images.isEmpty() && !isEditorOpen) {
                SwingUtilities.invokeLater(() -> {
                    next.setEnabled(false);
                    previous.setEnabled(false);
                    isEditorOpen = true; // Set the flag to true as the editor is being opened
                    ImageEditor editor = new ImageEditor();
                    editor.setTitle("Image Editor - editing image: " + currentIndex);
                    editor.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            next.setEnabled(true);
                            previous.setEnabled(true);
                            isEditorOpen = false; // Reset the flag as the editor is closed
                        }
                    });
                    editor.editImage(images, currentIndex.get(), imgLabel);
                });
            } else if (images.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No images to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (isEditorOpen) {
                JOptionPane.showMessageDialog(null, "Image editor is already open.", "Warning", JOptionPane.WARNING_MESSAGE);
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
        showGrid.addActionListener(e -> imageList.showGrid(images, imageList));
        pack.addActionListener(e -> {if (currentIndex.get() == 0) {path = chooser.chooseDirectory(); dialog.dispose();} else dialog.pack();});
    }
}
