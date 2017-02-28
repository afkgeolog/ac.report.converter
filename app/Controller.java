import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;

/**
 * Created by Vladyslav Dovhopol on 2/28/17.
 */
public class Controller extends Application {

    public void start(Stage primaryStage) throws Exception {
        readReport();

        Platform.exit();
    }

    /**
     * @return Excel file that has been read from file system. May be NULL.
     */
    public File readReport() {
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Excel files", "*.xls", "*.xlsx");

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.setTitle("Choose report");

        Window window = new Stage();
        window.centerOnScreen();

        return fileChooser.showOpenDialog(window);
    }
}
