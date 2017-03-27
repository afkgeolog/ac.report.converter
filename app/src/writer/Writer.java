package src.writer;

import src.domain.Employee;
import src.domain.MonthReport;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by Vladyslav Dovhopol on 3/8/17.
 */
public class Writer {

    /**
     * 1. Create a directory "Reports" if it doesn't exist
     * 2. Loop through each employee
     * 3. Create a directory from employee name if doesn't exist
     * 4. Write each month report in .xls format replacing existing reports.
     *    Name of each report is - year_month_employeeName.xls
     */
    public void write(Map<Employee, List<MonthReport>> reportsMap) throws FileSystemException {
        File outputDirectory = new OutputDirectory().choose();
        if (outputDirectory == null) {
            Logger.getGlobal().info("Output directory hasn't been choosed.");
            return;
        }
        MonthReportWriter writer = new MonthReportWriter();
        File reportsDirectory = createReportsDirectory(outputDirectory);
        for(Employee employee : reportsMap.keySet()) {
            File employeeDirectory = createEmployeeDirectory(reportsDirectory, employee);
            //Don't set to parallel. Logging won't work correct.
            reportsMap.get(employee).stream().forEach(monthReport -> {
                        try {
                            writer.write(employeeDirectory, monthReport);
                        } catch (IOException e) {
                            String msg = "Failed to write " + WriterUtils.convertToFileName(monthReport);
                            Logger.getGlobal().severe(msg);
                        }
            });
        }
    }

    private File createReportsDirectory(File outputDirectory) throws FileSystemException {
        File reportsDirectory = new File(outputDirectory, "Reports");
        return createDirectoryIfNotExist(reportsDirectory);
    }

    private File createEmployeeDirectory(File reportsDirectory, Employee employee) throws FileSystemException {
        File employeeDirectory = new File(reportsDirectory, WriterUtils.formatName(employee));
        return createDirectoryIfNotExist(employeeDirectory);
    }

    private File createDirectoryIfNotExist(File file) throws FileSystemException {
        if (file.exists() && file.isDirectory()) {
            return file;
        }
        if (!file.mkdir()) {
            throw new FileSystemException("Reports directory couldn't be created.");
        }
        return file;
    }
}
