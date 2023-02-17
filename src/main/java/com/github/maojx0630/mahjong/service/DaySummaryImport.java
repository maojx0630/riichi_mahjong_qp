package com.github.maojx0630.mahjong.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.github.maojx0630.mahjong.dto.ImportGame;
import com.github.maojx0630.mahjong.model.User;
import com.github.maojx0630.mahjong.model.UserDataChange;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.github.maojx0630.mahjong.service.UserDataChangeService.DAN_VALUE_MAP;

/**
 * @author 毛家兴
 * @since 2023/2/17 11:08
 */
@Service
@AllArgsConstructor
public class DaySummaryImport {

  private UserService userService;

  private ImportService importService;

  private UserDataChangeService changeService;

  @Transactional
  public void importAndDaySummary(String data) {
    List<String> outList = new ArrayList<>();
    outList.add("ID\t战后段位\t战后R值\t半庄数\t被飞数\t连对数\tR调整\tpt调整\t段位调整");
    List<String> split =
        StrUtil.split(data, "\n").stream().filter(StrUtil::isNotBlank).collect(Collectors.toList());
    DateTime dateTime = DateUtil.parse(split.get(0), "yyyy.MM.dd");
    split.remove(0);
    List<ImportGame> list = packageData(split, dateTime);
    Set<String> nameSet = new HashSet<>();
    for (ImportGame game : list) {
      nameSet.addAll(game.getUserName());
    }
    List<User> lastUserList = userService.lambdaQuery().in(User::getName, nameSet).list();
    importService.importData(list);
    for (User user : lastUserList) {
      User newData = userService.getById(user.getId());
      List<UserDataChange> changes = changeService.getDayUserData(user.getId(), dateTime);
      StringBuilder sb =
          new StringBuilder(user.getName())
              .append("\t")
              .append(DAN_VALUE_MAP.get(newData.getDanValue()).getDanName())
              .append("\t")
              .append(NumberUtil.decimalFormat("#0.00", Double.parseDouble(newData.getRate())))
              .append("\t")
              .append(changes.size())
              .append("\t")
              .append(
                  changes.stream()
                      .mapToInt(UserDataChange::getGamePoint)
                      .filter(item -> item <= 0)
                      .count())
              .append("\t")
              .append(
                  changes.stream()
                      .mapToInt(UserDataChange::getGameSeq)
                      .filter(item -> item <= 2)
                      .count())
              .append("\t")
              .append(
                  NumberUtil.decimalFormat(
                      "#0.00",
                      changes.stream()
                          .mapToDouble(item -> Double.parseDouble(item.getChangeRate()))
                          .sum()))
              .append("\t")
              .append(
                  changes.stream()
                      .filter(item -> Objects.nonNull(item.getChangePt()))
                      .mapToInt(UserDataChange::getChangePt)
                      .sum())
              .append("\t");
      if (newData.getDanValue().equals(user.getDanValue())) {
        sb.append(" ");

      } else {
        String lastName = DAN_VALUE_MAP.get(user.getDanValue()).getDanName();
        String newName = DAN_VALUE_MAP.get(newData.getDanValue()).getDanName();
        sb.append(lastName).append("=>").append(newName);
      }
      outList.add(sb.toString());
    }
    outList.forEach(System.out::println);
  }

  private List<ImportGame> packageData(List<String> split, DateTime dateTime) {
    List<ImportGame> list = new ArrayList<>();
    for (int i = 0; i < split.size(); i = i + 2) {
      List<String> nameList = StrUtil.split(split.get(i), " ");
      List<String> pointList = StrUtil.split(split.get(i + 1), " ");
      if (nameList.size() == pointList.size() && pointList.size() == 4) {
        if (1000 != pointList.stream().mapToInt(Integer::parseInt).sum()) {
          throw new RuntimeException(i + "数据不正确");
        }
      } else {
        throw new RuntimeException(i + "数据不正确");
      }
      List<Integer> collect =
          pointList.stream().map(Integer::parseInt).collect(Collectors.toList());
      ImportGame game = new ImportGame();
      game.setDateTime(dateTime);
      game.setUserName(nameList);
      game.setPointList(collect);
      list.add(game);
    }
    return list;
  }
}
