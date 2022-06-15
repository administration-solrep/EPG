package fr.dila.solonepg.core.export;

import fr.dila.ss.core.util.SSExcelUtil;
import fr.dila.st.core.export.AbstractExportConfig;
import java.util.Arrays;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.nuxeo.ecm.core.api.CoreSession;

public class MgppExportConfig extends AbstractExportConfig<List<String>> {

    public MgppExportConfig(List<List<String>> items, String sheetName, String[] headers, boolean isPdf) {
        super(items, sheetName, headers, getColWidths(headers), true, isPdf);
    }

    private static Double[] getColWidths(String[] headers) {
        double width = 1d / headers.length;
        Double[] colWidths = new Double[headers.length];
        Arrays.fill(colWidths, width);
        return colWidths;
    }

    @Override
    protected String[] getDataCells(CoreSession session, List<String> item) {
        return item.toArray(new String[0]);
    }

    @Override
    protected void formatWorkbookStyle(HSSFWorkbook workbook) {
        super.formatWorkbookStyle(workbook);
        setCellBorderStyle(workbook.getSheet(getSheetNameLabel()).getRow(0));
    }

    @Override
    protected void formatRowStyle(HSSFSheet sheet, HSSFRow row) {
        super.formatRowStyle(sheet, row);
        setCellBorderStyle(row);
    }

    private static void setCellBorderStyle(HSSFRow row) {
        row.forEach(cell -> SSExcelUtil.setBorder(cell, BorderStyle.THIN, IndexedColors.BLACK.getIndex()));
    }
}
