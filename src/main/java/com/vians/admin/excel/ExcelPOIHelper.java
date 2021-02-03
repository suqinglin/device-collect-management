package com.vians.admin.excel;

import com.vians.admin.model.DeviceBaseInfo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ExcelPOIHelper
 * @Description excel操作工具
 * @Author su qinglin
 * @Date 2021/1/26 14:33
 * @Version 1.0
 **/
public class ExcelPOIHelper {

    public List<ExcelUser> readUsersExcel(String fileName, InputStream inputStream) {
        Workbook workbook = null;
        List<ExcelUser> data = new ArrayList<>();
        /**
         * 判断文件格式 2007和2013标准是不一样的
         */
        try {
            if (fileName.endsWith(".xls")) {
                workbook = new HSSFWorkbook(inputStream);
            } else if (fileName.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(inputStream);
            }
            if (workbook != null) {
                Sheet sheet = workbook.getSheetAt(0);
                for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    ExcelUser.ExcelUserBuilder excelUserBuilder = ExcelUser.builder();
                    excelUserBuilder.userName(readCellContent(row.getCell(0)));
                    excelUserBuilder.password(readCellContent(row.getCell(1)));
                    excelUserBuilder.gender(readCellContent(row.getCell(2)));
                    excelUserBuilder.cardId(readCellContent(row.getCell(3)));
                    excelUserBuilder.phone(readCellContent(row.getCell(4)));
                    excelUserBuilder.role(readCellContent(row.getCell(5)));
                    excelUserBuilder.organization(readCellContent(row.getCell(6)));
                    excelUserBuilder.department(readCellContent(row.getCell(7)));
                    excelUserBuilder.workNum(readCellContent(row.getCell(8)));
                    excelUserBuilder.duty(readCellContent(row.getCell(9)));
                    data.add(excelUserBuilder.build());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public List<DeviceBaseInfo> readDevicesExcel(String fileName, InputStream inputStream) {
        Workbook workbook = null;
        List<DeviceBaseInfo> data = new ArrayList<>();
        /**
         * 判断文件格式 2007和2013标准是不一样的
         */
        try {
            if (fileName.endsWith(".xls")) {
                workbook = new HSSFWorkbook(inputStream);
            } else if (fileName.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(inputStream);
            }
            if (workbook != null) {
                Sheet sheet = workbook.getSheetAt(0);
                for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    DeviceBaseInfo.DeviceBaseInfoBuilder builder = DeviceBaseInfo.builder();
                    builder.MAC(readCellContent(row.getCell(0)));
                    builder.GwMac(readCellContent(row.getCell(1)));
                    builder.Device(readCellContent(row.getCell(2)));
                    builder.Name(readCellContent(row.getCell(3)));
                    builder.Feature(readCellContent(row.getCell(4)));
                    builder.Active(readCellContent(row.getCell(5)));
                    builder.Index(readCellContent(row.getCell(6)));
                    data.add(builder.build());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    private String readCellContent(Cell cell) {
        String content = "";
        if (cell == null) {
            return content;
        }
        cell.setCellType(CellType.STRING);
        content = cell.getStringCellValue();
        return content;
    }
}
