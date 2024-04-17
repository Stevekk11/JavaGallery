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
        frame.setIconImage(new ImageIcon("icons/photo.png").getImage());
        panel = new JPanel();
        zoom = new JButton("Zoom");
        showInBlackAndWhite = new JButton("Show in Black and White");
        showInColor = new JButton("Show in Color");
        changeSize = new JButton("Change Size");
        changeContrast = new JButton("Change Contrast");
        changeBrightness = new JButton("Change Brightness");
        panel.setLayout(new GridLayout(3, 2));
        panel.add(zoom);
        zoom.setFont(new Font("Arial", Font.BOLD, 20));
        zoom.setIcon(new ImageIcon("icons/zoom.png"));
        panel.add(showInBlackAndWhite);
        showInBlackAndWhite.setFont(new Font("Arial", Font.BOLD, 20));
        showInBlackAndWhite.setIcon(new ImageIcon("icons/black.png"));
        panel.add(showInColor);
        showInColor.setFont(new Font("Arial", Font.BOLD, 20));
        showInColor.setIcon(new ImageIcon("icons/color.png"));
        panel.add(changeSize);
        changeSize.setFont(new Font("Arial", Font.BOLD, 20));
        changeSize.setIcon(new ImageIcon("icons/resize.png"));
        panel.add(changeContrast);
        changeContrast.setFont(new Font("Arial", Font.BOLD, 20));
        changeContrast.setIcon(new ImageIcon("icons/contrast.png"));
        panel.add(changeBrightness);
        changeBrightness.setFont(new Font("Arial", Font.BOLD, 20));
        changeBrightness.setIcon(new ImageIcon("icons/brightness.png"));
        frame.add(panel);

        frame.setSize(800, 500);
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
        changeBrightness.addActionListener(e -> {
            ChangeBrightnessStrategy strategy = new ChangeBrightnessStrategy();
            strategy.editImage(images, index, label);
        });
        showInBlackAndWhite.addActionListener(e -> {
            GrayScaleStrategy strategy = new GrayScaleStrategy();
            strategy.editImage(images, index, label);
        });
    }
}
