package com.github.maojx0630.mahjong.service;

import com.github.maojx0630.mahjong.dto.ImportGame;
import com.github.maojx0630.mahjong.model.Game;
import com.github.maojx0630.mahjong.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 毛家兴
 * @since 2023/2/16 11:36
 */
@Service
@AllArgsConstructor
public class ImportService {

  private GameService gameService;

  private UserService userService;

  private UserDataChangeService changeService;

  @Transactional
  public void importData(List<ImportGame> list) {
    for (ImportGame importGame : list) {
      // 处理点数和不正确
      int sum = importGame.getPointList().stream().mapToInt(item -> item).sum();
      if (sum != 1000) {
        if (sum > 1000) {
          int i = sum - 1000;
          Integer integer = importGame.getPointList().get(3);
          int i1 = integer - i;
          importGame.getPointList().set(3, i1);
        } else {
          int i = 1000 - sum;
          Integer integer = importGame.getPointList().get(0);
          int i1 = i + integer;
          importGame.getPointList().set(0, i1);
        }
      }
      List<User> userList = userService.getUserList(importGame);
      Game game = gameService.saveGame(importGame, userList);

      changeService.toChange(game, importGame, userList);
      for (User user : userList) {
        userService
            .lambdaUpdate()
            .set(User::getPt, user.getPt())
            .set(User::getRate, user.getRate())
            .set(User::getDanValue, user.getDanValue())
            .eq(User::getId, user.getId())
            .update();
      }
    }
  }
}
