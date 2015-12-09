/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.common;

/**
 *
 * @author Prince vijayakumar M
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ReadxlsFile {

    private static String isNumberOrDate(Cell cell) {
        String retVal;
        if (HSSFDateUtil.isCellDateFormatted(cell)) {
            DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            retVal = formatter.format(cell.getDateCellValue());
        } else {
            DataFormatter df = new DataFormatter();
            retVal = df.formatCellValue(cell);
        }
        return retVal;
    }

    public LinkedHashMap getReadXLSFile(FileInputStream fis) {
        LinkedHashMap excelMap = new LinkedHashMap();
        LinkedHashMap map = new LinkedHashMap();
        try {
            Workbook workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheetAt(0);
            Row row;
            Cell cell;
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");

            int rows; // No of rows
            rows = sheet.getPhysicalNumberOfRows();
//            System.out.println("no of rows = "+rows);
            excelMap.put("totalrows", rows);
            List list = null;

            int cols = 8; // No of columns
            int tmp = 0;

            for (int r = 0; r < rows; r++) {
                list = new ArrayList();
                row = sheet.getRow(r);
                if (row != null) {
                    for (int c = 0; c < cols; c++) {
                        cell = row.getCell((short) c);
                        if (cell != null) {
                            switch (cell.getCellType()) {
                                case Cell.CELL_TYPE_BOOLEAN:
                                    String retVal = "" + cell.getBooleanCellValue();
                                    list.add(retVal);
                                    break;
                                case Cell.CELL_TYPE_NUMERIC:
                                    retVal = isNumberOrDate(cell);
                                    list.add(retVal);
                                    break;
                                case Cell.CELL_TYPE_STRING:
                                    list.add(cell.getStringCellValue());
                                    break;
                                case Cell.CELL_TYPE_BLANK:
                                    list.add("");
                                    break;
                                case Cell.CELL_TYPE_FORMULA:
                                    retVal = isNumberOrDate(cell);
                                    list.add(retVal);
                                    System.out.print(retVal + " ");
                                    break;
                            }
                        } else {
                            list.add(null);
                        }
                    }
                }
                map.put(r, list);
            }

            excelMap.put("map", map);


        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return excelMap;
    }
//    public static void main(String[] args) throws InvalidFormatException {
//        try {
//            File file = new File("D:\\bank scroll excel\\7790.xlsx");
//            FileInputStream fis = new FileInputStream(file);
//            
//            Map excelmap = getReadXLSFile(fis);            
//            int TOTALRECORDS = (Integer) excelmap.get("totalrows");
//            Map xlsmap = (Map) excelmap.get("map");                    
//            System.out.println("TOTALRECORDS = "+TOTALRECORDS);
//            
//            Iterator itr = xlsmap.entrySet().iterator();
//            while (itr.hasNext()) {
//                Map.Entry me = (Map.Entry) itr.next();
//                StringBuilder sb = new StringBuilder();
//                String ro = String.valueOf((Integer) me.getKey());
//                sb.append(ro);
//                sb.append(("\t"));
//                List listval = (List) me.getValue();
//                Iterator it = listval.iterator();
//                while (it.hasNext()) {
//                    sb.append((String) it.next());
//                    sb.append(("\t"));
//                }
//                System.out.println(sb.toString());
//            }
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
