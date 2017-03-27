package src.reader;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;

/**
 * Created by Vladyslav Dovhopol on 3/9/17.
 */
public class ReportReader {

    /**
     * @return Excel file that has been parse from file system. May be NULL.
     */
    public File read() {
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Excel files", "*.xls", "*.xlsx");

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.setTitle("Choose report");

        Window window = new Stage();
        window.centerOnScreen();

        return fileChooser.showOpenDialog(window);
    }
}
