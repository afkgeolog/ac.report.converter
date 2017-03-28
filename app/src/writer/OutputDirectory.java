package src.writer;

import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;

/**
 * Created by Vladyslav Dovhopol on 3/27/17.
 */
class OutputDirectory {

    /**
     * Choose directory where all reports will be saved.
     * @return Directory. May be NULL if no file has been chosen.
     */
    public File choose() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose directory where to save reports");

        Window window = new Stage();

        window.centerOnScreen();

        return directoryChooser.showDialog(window);
    }
}
