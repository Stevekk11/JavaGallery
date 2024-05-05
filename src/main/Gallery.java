package main;
import Loaders.DirectoryChooser;
import Loaders.ImageLoader;
import main.ButtonActions.Delete;
import main.ButtonActions.Edit;

import javax.swing.*;
import java.awt.*;
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
        Edit.setIsEditorOpen(isEditorOpen);
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
        JScrollPane scrollPane = new JScrollPane(imagePanel);

        load.addActionListener(e -> {
            imageList = new ImageLoader(path);
            //load the images from folder
            if (path != null) {
                imageList.load();
                images = imageList.getImages();
            }
            //check if the folder is empty
            if (!images.isEmpty()) {
                dialog = new JFrame();
                dialog.setIconImage(new ImageIcon("icons/photo.png").getImage());
                imagePanel.add(imgLabel, BorderLayout.CENTER);
                imageList.displayImage(images, currentIndex.get(), imgLabel);
                dialog.add(scrollPane);
                dialog.pack();
                dialog.setDefaultCloseOperation(EXIT_ON_CLOSE);
                dialog.setVisible(true);
                dialog.setTitle("Image: " + currentIndex);
            } else {
                JOptionPane.showMessageDialog(null, "No images found.", "Error", JOptionPane.ERROR_MESSAGE);
                path = chooser.chooseDirectory();
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
        delete.addActionListener(e1 -> Delete.deleteImage(currentIndex,images,imgLabel,imageList,dialog,path));

        //edit the image
        edit.addActionListener(e1 -> Edit.edit(images,next,previous,currentIndex,imgLabel));

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
            if (imageList != null && path != null) {imageList.showGrid(imageList);}});
        //packs or chooses a directory
        pack.addActionListener(e -> {
            if (currentIndex.get() == 0) {path = chooser.chooseDirectory();
                if (dialog != null) {dialog.dispose();}} else dialog.pack();});
    }
}
