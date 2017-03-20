package src.writer;

import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Created by Vladyslav Dovhopol on 3/18/17.
 */
class Fonts {

    private Fonts() {}

    /**
     * @param workbook workbook for which font is created
     * @return Bold, with specified points in height font.
     */
    public static Font create_BOLD_16(Workbook workbook) {
        return create_BOLD(workbook, (short) 16);
    }

    /**
     * @param workbook workbook for which font is created
     * @return Bold, with specified points in height font.
     */
    public static Font create_BOLD_12(Workbook workbook) {
        return create_BOLD(workbook, (short) 12);
    }

    private static Font create_BOLD(Workbook workbook, short heightInPoints) {
        Font font = workbook.createFont();
        font.setFontHeightInPoints(heightInPoints);
        font.setBold(true);
        return font;
    }
}
