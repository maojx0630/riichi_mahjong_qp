package com.github.maojx0630.mahjong.qingping;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.github.maojx0630.mahjong.dto.ImportGame;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 毛家兴
 * @since 2023/2/16 10:22
 */
public class qingpingPt {

	public static void main(String[] args) {
		getMonthGame(2);
		getMonthGame(1);
	}

	@SneakyThrows
	public static List<ImportGame> getMonthGame(Integer sheetIndex) {
		List<ImportGame> list = new ArrayList<>();
		XSSFWorkbook wb = new XSSFWorkbook("/Users/mjx/Downloads/清平档案230216.xlsx");
		XSSFSheet sheetAt = wb.getSheetAt(sheetIndex);
		XSSFRow titleRow = sheetAt.getRow(0);
		short lastCellNum = titleRow.getLastCellNum();
		List<Integer> cellList = new ArrayList<>();
		for (int i = 0; i <= lastCellNum; i++) {
			XSSFCell cell = titleRow.getCell(i);
			if (cell != null) {
				String rawValue = cell.getRawValue();
				if (StrUtil.isNotBlank(rawValue)) {
					cellList.add(i);
				}
			}
		}
		for (Integer cellIndex : cellList) {
			XSSFCell xssfCell = sheetAt.getRow(0).getCell(cellIndex);
			CellType cellType = xssfCell.getCellType();
			DateTime parse;
			if (cellType == CellType.NUMERIC) {
				double numericCellValue = xssfCell.getNumericCellValue();
				parse = DateUtil.parse("2023." + numericCellValue, "yyyy.MM.dd");
			} else if (cellType == CellType.STRING) {
				String stringCellValue = xssfCell.getStringCellValue();
				parse = DateUtil.parse("2023." + stringCellValue, "yyyy.MM.dd");
			} else {
				xssfCell.getStringCellValue();
				throw new RuntimeException("xssfCell.getStringCellValue()");
			}
			int rowIndex = 1;
			while (true) {
				XSSFRow row = sheetAt.getRow(rowIndex);
				if (row == null) {
					break;
				}
				XSSFCell cell = row.getCell(cellIndex);
				if (cell == null) {
					break;
				}
				String nameValue = cell.getStringCellValue();
				String pointValue = sheetAt.getRow(rowIndex + 1).getCell(cellIndex).getStringCellValue();
				System.out.println(nameValue + "\t" + pointValue);
				ImportGame game = new ImportGame();
				game.setDateTime(parse);
				game.setUserName(StrUtil.split(nameValue, " "));
				List<String> split = StrUtil.split(pointValue, " ");
				List<Integer> integerList = new ArrayList<>();
				for (String s : split) {
					integerList.add(Integer.parseInt(s));
				}
				game.setPointList(integerList);
				list.add(game);
				rowIndex += 2;
			}
		}
		return list;
	}
}
