package Editors;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ImageEditor extends JFrame implements ImageEditStrategy{
    protected JPanel panel;
    protected JButton zoom;
    protected JButton showInBlackAndWhite;
    protected JButton showInColor;
    protected JButton changeSize;
    protected JButton changeContrast;
    protected JButton changeBrightness;
    protected JButton changeCompression;

    public ImageEditor() {
        setIconImage(new ImageIcon("icons/photo.png").getImage());
        panel = new JPanel();
        zoom = new JButton("Zoom");
        showInBlackAndWhite = new JButton("Show in Black and White");
        showInColor = new JButton("Show in Color");
        changeSize = new JButton("Change Size");
        changeContrast = new JButton("Change Contrast");
        changeBrightness = new JButton("Change Brightness");
        changeCompression = new JButton("<html>Change compression -<br>For JPEG images only!</html>");
        panel.setLayout(new GridLayout(4, 2));
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
        changeCompression.setFont(new Font("Arial", Font.BOLD, 20));
        changeCompression.setIcon(new ImageIcon("icons/compression.png"));
        panel.add(changeCompression);
        add(panel);

        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
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
        showInColor.addActionListener(e -> {
            int i = 0;
            for (Map.Entry<String, Image> entry : images.entrySet()) {
                if (i == index){
                    label.setIcon(new ImageIcon(entry.getValue().getScaledInstance(entry.getValue().getWidth(null)/4, entry.getValue().getHeight(null)/4, Image.SCALE_SMOOTH)));
                    break;
                }
                i++;
            }
        });
        changeCompression.addActionListener(e -> {
            ChangeCompressionStrategy strategy = new ChangeCompressionStrategy();
            strategy.editImage(images, index, label);
        });
        changeSize.addActionListener(e -> {
            ChangeSizeStrategy strategy = new ChangeSizeStrategy();
            strategy.editImage(images, index, label);
        });
    }

    public static void main(String[] args) {
        ImageEditor editor = new ImageEditor();
        editor.editImage(null,0,null);
    }
}
