/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2021 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package de.symeda.sormas.backend.util;

import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.apache.poi.xssf.usermodel.XSSFTableStyleInfo;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import de.symeda.sormas.api.utils.InfoProvider;

public final class XssfHelper {

	private XssfHelper() {
		// Hide utility class constructor
	}

	public static final int TABLE_STYLE_PRIMARY = 1;
	public static final int TABLE_STYLE_SECONDARY = 2;

	public static XSSFTable configureTable(AreaReference reference, String safeTableName, XSSFSheet sheet, int styleNumber) {

		XSSFTable table = sheet.createTable(reference);
		table.setName(safeTableName);
		table.setDisplayName(safeTableName);
		XssfHelper.styleTable(table, styleNumber);
		table.getCTTable().addNewAutoFilter();

		return table;
	}

	public static void styleTable(XSSFTable table, int styleNumber) {

		// Style the table - can this be simplified?
		table.getCTTable().addNewTableStyleInfo();
		String tableStyleName = "TableStyleLight" + styleNumber;
		table.getCTTable().getTableStyleInfo().setName(tableStyleName);
		XSSFTableStyleInfo style = (XSSFTableStyleInfo) table.getStyle();
		style.setName(tableStyleName);
		style.setFirstColumn(false);
		style.setLastColumn(false);
		style.setShowRowStripes(true);
		style.setShowColumnStripes(false);
	}

	public static void addAboutSheet(XSSFWorkbook workbook) {

		XSSFSheet sheet = workbook.createSheet("About");
		XSSFRow row = sheet.createRow(0);
		XSSFCell cell = row.createCell(0);
		cell.setCellValue("SORMAS Version");

		row = sheet.createRow(1);
		cell = row.createCell(0);
		cell.setCellValue(InfoProvider.get().getVersion());
	}

	public static final XSSFColor createColor(int red, int green, int blue) {

		byte[] rgb = new byte[3];
		rgb[0] = (byte) red;
		rgb[1] = (byte) green;
		rgb[2] = (byte) blue;

		XSSFColor color = new XSSFColor(rgb, new DefaultIndexedColorMap());
		return color;
	}
}
