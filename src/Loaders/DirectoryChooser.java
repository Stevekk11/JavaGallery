package Loaders;

import javax.swing.*;
import java.io.File;

public class DirectoryChooser extends JFileChooser {
    public DirectoryChooser(String title) {
        super(title);
        setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        setSelectedFile(new File(System.getProperty("user.home")));
    }
    public DirectoryChooser(String title, boolean saving) {
        super(title);
        if (saving){
            setFileSelectionMode(JFileChooser.FILES_ONLY);
            setSelectedFile(new File("changedImg.png"));
            showSaveDialog(this);
        }
    }
    public String chooseDirectory(){
        int result = showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            return getSelectedFile().getAbsolutePath();
        } else if (result == JFileChooser.CANCEL_OPTION) {
            chooseDirectory();
        }
        return null;
    }
}
