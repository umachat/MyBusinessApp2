package com.mybusinessapp.example.madhu.mybusinessapp2.reports;

import com.mybusinessapp.example.madhu.mybusinessapp2.util.AppConstants;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Writes an Excel file after invoking dao to get data.
 */
public class ExcelWriteUtils {

    public static void refresh(File file) {

        if(file.exists()) file.delete();
    }

    public static <T> void writeToExcelInMultiSheets(File file, final String sheetName,
                                                           final List<T> data,boolean update) {

        OutputStream fos = null;
        XSSFWorkbook workbook = null;
        try {

            XSSFSheet sheet = null;


            if (update) {
                workbook = (XSSFWorkbook) WorkbookFactory.create(new FileInputStream(file));
            } else {
                workbook = new XSSFWorkbook();
            }



            sheet = workbook.createSheet(sheetName);
           // sheet.protectSheet("madshi");

            List<String> fieldNames = getFieldNamesForClass(data.get(0).getClass());
            int rowCount = 0;
            int columnCount = 0;
            XSSFRow row = sheet.createRow(rowCount++);

            //Cell style for header row
            CellStyle cs = workbook.createCellStyle();
            cs.setFillForegroundColor(HSSFColor.LIME.index);
            cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            cs.setWrapText(true);


            //Cell style for other row
            CellStyle cs2 = workbook.createCellStyle();
            cs2.setWrapText(true);


            for (String fieldName : fieldNames) {
                if(!Pattern.matches("[a-zA-Z_]*", fieldName) || fieldName.equalsIgnoreCase("serialVersionUID")) {
                    continue;
                }
                Cell cell = row.createCell(columnCount++);
                cell.setCellValue(fieldName.toUpperCase());
                cell.setCellStyle(cs);
//                sheet.autoSizeColumn(columnCount);
            }
            Class<? extends Object> classz = data.get(0).getClass();
            for (T t : data) {
                row = sheet.createRow(rowCount++);
                columnCount = 0;
                for (String fieldName : fieldNames) {
                    //to skip android api generated weird methods for serializable objects.
                    if(!Pattern.matches("[a-zA-Z_]*", fieldName) || fieldName.equalsIgnoreCase("serialVersionUID")) {
                        continue;
                    }

                    Cell cell = row.createCell(columnCount);
                    cell.setCellStyle(cs2);

                    Method method = null;
                    try {
                        method = classz.getMethod("get" + capitalize(fieldName));
                    } catch (NoSuchMethodException nme) {
                        method = classz.getMethod("get" + fieldName);
                    }
                    Object value = method.invoke(t, (Object[]) null);
                    if (value != null) {
                        if (value instanceof String) {
                            cell.setCellValue((String) value);
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                        } else if (value instanceof Long) {
                            cell.setCellValue((Long) value);
                            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        } else if (value instanceof Integer) {
                            cell.setCellValue((Integer) value);
                            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        } else if (value instanceof Double) {
                            cell.setCellValue((Double) value);
                            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        }
                    }
                    columnCount++;
//                    sheet.autoSizeColumn(columnCount);
                }
            }

            sheet.protectSheet(AppConstants.EXCEL_PASSWORD);


            fos = new FileOutputStream(file);


            //workbook password  TODO


            workbook.write(fos);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
            }

        }
    }

    //retrieve field names from a POJO class
    private static List<String> getFieldNamesForClass(Class<?> clazz) throws Exception {
        List<String> fieldNames = new ArrayList<String>();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fieldNames.add(fields[i].getName());
        }
        return fieldNames;
    }

    //capitalize the first letter of the field name for retriving value of the field later
    private static String capitalize(String s) {
        if (s.length() == 0)
            return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

}
