/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nehemie_mutuelle;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import javax.swing.JTable;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 * @author Admin
 */
public class JTableToExcelExporter {

    public static void exportFromTable(JTable table, String filename) {
        exportFromTable(null, table, filename);
    }
    
    public static void exportFromTable(Map<String, Object> params, JTable table, String filename) {

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Sample sheet");

        if (params != null && !params.isEmpty()) {
            writeParams(params, sheet);
        }
        
        writeHeader(table, sheet);
        writeData(table, sheet);

        try {
            FileOutputStream out = new FileOutputStream(new File(filename));
            workbook.write(out);
            out.close();
            System.out.println("Excel written successfully..");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Экспорт в excel"+e.getMessage());
        }
    }

    private static void writeParams(Map<String, Object> params, HSSFSheet sheet) {
        for (String key : params.keySet()) {
            Object value = params.get(key);
            HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
            writeCellValue(key, row.createCell(0), sheet);
            writeCellValue(value, row.createCell(1), sheet);
        }
        sheet.createRow(sheet.getLastRowNum() + 1);
    }
    
    private static void writeData(JTable policyTable, HSSFSheet sheet) {
        for (int i = 0; i < policyTable.getRowCount(); i++) {
            HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
            for (int j = 0; j < policyTable.getColumnCount(); j++) {
                HSSFCell cell = row.createCell(j);
                Object obj = policyTable.getValueAt(i, j);
                writeCellValue(obj, cell, sheet);
            }
        }
    }

    private static void writeHeader(JTable policyTable, HSSFSheet sheet) {
        HSSFRow headerRow = sheet.createRow(sheet.getLastRowNum() + 1);
        for (int j = 0; j < policyTable.getColumnCount(); j++) {
            String name = policyTable.getColumnName(j);
            HSSFCell headerCell = headerRow.createCell(j);
            headerCell.setCellValue(name);

            HSSFCellStyle my_style = sheet.getWorkbook().createCellStyle();
            HSSFFont my_font = sheet.getWorkbook().createFont();
            my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            my_style.setFont(my_font);
            headerCell.setCellStyle(my_style);
        }
    }

    private static void writeCellValue(Object obj, HSSFCell cell, HSSFSheet sheet) {
        if (obj instanceof Date) {
            cell.setCellValue((Date) obj);
            
            HSSFDataFormat df = sheet.getWorkbook().createDataFormat();
            HSSFCellStyle cs = sheet.getWorkbook().createCellStyle();
            cs.setDataFormat(df.getFormat("dd.mm.yyyy"));
            cell.setCellStyle(cs);
            
        } else if (obj instanceof Boolean) {
            cell.setCellValue((Boolean) obj);
        } else if (obj instanceof String) {
            cell.setCellValue((String) obj);
        } else if (obj instanceof Double) {
            cell.setCellValue((Double) obj);
        } else if (obj instanceof BigDecimal) {
            cell.setCellValue(((BigDecimal) obj).doubleValue());
        } else if (obj instanceof Long) {
            cell.setCellValue(((Long) obj).longValue());
        } else if (obj instanceof Integer) {
            cell.setCellValue(((Integer) obj).intValue());
        }
    }
}
