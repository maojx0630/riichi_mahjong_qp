package com.github.maojx0630.mahjong.service;

import cn.hutool.core.date.DateUtil;
import com.github.maojx0630.mahjong.common.base.BasicServiceImpl;
import com.github.maojx0630.mahjong.dto.ImportGame;
import com.github.maojx0630.mahjong.mapper.UserMapper;
import com.github.maojx0630.mahjong.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 毛家兴
 * @since 2022/2/21 16:41
 */
@Service
@Transactional(readOnly = true)
public class UserService extends BasicServiceImpl<UserMapper, User> {
  public List<User> getUserList(ImportGame importGame) {
    List<User> list = new ArrayList<>();
    for (String s : importGame.getUserName()) {
      User one = lambdaQuery().eq(User::getName, s).one();
      if (one == null) {
        User user = new User();
        user.setId(baseMapper.getNewId());
        user.setName(s);
        user.setDanValue(0);
        user.setRate("1500");
        user.setPt(0);
        user.setRegisterDate(DateUtil.formatDate(importGame.getDateTime()));
        baseMapper.insert(user);
        list.add(user);
      } else {
        list.add(one);
      }
    }
    return list;
  }

  public List<User> getRankUserList() {
    return baseMapper.getRankUserList();
  }
}
