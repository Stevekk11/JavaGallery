package main;

import Loaders.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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
    }

    /**
     * This method initialises the components
     */

    public void init(){
        setTitle("Image Gallery");
        setSize(400,400);
        JPanel buttonPanel = new JPanel(new GridLayout(3,2));
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
                imageList.displayImage(images,currentIndex.get(),imgLabel);

                previous.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(currentIndex.get() > 0){
                            currentIndex.decrementAndGet();
                            imageList.displayImage(images,currentIndex.get(),imgLabel);
                        }
                    }
                });

                next.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(currentIndex.get() < images.size()-1){
                            currentIndex.incrementAndGet();
                            imageList.displayImage(images,currentIndex.get(),imgLabel);
                        }
                    }
                });
                JScrollPane scrollPane = new JScrollPane(panel);
                dialog.add(scrollPane);
                dialog.setSize(800,600);
                dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "No images found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public static void main(String[] args) {
        Gallery g = new Gallery();
        g.init();
    }
}
