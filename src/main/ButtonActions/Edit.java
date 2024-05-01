package main.ButtonActions;

import Editors.ImageEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Edit {

    private static boolean isEditorOpen;

    private Edit() {
    }

    public static void edit(HashMap<String, Image> images, JButton next, JButton previous, AtomicInteger currentIndex, JLabel imgLabel) {
        if (!images.isEmpty() && !isEditorOpen) {
            SwingUtilities.invokeLater(() -> {
                next.setEnabled(false);
                previous.setEnabled(false);
                isEditorOpen = true; // Set the flag to true as the editor is being opened
                //open the image editor
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
    }
    public static void setIsEditorOpen(boolean isOpen) {
        isEditorOpen = isOpen;
    }
}
