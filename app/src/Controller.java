package src;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.apache.commons.lang.exception.ExceptionUtils;
import src.reader.ReportParser;
import src.reader.ReportReader;

import java.io.File;
import java.util.logging.Logger;

/**
 * Created by Vladyslav Dovhopol on 2/28/17.
 */
public class Controller extends Application {

    private static LoggingConfigs LOGGING_CONFIGS = LoggingConfigs.INSTANCE;

    public void start(Stage stage) {
        try {
            File xlsReport = new ReportReader().read();
            new ReportParser().parse(xlsReport);
            //Delete log files if no exception has occured.
            LOGGING_CONFIGS.deleteLogFilesOnExit();
        } catch (Exception e) {
            String stackTrace = ExceptionUtils.getFullStackTrace(e);
            Logger.getGlobal().severe(stackTrace);
            showErrorDialog();
        }
        Platform.exit();
    }

    private void showErrorDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("See logs for details.");

        alert.showAndWait();
    }
}
