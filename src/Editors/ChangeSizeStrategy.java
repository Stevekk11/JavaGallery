package Editors;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ChangeSizeStrategy implements ImageEditStrategy {

    private boolean updatingFields = false;

    @Override
    public void editImage(Map<String, Image> images, int index, JLabel label) {
        JFrame editFrame = new JFrame("Change Size of Image");
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JTextField widthField = new JTextField();
        JTextField heightField = new JTextField();
        JTextField widthLabel = new JTextField("Width:");
        JTextField heightLabel = new JTextField("Height:");
        panel.add(widthLabel);
        panel.add(heightLabel);
        widthLabel.setEditable(false);
        heightLabel.setEditable(false);
        panel.add(widthField);
        panel.add(heightField);
        editFrame.add(panel);
        editFrame.setSize(300, 100);
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