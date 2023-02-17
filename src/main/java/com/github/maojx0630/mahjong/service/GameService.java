package com.github.maojx0630.mahjong.service;

import cn.hutool.core.date.DateUtil;
import com.github.maojx0630.mahjong.common.base.BasicServiceImpl;
import com.github.maojx0630.mahjong.dto.ImportGame;
import com.github.maojx0630.mahjong.mapper.GameMapper;
import com.github.maojx0630.mahjong.model.Game;
import com.github.maojx0630.mahjong.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 毛家兴
 * @since 2022/2/21 16:41
 */
@Service
@Transactional(readOnly = true)
public class GameService extends BasicServiceImpl<GameMapper, Game> {
  public Game saveGame(ImportGame importGame, List<User> userList) {
    Integer newId = baseMapper.getNewId();
    Game game = new Game();
    game.setId(newId);
    game.setDateInt(Integer.parseInt(DateUtil.format(importGame.getDateTime(), "yyyyMMdd")));
    game.setDateStr(DateUtil.formatDate(importGame.getDateTime()));
    game.setYi(userList.get(0).getId());
    game.setYiPoint(importGame.getPointList().get(0));
    game.setEr(userList.get(1).getId());
    game.setErPoint(importGame.getPointList().get(1));
    game.setSan(userList.get(2).getId());
    game.setSanPoint(importGame.getPointList().get(2));
    game.setSi(userList.get(3).getId());
    game.setSiPoint(importGame.getPointList().get(3));
    StringBuilder sb = new StringBuilder(",");
    for (User user : userList) {
      sb.append(user.getName()).append(",");
    }
    game.setNameGroup(sb.toString());
    this.save(game);
    return game;
  }
}
