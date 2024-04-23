package Editors;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
/**
 * Implements the ImageEditStrategy interface to provide resizing functionality for images.
 */
public class ChangeSizeStrategy implements ImageEditStrategy {

    private boolean updatingFields = false;
     /**
     * Edits the image by allowing the user to change its size.
     *
     * @param images A map containing image filenames as keys and Image objects as values.
     * @param index  The index of the image to be edited.
     * @param label  The JLabel component where the edited image will be displayed.
     */
    @Override
    public void editImage(Map<String, Image> images, int index, JLabel label) {
        JFrame editFrame = new JFrame("Change Size of Image");
        editFrame.setLayout(new GridLayout(2,1));
        JLabel l = new JLabel();
        l.setHorizontalAlignment(JLabel.CENTER);
        l.setVerticalAlignment(JLabel.CENTER);
        l.setIcon(new ImageIcon("icons/linked.png"));

        JPanel panel = new JPanel(new GridLayout(1, 2));
        JPanel icons = new JPanel(new GridLayout(1, 3));
        JTextField widthField = new JTextField();
        JTextField heightField = new JTextField();
        JTextField widthLabel = new JTextField("Width:");
        JTextField heightLabel = new JTextField("Height:");

        icons.add(widthLabel);
        icons.add(l);
        icons.add(heightLabel);
        widthLabel.setEditable(false);
        heightLabel.setEditable(false);
        widthLabel.setBorder(null);
        heightLabel.setBorder(null);
        widthLabel.setHorizontalAlignment(JTextField.CENTER);
        heightLabel.setHorizontalAlignment(JTextField.CENTER);

        panel.add(widthField);
        panel.add(heightField);
        editFrame.add(icons);
        editFrame.add(panel);
        editFrame.setSize(300, 150);
        editFrame.setVisible(true);
        editFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editFrame.setLocationRelativeTo(null);

        widthField.setFont(new Font("Arial", Font.PLAIN, 20));
        heightField.setFont(new Font("Arial", Font.PLAIN, 20));

        final Image originalImage = images.values().stream().skip(index).findFirst().orElse(null);
        final double aspectRatio = (double) originalImage.getWidth(null) / originalImage.getHeight(null);
        widthField.setText(originalImage.getWidth(null) + "");
        heightField.setText(originalImage.getHeight(null) + "");

        DocumentListener sizeChangeListener = new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                updateSize();
                widthField.setBorder(null);
                heightField.setBorder(null);
            }

            public void removeUpdate(DocumentEvent e) {
                updateSize();
                widthField.setBorder(null);
                heightField.setBorder(null);
            }

            public void insertUpdate(DocumentEvent e) {
                updateSize();
                widthField.setBorder(null);
                heightField.setBorder(null);
            }

            private void updateSize() {
                if (updatingFields) return;

                SwingUtilities.invokeLater(() -> {
                    try {
                        updatingFields = true;
                        int newWidth = Integer.parseInt(widthField.getText());
                        int newHeight = Integer.parseInt(heightField.getText());
                        if (widthField.isFocusOwner()) {
                            newHeight = (int) (newWidth / aspectRatio);
                            heightField.setText(String.valueOf(newHeight));
                        } else if (heightField.isFocusOwner()) {
                            newWidth = (int) (newHeight * aspectRatio);
                            widthField.setText(String.valueOf(newWidth));
                        }
                        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
                        resizedImage.getGraphics().drawImage(originalImage, 0, 0, newWidth, newHeight, null);
                        label.setIcon(new ImageIcon(resizedImage));
                    } catch (IllegalArgumentException | OutOfMemoryError | NegativeArraySizeException ex) {
                        widthField.setBorder(BorderFactory.createLineBorder(Color.RED, 3, true));
                        heightField.setBorder(BorderFactory.createLineBorder(Color.RED, 3, true));
                    } finally {
                        updatingFields = false;
                    }
                });
            }
        };

        widthField.getDocument().addDocumentListener(sizeChangeListener);
        heightField.getDocument().addDocumentListener(sizeChangeListener);
    }
}
