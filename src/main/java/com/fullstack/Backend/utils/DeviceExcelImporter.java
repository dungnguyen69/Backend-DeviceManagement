package com.fullstack.Backend.utils;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import static com.fullstack.Backend.constant.constant.*;
import com.fullstack.Backend.entities.Device;
import com.fullstack.Backend.enums.Origin;
import com.fullstack.Backend.enums.Project;
import com.fullstack.Backend.enums.Status;
import com.fullstack.Backend.services.IEmployeeService;
import com.fullstack.Backend.services.IItemTypeService;
import com.fullstack.Backend.services.IPlatformService;
import com.fullstack.Backend.services.IRamService;
import com.fullstack.Backend.services.IScreenService;
import com.fullstack.Backend.services.IStorageService;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

public class DeviceExcelImporter {
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;

	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	static String[] HEADERs = { "Id", "Title", "Description", "Published" };
	static String SHEET = "Devices";

	// Initialize an instance.
	public DeviceExcelImporter() {
		workbook = new XSSFWorkbook(); // The whole Excel file
	}

	public static boolean hasExcelFormat(MultipartFile file) {
		if (!TYPE.equals(file.getContentType()))
			return false;

		return true;
	}

	public static int getNumberOfNonEmptyCells(XSSFSheet sheet, int columnIndex) {
		int numOfNonEmptyCells = 0;
		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			if (row != null) {
				Cell cell = row.getCell(columnIndex);
				if (cell != null && cell.getCellType() != CellType.BLANK) {
					numOfNonEmptyCells++;
				}
			}
		}
		return numOfNonEmptyCells;
	}

	public void returnImportResult(HttpServletResponse response) throws IOException {

		isFileFormatValid();
		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}

	private Object getValue(Cell cell) {
		switch (cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue();
		case NUMERIC:
			return String.valueOf((int) cell.getNumericCellValue());
		case BOOLEAN:
			return cell.getBooleanCellValue();
		case ERROR:
			return cell.getErrorCellValue();
		case FORMULA:
			return cell.getCellFormula();
		case BLANK:
			return null;
		case _NONE:
			return null;
		default:
			break;
		}
		return null;
	}

	private void isFileFormatValid() {
		sheet = workbook.createSheet("Import Result");
		CellStyle cellStyle = workbook.createCellStyle(); // Cells
		XSSFFont font = workbook.createFont(); // Set up font style for cells
		font.setBold(true);
		font.setFontHeight(16);
		cellStyle.setFont(font);
		cellStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);

		createCell(sheet.createRow(0), 0, "Import Result", cellStyle);
		createCell(sheet.createRow(1), 0, "Error", cellStyle);
		createCell(sheet.createRow(1), 1, "The import file does not match the format (.xlsx)", cellStyle);
	}

	private void createCell(Row row, int columnCount, Object value, CellStyle cellStyle) {
		sheet.autoSizeColumn(columnCount); // adjust the column size for the contents
		Cell cell = row.createCell(columnCount); // )cells for a row
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss"); // format for date
		String dateValue;
		// Probe and set a data type for values
		if (value instanceof Integer)
			cell.setCellValue((Integer) value);
		else if (value instanceof Long)
			cell.setCellValue((Long) value);
		else if (value instanceof Date) {
			dateValue = dateFormatter.format(value); // attain current date
			cell.setCellValue(dateValue);
		} else if (value instanceof Boolean)
			cell.setCellValue((Boolean) value);
		else
			cell.setCellValue((String) value);
		cell.setCellStyle(cellStyle);
	}
}