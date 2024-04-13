package Editors;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public interface ImageEditStrategy {
    void editImage(Map<String, Image> images, int index, JLabel label);
}
