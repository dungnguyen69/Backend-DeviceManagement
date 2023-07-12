package com.fullstack.Backend.utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddressList;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.*;

import com.fullstack.Backend.dto.device.DropDownListsDTO;

public class DeviceExcelTemplate {
	private XSSFWorkbook workBook;
	private XSSFSheet sheet;

	// Initialize an instance.
	public DeviceExcelTemplate() {
		workBook = new XSSFWorkbook();
	}

	private void dropdownLists(DropDownListsDTO dropDownListsDTO) {
		dropDownMenuPlatform(dropDownListsDTO.getPlatformList());
		dropdownMenu(1, dropDownListsDTO.getStatusList());
		dropdownMenu(2, dropDownListsDTO.getItemTypeList());
		dropdownMenu(4, dropDownListsDTO.getRamList());
		dropdownMenu(5, dropDownListsDTO.getScreenList());
		dropdownMenu(6, dropDownListsDTO.getStorageList());
		dropdownMenu(9, dropDownListsDTO.getProjectList());
		dropdownMenu(10, dropDownListsDTO.getOriginList());
	}

	private void dropdownMenu(int column, String[] list) {
		XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
		XSSFDataValidationConstraint constraint = (XSSFDataValidationConstraint) dvHelper
				.createExplicitListConstraint(list);
		CellRangeAddressList addressList = new CellRangeAddressList(1, 100, column, column);
		XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(constraint, addressList);
		validation.setShowErrorBox(true);
		sheet.addValidationData(validation);
	}

	private void dropDownMenuPlatform(String[] platformList) {
		XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
		XSSFSheet hiddenSheet = workBook.createSheet("hidden"); // create sheet for storing the list items:
		String reference = "hidden!$A$1:$A$" + platformList.length;
		int rowIndex = 0;
		for (String platform : platformList) {
			Row row = hiddenSheet.createRow(rowIndex);
			Cell cell = row.createCell(0);
			cell.setCellValue(platform);
			rowIndex++;
		}
		hiddenSheet.setSelected(false); // unselect that sheet because we will hide it later
		XSSFName namedCell = workBook.createName(); // create a named range for the list constraint
		namedCell.setNameName("hidden");
		namedCell.setRefersToFormula(reference);
		XSSFDataValidationConstraint platformVersionConstraint = (XSSFDataValidationConstraint) dvHelper
				.createFormulaListConstraint("hidden");
		CellRangeAddressList addressListForPlatformVersion = new CellRangeAddressList(1, 100, 3, 3);
		XSSFDataValidation platformVersionValidation = (XSSFDataValidation) dvHelper
				.createValidation(platformVersionConstraint, addressListForPlatformVersion);
		platformVersionValidation.setShowErrorBox(true);
		workBook.setSheetHidden(1, true); // Hide the hidden sheet
		sheet.addValidationData(platformVersionValidation);
	}

	// For headers
	private void writeHeaderLine() {
		sheet = workBook.createSheet("Devices");
		Row row = sheet.createRow(0); // First row of the sheet
		CellStyle cellStyle = workBook.createCellStyle(); // Cells
		XSSFFont font = workBook.createFont(); // Set up font style for cells
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

		// Use map
		createCell(row, 0, "Name", cellStyle);
		createCell(row, 1, "Status", cellStyle);
		createCell(row, 2, "Item Type", cellStyle);
		createCell(row, 3, "Platform", cellStyle);
		createCell(row, 4, "Ram", cellStyle);
		createCell(row, 5, "Screen", cellStyle);
		createCell(row, 6, "Storage", cellStyle);
		createCell(row, 7, "Inventory Number", cellStyle);
		createCell(row, 8, "Serial Number", cellStyle);
		createCell(row, 9, "Project", cellStyle);
		createCell(row, 10, "Origin", cellStyle);
		createCell(row, 11, "Comments", cellStyle);
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
		CellStyle cellStyle = workBook.createCellStyle();
		XSSFFont font = workBook.createFont();
		font.setFontHeight(14);
		cellStyle.setFont(font);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);

		Row row = sheet.createRow(1);
		int columnCount = 0;
		for (int i = 0; i < 12; i++) {
			createCell(row, columnCount++, null, cellStyle);
		}
	}

	public void export(HttpServletResponse response, DropDownListsDTO dropDownListsDTO) throws IOException {
		writeHeaderLine();
		writeDataLines();
		dropdownLists(dropDownListsDTO);
		ServletOutputStream outputStream = response.getOutputStream();
		workBook.write(outputStream);
		workBook.close();
		outputStream.close();
	}
}
