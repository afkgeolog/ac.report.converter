package src.writer;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellUtil;

/**
 * Created by Vladyslav Dovhopol on 3/17/17.
 */
abstract class RowWriter {

    protected final Row row;

    protected RowWriter(Row row) {
        this.row = row;
    }

    protected enum Cells {
        PERIOD,
        ARRIVAL_TIME,
        LEAVING_TIME,
        TIME_AT_WORK,
        TIME_IN_TELEPORTS,
        TELEPORTS_ON_ARRIVAL,
        TELEPORTS_ON_LEAVING
    }

    protected Cell write(Cells cellsEnum, String value) {
        Cell cell = row.createCell(cellsEnum.ordinal());
        cell.setCellValue(value);
        return cell;
    }

    protected void write(Cells cellsEnum, String value, CellStyle cellStyle) {
        Cell cell = write(cellsEnum, value);
        cell.setCellStyle(cellStyle);
    }

    protected void applyFont() {
        Font bold_12 = Fonts.create_BOLD_12(row.getSheet().getWorkbook());
        for (Cell cell : row) {
            CellUtil.setFont(cell, bold_12);
        }
    }
}
