package util;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelCreator {

    public static DataSource getExcel(HashMap<String, String> content) {

        List<String> columns = Arrays.asList(content.get("col").split("\\s*,\\s*"));
        DataSource fds = null;
        try {

            // Create a Workbook
            Workbook workbook = new XSSFWorkbook();
            /*
             * CreationHelper helps us create instances of various things like DataFormat,
             * Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way
             */
            CreationHelper createHelper = workbook.getCreationHelper();

            // Create a Sheet
            Sheet sheet = workbook.createSheet("Reports");

            // Create a Font for styling header cells
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 14);
            headerFont.setColor(IndexedColors.RED.getIndex());

            // Create a CellStyle with the font
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            // Create a Row
            Row headerRow = sheet.createRow(0);

            // Create cells
            for (int i = 0; i < columns.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns.get(i));
                cell.setCellStyle(headerCellStyle);
            }

            int rowNum = 1;
            
           do{
                Row row = sheet.createRow(rowNum);
                List<String> columnsRowVal = Arrays.asList(content.get("val_"+rowNum).split("\\s*,\\s*"));
                for (int i = 0; i < columnsRowVal.size(); i++) {
                    row.createCell(i).setCellValue(columnsRowVal.get(i));
                }
                rowNum++;
            } while(content.containsKey("val_"+rowNum)); 
           
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out); // write excel data to a byte array
            

            // Now use your ByteArrayDataSource as
            fds = new ByteArrayDataSource(out.toByteArray(), "application/vnd.ms-excel");

            workbook.close();

            System.out.println(" FILE SYSTEM CREATED !!!! ");
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        
        return fds;
    }
}