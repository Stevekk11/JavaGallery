import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class Gallery extends JFrame {
    protected HashMap<Image,String> images;
    protected JButton load;
    protected JButton exit;
    protected JButton next;
    protected JButton previous;
    protected JButton showGrid;
    protected JLabel label;
    protected JButton delete;

    public Gallery() {
        images = new HashMap<>();
        load = new JButton("Load");
        exit = new JButton("Exit");
        next = new JButton("Next");
        previous = new JButton("Previous");
        showGrid = new JButton("Show Grid");
        delete = new JButton("Delete");
        label = new JLabel();

        setTitle("Gallery");
        setSize(400,400);
        JPanel buttonPanel = new JPanel(new GridLayout(3,2));

        JPanel labelPanel = new JPanel(new BorderLayout());
        label = new JLabel("Gallery", SwingConstants.CENTER);
        labelPanel.add(label, BorderLayout.CENTER);
        add(labelPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        buttonPanel.add(load);
        buttonPanel.add(exit);
        buttonPanel.add(next);
        buttonPanel.add(previous);
        buttonPanel.add(showGrid);
        buttonPanel.add(delete);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        Gallery g = new Gallery();
    }
}
