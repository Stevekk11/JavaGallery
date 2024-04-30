package Editors;

import Loaders.DirectoryChooser;
import Loaders.SetImageSize;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 * This class is used to change the brightness of the image
 */
public class ChangeBrightnessStrategy implements ImageEditStrategy {

    private DirectoryChooser fileChooser;
    private BufferedImage changedImg;

    /**
     * This method edits the image using Graphics2D
     *
     * @param images a HashMap of images
     * @param index  the current image
     * @param label  the image label
     */
    @Override
    public void editImage(Map<String, Image> images, int index, JLabel label) {
        JFrame frame = new JFrame("Change brightness");
        JSlider slider = new JSlider(JSlider.HORIZONTAL, -100, 100, 0);
        JLabel valueLabel = new JLabel("Brightness: " + slider.getValue());
        JButton saveButton = new JButton("Save");
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();

        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.setMajorTickSpacing(50);
        slider.setMinorTickSpacing(20);

        labelTable.put(-100, new JLabel("-100"));
        labelTable.put(-50, new JLabel("-50"));
        labelTable.put(0, new JLabel("0"));
        labelTable.put(50, new JLabel("50"));
        labelTable.put(100, new JLabel("100"));
        slider.setLabelTable(labelTable);

        frame.add(slider, BorderLayout.CENTER);
        frame.add(valueLabel, BorderLayout.SOUTH);
        frame.add(saveButton, BorderLayout.NORTH);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 150);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        // Add the ActionListener to the saveButton here
        saveButton.addActionListener(e -> {
            fileChooser = new DirectoryChooser("Select where to save the image", true);
            File fileToSave = fileChooser.getSelectedFile();
            try {
                ImageIO.write(changedImg, "png", fileToSave);
                JOptionPane.showMessageDialog(frame, "Image saved successfully!");
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error saving image: " + ex.getMessage());
            }
        });

        slider.addChangeListener(e -> {
            int value = slider.getValue();
            valueLabel.setText("Brightness: " + value);

            if (!slider.getValueIsAdjusting() && index < images.size() && index >= 0) {
                int i = 0;
                for (Map.Entry<String, Image> entry : images.entrySet()) {
                    if (i == index) {
                        Image image = SetImageSize.setImageSize(entry.getValue().getWidth(null),entry.getValue().getHeight(null),entry.getValue());
                        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
                        Graphics2D graphics = bufferedImage.createGraphics();
                        graphics.drawImage(image, 0, 0, null);
                        graphics.dispose();

                        float scaleFactor = 1 + (value / 100f);
                        RescaleOp rescaleOp = new RescaleOp(scaleFactor, 0, null);
                        changedImg = rescaleOp.filter(bufferedImage, null);

                        label.setIcon(new ImageIcon(changedImg));
                        label.repaint();
                        break;
                    }
                    i++;
                }
            }
        });
    }
}

