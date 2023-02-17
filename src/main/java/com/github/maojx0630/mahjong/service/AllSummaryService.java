package com.github.maojx0630.mahjong.service;

import cn.hutool.core.util.NumberUtil;
import com.github.maojx0630.mahjong.model.DanInfo;
import com.github.maojx0630.mahjong.model.User;
import com.github.maojx0630.mahjong.model.UserDataChange;
import lombok.AllArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.maojx0630.mahjong.service.UserDataChangeService.DAN_VALUE_MAP;

/**
 * @author 毛家兴
 * @since 2023/2/17 13:31
 */
@Service
@AllArgsConstructor
public class AllSummaryService {

  private UserService userService;

  private UserDataChangeService changeService;

  public void exportAllData(String path) {
    File file = new File(path);
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    try (XSSFWorkbook wb = new XSSFWorkbook();
        FileOutputStream fileOutputStream = new FileOutputStream(path)) {
      rank(wb);
      rate(wb);
      wb.getCreationHelper().createFormulaEvaluator().evaluateAll();
      wb.write(fileOutputStream);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static final List<String> rankTitleList =
      Arrays.asList("段位排名", "ID", "段位", "PT", "R值", "半庄数", "均顺", "顺位总计", "被飞次数", "被飞率", "登记顺序");

  private void rank(XSSFWorkbook wb) {
    XSSFSheet sheet = wb.createSheet("排名");
    int rowIndex = 0;
    XSSFRow title = sheet.createRow(rowIndex++);
    for (int i = 0; i < rankTitleList.size(); i++) {
      title.createCell(i).setCellValue(rankTitleList.get(i));
    }
    List<User> userList = userService.getRankUserList();
    for (int i = 0; i < userList.size(); i++) {
      User user = userList.get(i);
      List<UserDataChange> list =
          changeService.lambdaQuery().eq(UserDataChange::getUserId, user.getId()).list();
      int sumSeq = list.stream().mapToInt(UserDataChange::getGameSeq).sum();
      long fly =
          list.stream().mapToInt(UserDataChange::getGamePoint).filter(item -> item < 0).count();
      DanInfo danInfo = DAN_VALUE_MAP.get(user.getDanValue());
      XSSFRow row = sheet.createRow(rowIndex++);
      int cellIndex = 0;
      row.createCell(cellIndex++).setCellValue((i + 1));
      row.createCell(cellIndex++).setCellValue(user.getName());
      row.createCell(cellIndex++).setCellValue(danInfo.getDanName());
      row.createCell(cellIndex++).setCellValue(user.getPt() + "/" + danInfo.getUpgradePt());
      row.createCell(cellIndex++)
          .setCellValue(NumberUtil.decimalFormat("#0.00", Double.parseDouble(user.getRate())));
      row.createCell(cellIndex++).setCellValue(list.size());
      row.createCell(cellIndex++)
          .setCellValue(NumberUtil.decimalFormat("#0.00", NumberUtil.div(sumSeq, list.size())));
      row.createCell(cellIndex++).setCellValue(sumSeq);
      row.createCell(cellIndex++).setCellValue(fly);
      row.createCell(cellIndex++)
          .setCellValue(NumberUtil.decimalFormat("#0.00%", NumberUtil.div(fly, list.size())));
      row.createCell(cellIndex++).setCellValue(user.getId());
    }
  }

  private static final List<String> rateTitleList =
      Arrays.asList(
          "登记顺序", "登记日期", "ID", "半庄数", "一位率", "二位率", "三位率", "四位率", "连对率", "连错率", "一位次数", "二位次数",
          "三位次数", "四位次数");

  private void rate(XSSFWorkbook wb) {
    XSSFSheet sheet = wb.createSheet("位率");
    int rowIndex = 0;
    XSSFRow title = sheet.createRow(rowIndex++);
    for (int i = 0; i < rateTitleList.size(); i++) {
      title.createCell(i).setCellValue(rateTitleList.get(i));
    }
    for (User user : userService.lambdaQuery().orderByAsc(User::getId).list()) {
      List<UserDataChange> list =
          changeService.lambdaQuery().eq(UserDataChange::getUserId, user.getId()).list();
      List<Integer> collect =
          list.stream().map(UserDataChange::getGameSeq).collect(Collectors.toList());
      int cellIndex = 0;
      int count = list.size();
      XSSFRow row = sheet.createRow(rowIndex++);
      row.createCell(cellIndex++).setCellValue(user.getId());
      row.createCell(cellIndex++).setCellValue(user.getRegisterDate());
      row.createCell(cellIndex++).setCellValue(user.getName());
      row.createCell(cellIndex++).setCellValue(count);
      row.createCell(cellIndex++).setCellFormula("K" + rowIndex + "/D" + rowIndex);
      row.createCell(cellIndex++).setCellFormula("L" + rowIndex + "/D" + rowIndex);
      row.createCell(cellIndex++).setCellFormula("M" + rowIndex + "/D" + rowIndex);
      row.createCell(cellIndex++).setCellFormula("N" + rowIndex + "/D" + rowIndex);
      row.createCell(cellIndex++).setCellFormula("E" + rowIndex + "+F" + rowIndex);
      row.createCell(cellIndex++).setCellFormula("G" + rowIndex + "+H" + rowIndex);
      row.createCell(cellIndex++).setCellValue(collect.stream().filter(item -> item == 1).count());
      row.createCell(cellIndex++).setCellValue(collect.stream().filter(item -> item == 2).count());
      row.createCell(cellIndex++).setCellValue(collect.stream().filter(item -> item == 3).count());
      row.createCell(cellIndex++).setCellValue(collect.stream().filter(item -> item == 4).count());
    }
  }
}
