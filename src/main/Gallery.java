package main;

import Loaders.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class Gallery extends JFrame {
    protected HashMap<String,Image> images;
    protected JButton load;
    protected JButton exit;
    protected JButton next;
    protected JButton previous;
    protected JButton showGrid;
    protected JLabel label;
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
        label = new JLabel("main.Gallery", SwingConstants.CENTER);
    }

    /**
     * This method initialises the components
     */

    public void init(){
        setTitle("Image Gallery");
        setSize(400,400);
        JPanel buttonPanel = new JPanel(new GridLayout(3,2));

        JPanel labelPanel = new JPanel(new BorderLayout());

        labelPanel.add(label, BorderLayout.CENTER);
        add(labelPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

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
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageLoader imageList = new ImageLoader("images");
                imageList.load();
                images = imageList.getImages();
                if (!images.isEmpty()) {
                    JFrame dialog = new JFrame();
                    JPanel panel = new JPanel(new GridLayout(10, 10));
                    for (Map.Entry<String, Image> entry : images.entrySet()) {
                        Image image = entry.getValue();
                        String filename = entry.getKey();
                        ImageIcon imageIcon = new ImageIcon(image.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                        JLabel imgLabel = new JLabel(imageIcon);
                        panel.add(imgLabel);
                        JLabel filenameLabel = new JLabel(filename);
                        panel.add(filenameLabel);
                    }
                    JScrollPane scrollPane = new JScrollPane(panel);
                    dialog.add(scrollPane);
                    dialog.setSize(800, 600);
                    dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    dialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "No images found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public static void main(String[] args) {
        Gallery g = new Gallery();
        g.init();
    }
}
