package com.yougou.wfx.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * <p>Title: ExcelUtil</p>
 * <p>Description: </p>
 * @author: zheng.qq
 * @date: 2016年4月12日
 */
public class ExcelUtil {
	/**
	 * 获得单元格中的内容
	 * 
	 * @param cell
	 * @return
	 */
	private static Object getCellValue(Cell cell) {
		DecimalFormat df = new DecimalFormat("#");
		Object result = null;
		if (cell != null) {
			int cellType = cell.getCellType();
			switch (cellType) {
				case HSSFCell.CELL_TYPE_STRING:
					result = cell.getRichStringCellValue().getString();
					break;
				case HSSFCell.CELL_TYPE_NUMERIC:
					result = df.format(cell.getNumericCellValue());
					break;
				case HSSFCell.CELL_TYPE_FORMULA:
					result = cell.getCellFormula();
					break;
				case HSSFCell.CELL_TYPE_ERROR:
					result = null;
					break;
				case HSSFCell.CELL_TYPE_BOOLEAN:
					result = cell.getBooleanCellValue();
					break;
				case HSSFCell.CELL_TYPE_BLANK:
					result = null;
					break;
			}
		}
		return result;
	}

	public static HSSFSheet getSheet(String filePath, int sheetId){
		HSSFSheet sheet = null;
		try {
			FileInputStream fis = new FileInputStream(new File(filePath));
			HSSFWorkbook wb = new HSSFWorkbook(fis);
			sheet = wb.getSheetAt(sheetId);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sheet;
	}
	
	public static List<Object[]> readExcel(InputStream s, int sheetId){
		List<Object[]> result = new ArrayList<Object[]>();
		try {
			HSSFWorkbook wb = new HSSFWorkbook(s);
			HSSFSheet sheet = wb.getSheetAt(sheetId);
			//迭代行
	        for (Iterator<Row> riter = (Iterator<Row>) sheet.rowIterator(); riter.hasNext();) {
	            Row row = riter.next();
	            // 获得本行中单元格的个数
				int columnCount = row.getLastCellNum();
				Object[] rowData = new Object[columnCount];
	            //迭代列
	            for (Iterator<Cell> citer = (Iterator<Cell>) row.cellIterator(); citer.hasNext();) {
                    Cell cell = citer.next();
					int colNum = cell.getColumnIndex();
					// 获得指定单元格中数据
					Object cellStr = getCellValue(cell);
					rowData[colNum] = cellStr;
	            }
	            result.add(rowData);
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
