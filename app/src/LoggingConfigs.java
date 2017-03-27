package src;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

/**
 * Created by Vladyslav Dovhopol on 3/9/17.
 *
 * Class used only to configure logs.
 * Objects of this class MUST be never be instantiated.
 */
public class LoggingConfigs {

    public final static LoggingConfigs INSTANCE = new LoggingConfigs();

    //If we don't store references to loggers they may be garbage collected.
    private final List<Logger> LOGGERS = new ArrayList<>();

    private final String LOG_FILE_SUFFIX = "_log.txt";

    private LoggingConfigs() {
        Logger logger = Logger.getGlobal();
        logger.setLevel(Level.ALL);

        Handler fileHandler = null;
        try {
            String now = LocalDateTime.now().toString();
            fileHandler = new FileHandler(now + LOG_FILE_SUFFIX);
            fileHandler.setFormatter(new SimpleFormatter());
        } catch (IOException e) {
            throw new RuntimeException("File handler couldn't be instantiated.", e);
        }
        logger.addHandler(fileHandler);
        logger.info("Instantiate logger.");

        LOGGERS.add(logger);
    }

    public void deleteLogFilesOnExit() {
        File rootDirectory = new File(".");
        FilenameFilter logFileFilter = (dir, name) -> name.endsWith(LOG_FILE_SUFFIX);
        File[] logFiles = rootDirectory.listFiles(logFileFilter);
        for (File logFile : logFiles) {
            logFile.deleteOnExit();
        }
    }
}
