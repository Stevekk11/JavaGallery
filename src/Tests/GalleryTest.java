package Tests;

import main.Gallery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class GalleryTest {
    Gallery gallery;

    @BeforeEach
    void setUp() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        gallery = new Gallery();
    }

    @Test
    void testInvoke(){
        assertNotNull(gallery);
        assertNotNull(getGridFrame());
        JPanel label = (JPanel) getGridFrame().getContentPane().getComponent(0);
        Component[] components = label.getComponents();
        int buttons = countButtons(components);
        assertEquals(9,buttons);
        assertEquals("Image Gallery",gallery.getTitle());
        assertEquals("Previous", ((JButton) components[3]).getText());
        assertNotNull(((JButton) components[3]).getActionListeners());
    }

    private JFrame getGridFrame() {
        for (Frame frame : Frame.getFrames()) {
            if (frame instanceof JFrame && frame.getTitle().equals("Image Gallery")) {
                return (JFrame) frame;
            }
        }
        return null;
    }

    private int countButtons(Component[] components) {
        int buttonCount = 0;
        for (Component component : components) {
            if (component instanceof JButton) {
                buttonCount++;
            }
        }
        return buttonCount;
    }
}