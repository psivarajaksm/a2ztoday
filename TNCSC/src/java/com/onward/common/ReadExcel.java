/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onward.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *
 * @author Prince vijayakumar M
 */
public class ReadExcel {

    public Map getData(String serverPath) throws Exception {
        Map xlmap = new LinkedHashMap();
        try {
            File file = new File(serverPath);
            FileInputStream fis = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheetAt(0);
            Row row;
            Cell cell;

            int rows; // No of rows
            rows = sheet.getPhysicalNumberOfRows();
            List xllist = null;

            int cols = 9; // No of columns
            int tmp = 0;

            for (int r = 0; r < rows; r++) {
                // <editor-fold defaultstate="collapsed" desc="Main For Loop">
                row = sheet.getRow(r);
                if (r > 0) {
                    if (row != null) {
                        xllist = new ArrayList();
                        for (int c = 0; c < cols; c++) {
                            // <editor-fold defaultstate="collapsed" desc="Set For Loop">
                            cell = row.getCell((short) c);
//                        System.out.println("[" + r + "," + c + "]");
                            if (c == 0 || c == 1) {
                                if (cell != null) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    Date date = cell.getDateCellValue();
                                    xllist.add(sdf.format(date));
                                } else {
                                    xllist.add(null);
                                }
                            }
                            if (c == 2 || c == 7) {
                                if (cell != null) {
                                    xllist.add(cell.getStringCellValue());
                                } else {
                                    xllist.add(null);
                                }
                            }
                            if (c == 3 || c == 4 || c == 5 || c == 6 || c == 8) {
                                switch (cell.getCellType()) {
                                    case Cell.CELL_TYPE_BOOLEAN:
                                        xllist.add(cell.getBooleanCellValue());
                                        break;
                                    case Cell.CELL_TYPE_NUMERIC:
                                        xllist.add(String.valueOf(cell.getNumericCellValue()));
                                        break;
                                    case Cell.CELL_TYPE_STRING:
                                        xllist.add(cell.getStringCellValue());
                                        break;
                                }
                            }
                            xlmap.put(r, xllist);
                            // </editor-fold>
                        }
                    }
                }
                // </editor-fold>
            }

        } catch (FileNotFoundException e) {
            xlmap.put("ERROR", "File Not Found!");
            e.printStackTrace();
        } catch (IOException e) {
            xlmap.put("ERROR", "File Read Exception!");
            e.printStackTrace();
        }
        return xlmap;
    }
}
