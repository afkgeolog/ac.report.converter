package src;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.apache.commons.lang.exception.ExceptionUtils;
import src.domain.Employee;
import src.domain.MonthReport;
import src.reader.InputReportDto;
import src.reader.ReportParser;
import src.reader.ReportReader;
import src.writer.Writer;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by Vladyslav Dovhopol on 2/28/17.
 */
public class Controller extends Application {

    private static LoggingConfigs LOGGING_CONFIGS = LoggingConfigs.INSTANCE;

    public void start(Stage stage) {
        try {
            File xlsReport = new ReportReader().read();
            if (xlsReport == null) {
                //File hasn't been selected.
                Platform.exit();
                return;
            }
            InputReportDto inputReportDto = new ReportParser().parse(xlsReport);
            Map<Employee, List<MonthReport>> reports = ReportsCompiler.compile(inputReportDto);
            new Writer().write(reports);
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
