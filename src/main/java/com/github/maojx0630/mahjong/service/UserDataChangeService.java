package com.github.maojx0630.mahjong.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.github.maojx0630.mahjong.common.base.BasicServiceImpl;
import com.github.maojx0630.mahjong.common.exception.StateEnum;
import com.github.maojx0630.mahjong.dto.ImportGame;
import com.github.maojx0630.mahjong.mapper.UserDataChangeMapper;
import com.github.maojx0630.mahjong.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 毛家兴
 * @since 2022/2/21 16:41
 */
@Service
@Transactional(readOnly = true)
public class UserDataChangeService extends BasicServiceImpl<UserDataChangeMapper, UserDataChange> {

  public static final Map<Integer, DanTitle> DAN_TITLE_MAP = new HashMap<>();
  public static final Map<Integer, DanInfo> DAN_VALUE_MAP = new HashMap<>();

  public static final Map<Integer, Integer> BASIC_RATE = new HashMap<>();

  public UserDataChangeService(
      DanInfoBasicService danInfoService, DanTitleBasicService danTitleService) {
    List<DanInfo> list = danInfoService.list();
    for (DanInfo danInfo : list) {
      DAN_VALUE_MAP.put(danInfo.getDanValue(), danInfo);
    }
    for (DanTitle danTitle : danTitleService.list()) {
      for (DanInfo danInfo : list) {
        if (danInfo.getTitleValue().equals(danTitle.getTitleName())) {
          DAN_TITLE_MAP.put(danInfo.getDanValue(), danTitle);
        }
      }
    }
    BASIC_RATE.put(0, 30);
    BASIC_RATE.put(1, 10);
    BASIC_RATE.put(2, -10);
    BASIC_RATE.put(3, -30);
  }

  public void toChange(Game game, ImportGame importGame, List<User> userList) {
    List<UserDataChange> changeList = initChangeList(game, importGame, userList);
    // changePt
    if (deskmate(userList)) {
      for (int i = 0; i < userList.size(); i++) {
        int danValue = userList.get(i).getDanValue();
        Integer pt = getSeqPt(i, DAN_VALUE_MAP.get(danValue), DAN_TITLE_MAP.get(danValue));
        UserDataChange change = changeList.get(i);
        change.setChangePt(pt);
      }
    } else {
      DanTitle lastDanTitle =
          DAN_TITLE_MAP.values().stream()
              .max(Comparator.comparingInt(DanTitle::getTitleValue))
              .get();
      for (User user : userList) {
        DanTitle danTitle = DAN_TITLE_MAP.get(user.getDanValue());
        if (danTitle.getTitleValue() < lastDanTitle.getTitleValue()) {
          lastDanTitle = danTitle;
        }
      }
      for (int i = 0; i < userList.size(); i++) {
        User user = userList.get(i);
        DanTitle danTitle = DAN_TITLE_MAP.get(user.getDanValue());
        Integer pt;
        // 特上以上按照上级结算
        if (lastDanTitle.getTitleValue() == 0 && danTitle.getTitleValue() >= 2) {
          pt = getSeqPt(i, DAN_VALUE_MAP.get(user.getDanValue()), DAN_TITLE_MAP.get(10));
        } else {
          pt = getSeqPt(i, DAN_VALUE_MAP.get(user.getDanValue()), lastDanTitle);
        }
        UserDataChange change = changeList.get(i);
        change.setChangePt(pt);
      }
    }

    // change rate
    double averageRate =
        NumberUtil.div(
            userList.stream().mapToDouble(item -> Double.parseDouble(item.getRate())).sum(), 4);
    for (int i = 0; i < changeList.size(); i++) {
      UserDataChange change = changeList.get(i);
      /// 对局数辅正
      double num = 0.2D;
      int count = this.lambdaQuery().eq(UserDataChange::getUserId, change.getUserId()).count();
      if (count < 200) {
        num = NumberUtil.sub(1, NumberUtil.mul(count, 0.004D));
      }
      // 辅正值
      double sub =
          NumberUtil.div(
              NumberUtil.sub(averageRate, Double.parseDouble(userList.get(i).getRate())), 40D);
      // 对局结果
      int basicRate = BASIC_RATE.get(i);
      double add = NumberUtil.add(sub, basicRate);
      double mul = NumberUtil.mul(add, num);
      change.setChangeRate(mul + "");
      change.setAfterRate(NumberUtil.add(Double.parseDouble(change.getBeforeRate()), mul) + "");
      userList.get(i).setRate(change.getAfterRate());
    }

    // 调整段位
    // 计算升降级 使其影响下次计算
    for (int i = 0; i < changeList.size(); i++) {
      User user = userList.get(i);
      UserDataChange change = changeList.get(i);
      int count = this.lambdaQuery().eq(UserDataChange::getUserId, change.getUserId()).count();
      if (count < 10) { // 定位赛
        if (i <= 1) {
          DanInfo info = DAN_VALUE_MAP.get(user.getDanValue() + 1);
          user.setPt(info.getBasisPt());
          user.setDanValue(info.getDanValue());
          change.setChangePt(null);
          continue;
        }
      }
      DanInfo danInfo = DAN_VALUE_MAP.get(user.getDanValue());
      int pt = user.getPt() + change.getChangePt();
      if (pt < 0) { // 如果pt小于0则判断是否降级
        // 如果小于0了 就只显示实际扣除的
        if (0 == danInfo.getDemotion()) { // 会降级
          DanInfo info = DAN_VALUE_MAP.get(user.getDanValue() - 1);
          user.setPt(info.getBasisPt());
          user.setDanValue(info.getDanValue());
        } else { // 不会降级
          user.setPt(0);
        }
        change.setChangePt(pt * -1);
      } else {
        if (pt >= danInfo.getUpgradePt()) { // 满足升级pt
          DanInfo info = DAN_VALUE_MAP.get(user.getDanValue() + 1);
          change.setChangePt(danInfo.getUpgradePt() - user.getPt());
          user.setPt(info.getBasisPt());
          user.setDanValue(info.getDanValue());
        } else { // 未升级
          user.setPt(pt);
        }
      }
    }
    for (int i = 0; i < changeList.size(); i++) {
      User user = userList.get(i);
      UserDataChange change = changeList.get(i);
      change.setAfterPt(user.getPt());
      change.setAfterDanValue(user.getDanValue());
      if (!change.getBeforeDanValue().equals(change.getAfterDanValue())) {
        change.setRemark(
            DAN_VALUE_MAP.get(change.getBeforeDanValue()).getDanName()
                + " => "
                + DAN_VALUE_MAP.get(change.getAfterDanValue()).getDanName());
      }
    }

    this.saveBatch(changeList);
  }

  // 是否同桌
  private static boolean deskmate(List<User> userList) {
    return userList.stream()
            .map(item -> DAN_TITLE_MAP.get(item.getDanValue()))
            .collect(Collectors.toSet())
            .size()
        == 1;
  }

  private Integer getSeqPt(Integer seq, DanInfo danInfo, DanTitle danTitle) {
    int i = seq + 1;
    switch (i) {
      case 1:
        return danTitle.getTitleFirst();
      case 2:
        return danTitle.getTitleSecond();
      case 3:
        return danTitle.getTitleThird();
      case 4:
        return danInfo.getFourth();
    }
    throw StateEnum.ranking_not_correct.create().append("[").append(i + "").append("]");
  }

  private List<UserDataChange> initChangeList(
      Game game, ImportGame importGame, List<User> userList) {
    List<UserDataChange> changeList = new ArrayList<>();
    for (int i = 0; i < userList.size(); i++) {
      User user = userList.get(i);
      UserDataChange change = new UserDataChange();
      change.setUserId(user.getId());
      change.setGameId(game.getId());
      change.setBeforePt(user.getPt());
      change.setBeforeRate(user.getRate());
      change.setBeforeDanValue(user.getDanValue());
      change.setGameSeq(i + 1);
      change.setGamePoint(importGame.getPointList().get(i));
      changeList.add(change);
    }
    return changeList;
  }

  public Set<Integer> getMonthUser(int dateInt) {
    return baseMapper.getMonthUser(dateInt);
  }

  public List<UserDataChange> getMonthChangeList(Integer userId, int dateInt) {
    return baseMapper.getMonthChangeList(userId, dateInt);
  }

  public List<UserDataChange> getDayUserData(int userId, Date date) {
    int dateInt = Integer.parseInt(DateUtil.format(date, "yyyyMMdd"));
    return baseMapper.getDayUserData(userId, dateInt);
  }
}
