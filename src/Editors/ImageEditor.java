package Editors;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ImageEditor extends JFrame implements ImageEditStrategy{
    protected JFrame frame;
    protected JPanel panel;
    protected JButton zoom;
    protected JButton showInBlackAndWhite;
    protected JButton showInColor;
    protected JButton changeSize;
    protected JButton changeContrast;
    protected JButton changeBrightness;

    public ImageEditor() {
        frame = new JFrame("Image Editor");
        panel = new JPanel();
        zoom = new JButton("Zoom");
        showInBlackAndWhite = new JButton("Show in Black and White");
        showInColor = new JButton("Show in Color");
        changeSize = new JButton("Change Size");
        changeContrast = new JButton("Change Contrast");
        changeBrightness = new JButton("Change Brightness");
        panel.setLayout(new GridLayout(3, 2));
        panel.add(zoom);
        panel.add(showInBlackAndWhite);
        panel.add(showInColor);
        panel.add(changeSize);
        panel.add(changeContrast);
        panel.add(changeBrightness);
        frame.setSize(700, 400);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    @Override
    public void editImage(Map<String, Image> images, int index, JLabel label){
        zoom.addActionListener(e -> {
             ZoomStrategy strategy = new ZoomStrategy();
             strategy.editImage(images, index, label);
        });
        changeContrast.addActionListener(e -> {
            ChangeContrastStrategy strategy = new ChangeContrastStrategy();
            strategy.editImage(images, index, label);
        });
    }
}
