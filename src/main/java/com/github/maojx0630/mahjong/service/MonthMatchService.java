package com.github.maojx0630.mahjong.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.github.maojx0630.mahjong.dto.MonthMatchData;
import com.github.maojx0630.mahjong.model.User;
import com.github.maojx0630.mahjong.model.UserDataChange;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author 毛家兴
 * @since 2023/2/17 08:54
 */
@Service
@AllArgsConstructor
public class MonthMatchService {

  private static final Map<Integer, Integer> INTEGER_MAP = new HashMap<>();

  private UserService userService;

  private UserDataChangeService changeService;

  static {
    INTEGER_MAP.put(1, -10);
    INTEGER_MAP.put(2, -20);
    INTEGER_MAP.put(3, -30);
    INTEGER_MAP.put(4, -40);
  }

  public void getMonthMatchData(DateTime date) {
    int dateInt = Integer.parseInt(DateUtil.format(date,"yyyyMM"));
    Set<Integer> userIds = changeService.getMonthUser(dateInt);
    List<MonthMatchData> list = new ArrayList<>();
    for (Integer userId : userIds) {
      User user = userService.getById(userId);
      List<UserDataChange> changeList = changeService.getMonthChangeList(userId, dateInt);
      double point = 0D;

      for (UserDataChange change : changeList) {
        Integer seqPoint = INTEGER_MAP.get(change.getGameSeq());
        double div = NumberUtil.div(change.getGamePoint().intValue(), 10);
        point = NumberUtil.add(seqPoint, div, point).doubleValue();
      }

      MonthMatchData monthMatchData = new MonthMatchData();
      monthMatchData.setName(user.getName());
      monthMatchData.setCount(changeList.size() + "/20");
      monthMatchData.setPoint(point);
      list.add(monthMatchData);
    }
    list.stream()
        .sorted((item1, item2) -> NumberUtil.compare(item2.getPoint(), item1.getPoint()))
        .forEach(
            item -> {
              System.out.println(item.getCount() + "\t" + item.getPoint() + "\t" + item.getName());
            });
  }
}
