package com.hjz.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;


import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.common.*;;


public class CreatAndReadExcel {
//测试主函数
//    private static String excel2003Path = "C:\\Users\\lenovo\\Desktop\\项目\\cdw.xls";
//    public static void main(String[] args) throws Exception {
//
//
////        List<List<Object>> excel2007List = readExcel(excel2007Path);// 读取2007版Excel文件
//        List<List<Object>> excel2003List = readExcel(excel2003Path);// 读取2003版Excel文件
//        System.out.println(excel2003List.toString());
//        System.out.println(excel2003List.get(1).get(0));
//        
//        String str = "手打手打";
//        if (str.equals(excel2003List.get(1).get(0).toString()) ) {
//        	System.out.println("true");
//        }else {
//        	System.out.println("false");
//        }
//    }


    /**
     * 读取excel
     *
     * @param fileName
     * @return 行<列>
     * @throws IOException
     */
    public static List<List<Object>> readExcel(String fileName) throws IOException {
        File file = new File(fileName);
        Workbook wb = null;
        if (fileName.endsWith(".xlsx")) {// 2007
            wb = new XSSFWorkbook(new FileInputStream(file));// 创建 一个excel文档对象
        } else if (fileName.endsWith(".xls")) {// 2003
            wb = new HSSFWorkbook(new FileInputStream(file));// 创建 一个excel文档对象
        }

        Sheet sheet = wb.getSheetAt(0);// 读取第一个sheet页表格内容
        Object value = null;
        Row row = null;
        Cell cell = null;
        System.out.println("读取office 2007 excel内容如下：");
//        System.out.println(sheet.getPhysicalNumberOfRows());// 获取的是物理行数，也就是不包括那些空行（隔行）的情况。
//        System.out.println(sheet.getLastRowNum());// 获取的是最后一行的编号（编号从0开始）
        // 行
        List<List<Object>> rowlist = new LinkedList<List<Object>>();
        for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
            row = sheet.getRow(i);
            if (row == null) {
                continue;
            }

            // 列
            List<Object> cellList = new LinkedList<Object>();
            for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
                cell = row.getCell(j);
                if (cell == null) {
                    continue;
                }

                DecimalFormat df = new DecimalFormat("0");// 格式化 number String
                DecimalFormat nf = new DecimalFormat("0");// 格式化数字
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
                switch (cell.getCellType()) {
                    case STRING:// 字符串——String type
                        value = cell.getStringCellValue();
                        break;
                    case NUMERIC:// 数字——Number type
                        if ("@".equals(cell.getCellStyle().getDataFormatString())) {
                            value = df.format(cell.getNumericCellValue());
                        } else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
                            value = nf.format(cell.getNumericCellValue());
                        } else {
                            value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
                        }
                        break;
                    case BOOLEAN:// boolean——Boolean type
                        value = cell.getBooleanCellValue();
                        break;
                    case BLANK:// 空白——Blank type
                        value = "";
                        break;
                    default:// default type
                        value = cell.toString();
                }
                if (value == null || "".equals(value)) {
                    continue;
                }
                cellList.add(value);
            }
            rowlist.add(cellList);
        }
        return rowlist;
    }
}