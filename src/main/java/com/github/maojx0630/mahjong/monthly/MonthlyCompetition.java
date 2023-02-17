package com.github.maojx0630.mahjong.monthly;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.github.maojx0630.mahjong.dto.ImportGame;
import lombok.SneakyThrows;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 计算月赛成绩
 *
 * @author 毛家兴
 * @since 2023/2/16 08:44
 */
public class MonthlyCompetition {

  private static final Map<Integer, Integer> pointMap = new HashMap<>();

  static {
    pointMap.put(0, 150);
    pointMap.put(1, 50);
    pointMap.put(2, -50);
    pointMap.put(3, -150);
  }

  @SneakyThrows
  public static void main(String[] args) {
    List<ImportGame> list = getMonthGame();
    Set<String> gameName = new HashSet<>();
    for (ImportGame game : list) {
      gameName.addAll(game.getUserName());
    }

    List<MonthlyPoint> pointList = new ArrayList<>();
    for (String s : gameName) {
      MonthlyPoint monthlyPoint = new MonthlyPoint(s);
      pointList.add(monthlyPoint);
      List<ImportGame> collect =
          list.stream().filter(item -> item.getUserName().contains(s)).collect(Collectors.toList());
      for (ImportGame game : collect) {

        int i = game.getUserName().indexOf(s);
        Integer point = game.getPointList().get(i);
        int sumPoint = point - 250 + pointMap.get(i);
        monthlyPoint.getPoints().add(sumPoint);
        if (monthlyPoint.getPoints().size() == 20) {
          break;
        }
      }
    }

    for (MonthlyPoint monthlyPoint : pointList) {
      int sum = monthlyPoint.getPoints().stream().mapToInt(item -> item).sum();
      double div = NumberUtil.div(sum, 10);
      monthlyPoint.setSumPoint(div);
    }
    List<MonthlyPoint> collect =
        pointList.stream()
            .filter(item -> item.getPoints().size() >= 20)
            .sorted((item1, item2) -> NumberUtil.compare(item1.getSumPoint(), item2.getSumPoint()))
            .collect(Collectors.toList());
    collect = CollUtil.reverse(collect);
    System.out.println("对局数\t点数\t名称");
    for (MonthlyPoint point : collect) {
      System.out.println(
          point.getPoints().size() + "/20\t" + point.getSumPoint() + "\t" + point.getName());
    }
  }

  @SneakyThrows
  public static List<ImportGame> getMonthGame() {
    List<ImportGame> list = new ArrayList<>();
    XSSFWorkbook wb = new XSSFWorkbook("/Users/mjx/Downloads/清平档案230214.xlsx");
    XSSFSheet sheetAt = wb.getSheetAt(1);
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
      String rawValue = sheetAt.getRow(0).getCell(cellIndex).getRawValue();
      System.out.println(rawValue);
      DateTime parse;
      try{
         parse = DateUtil.parse(rawValue, "MM.dd");
      }catch (Exception e){
        String stringCellValue = sheetAt.getRow(0).getCell(cellIndex).getStringCellValue();
        System.out.println(stringCellValue);
        parse = DateUtil.parse(stringCellValue, "MM.dd");
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
