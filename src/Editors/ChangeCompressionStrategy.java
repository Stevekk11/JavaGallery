package Editors;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * This class is used to change the compression of the image
 */
public class ChangeCompressionStrategy extends JFrame implements ImageEditStrategy {
    private BufferedImage compressedImage;

    /**
     *
     * @param images a HashMap of images
     * @param index the current image
     * @param label the image label
     */
    @Override
    public void editImage(Map<String, Image> images, int index, JLabel label) {
        setTitle("Change Compression - for JPEG images only");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        JSlider slider = new JSlider(1, 100, 80);
        JButton save = new JButton("Save");
        JLabel currentVal = new JLabel("Current value: " + slider.getValue() + "%");
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        panel.add(slider, BorderLayout.CENTER);
        panel.add(save, BorderLayout.NORTH);
        panel.add(currentVal, BorderLayout.SOUTH);
        add(panel, BorderLayout.CENTER);
        setVisible(true);

        slider.addChangeListener(e -> {
            currentVal.setText("Current value: " + slider.getValue() + "%");
            if (!slider.getValueIsAdjusting() && index < images.size() && index >= 0) {
                int i = 0;
                for (Map.Entry<String, Image> entry : images.entrySet()) {
                    if (i == index) {
                        // Adjust the compression level of the JPEG image
                        float compressionQuality = slider.getValue() / 100f;

                        // Calculate scaled dimensions
                        int scaledWidth = (int) (entry.getValue().getWidth(null) * compressionQuality);
                        int scaledHeight = (int) (entry.getValue().getHeight(null) * compressionQuality);

                        // Scale the image dynamically
                        Image scaledImage = entry.getValue().getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);

                        // Create BufferedImage with scaled dimensions
                        BufferedImage bufferedImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
                        Graphics2D graphics = bufferedImage.createGraphics();
                        graphics.drawImage(scaledImage, 0, 0, null);
                        graphics.dispose();

                        try {
                            // Get a ImageWriter for JPEG format
                            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
                            if (!writers.hasNext()) throw new IllegalStateException("No writers found");
                            ImageWriter writer = writers.next();

                            // Create the ImageWriteParam to compress the image
                            ImageWriteParam param = writer.getDefaultWriteParam();
                            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                            param.setCompressionQuality(compressionQuality);

                            // Use a ByteArrayOutputStream to get the image as a byte array
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
                            writer.setOutput(ios);
                            writer.write(null, new IIOImage(bufferedImage, null, null), param);
                            ios.flush();

                            // Update the image map with the compressed image
                            byte[] compressedImageBytes = baos.toByteArray();
                            ByteArrayInputStream bais = new ByteArrayInputStream(compressedImageBytes);
                            compressedImage = ImageIO.read(bais);
                            // Update the label icon to show the new image
                            label.setIcon(new ImageIcon(compressedImage));
                            label.repaint();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        break; // Exit the loop after processing the selected image
                    }
                    i++;
                }
            }
        });

        save.addActionListener(e -> {
            if (compressedImage != null) {
                // Open a JFileChooser to select where to save the image
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Specify a file to save");

                int userSelection = fileChooser.showSaveDialog(this);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    // Ensure the file has the correct extension
                    if (!fileToSave.getName().toLowerCase().endsWith(".jpg")) {
                        fileToSave = new File(fileToSave + ".jpg");
                    }

                    try {
                        // Save the compressed image to the selected file
                        ImageIO.write(compressedImage, "jpg", fileToSave);
                        JOptionPane.showMessageDialog(this, "Image saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "No compressed image available.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
