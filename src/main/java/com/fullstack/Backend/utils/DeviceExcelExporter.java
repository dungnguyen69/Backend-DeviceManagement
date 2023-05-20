package com.fullstack.Backend.utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fullstack.Backend.entities.Device;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

public class DeviceExcelExporter {
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private List<Device> listDevices;

	// Initialize an instance.
	public DeviceExcelExporter(List<Device> listDevices) {
		this.listDevices = listDevices;
		workbook = new XSSFWorkbook(); // The whole Excel file
	}

	// For headers
	private void writeHeaderLine() {
		sheet = workbook.createSheet("Devices"); // An Excel Sheet : Devices
		Row row = sheet.createRow(0); // First row of the sheet
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

		createCell(row, 0, "ID", cellStyle);
		createCell(row, 1, "Name", cellStyle);
		createCell(row, 2, "Status", cellStyle);
		createCell(row, 3, "Item Type", cellStyle);
		createCell(row, 4, "Platform Name", cellStyle);
		createCell(row, 5, "Platform Version", cellStyle);
		createCell(row, 6, "Ram", cellStyle);
		createCell(row, 7, "Screen", cellStyle);
		createCell(row, 8, "Storage", cellStyle);
		createCell(row, 9, "Inventory Number", cellStyle);
		createCell(row, 10, "Serial Number", cellStyle);
		createCell(row, 11, "Project", cellStyle);
		createCell(row, 12, "Origin", cellStyle);
		createCell(row, 13, "Owner", cellStyle);
		createCell(row, 14, "Keeper", cellStyle);
		createCell(row, 15, "Booking Date", cellStyle);
		createCell(row, 16, "Return Date", cellStyle);
		createCell(row, 17, "Created Date", cellStyle);
		createCell(row, 18, "Updated Date", cellStyle);
		createCell(row, 19, "Comments", cellStyle);

	}

	private void createCell(Row row, int columnCount, Object value, CellStyle cellStyle) {
		sheet.autoSizeColumn(columnCount); // adjust the column size for the contents
		Cell cell = row.createCell(columnCount); // cells for a row
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

	// For data
	private void writeDataLines() {
		int rowCount = 1; // Start from first row

		CellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		cellStyle.setFont(font);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);

		for (Device device : listDevices) { // Print devices for each row
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;
			int id = rowCount - 1;
			createCell(row, columnCount++, id, cellStyle);
			createCell(row, columnCount++, device.getName(), cellStyle);
			createCell(row, columnCount++, device.getStatus().name(), cellStyle);
			createCell(row, columnCount++, device.getItemType().getName(), cellStyle);
			createCell(row, columnCount++, device.getPlatform().getName(), cellStyle);
			createCell(row, columnCount++, device.getPlatform().getVersion(), cellStyle);
			createCell(row, columnCount++, device.getRam().getSize(), cellStyle);
			createCell(row, columnCount++, device.getScreen().getSize(), cellStyle);
			createCell(row, columnCount++, device.getStorage().getSize(), cellStyle);
			createCell(row, columnCount++, device.getInventoryNumber(), cellStyle);
			createCell(row, columnCount++, device.getSerialNumber(), cellStyle);
			createCell(row, columnCount++, device.getProject().name(), cellStyle);
			createCell(row, columnCount++, device.getOrigin().name(), cellStyle);
			createCell(row, columnCount++, device.getOwner().getUserName(), cellStyle);
			createCell(row, columnCount++, "", cellStyle);
			createCell(row, columnCount++, "", cellStyle);
			createCell(row, columnCount++, "", cellStyle);
			createCell(row, columnCount++, device.getCreatedDate(), cellStyle);
			createCell(row, columnCount++, device.getUpdatedDate(), cellStyle);
			createCell(row, columnCount++, device.getComments(), cellStyle);

		}
	}

	// write the content of the Excel file into the output stream of the response
	// => clients will be able to download the exported Excel file
	public void export(HttpServletResponse response) throws IOException {
		writeHeaderLine();
		writeDataLines();
		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}
}
