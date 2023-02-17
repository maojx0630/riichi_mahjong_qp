package com.github.maojx0630.mahjong.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.maojx0630.mahjong.model.UserDataChange;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author 毛家兴
 * @since 2022/2/21 16:32
 */
@Repository
public interface UserDataChangeMapper extends BaseMapper<UserDataChange> {

  @Select(
      "SELECT DISTINCT "
          + " a.user_id  "
          + "FROM "
          + " user_data_change a "
          + " LEFT JOIN game b ON b.id = a.game_id  "
          + "WHERE "
          + " b.date_int/100 = #{dateInt}")
  Set<Integer> getMonthUser(int dateInt);

  @Select(
      "SELECT "
          + " a.*  "
          + "FROM "
          + " user_data_change a "
          + " LEFT JOIN game b ON b.id = a.game_id "
          + " where b.date_int/100 = #{dateInt} and a.user_id=#{userId} "
          + " ORDER BY a.game_id "
          + " LIMIT 20")
  List<UserDataChange> getMonthChangeList(
      @Param("userId") Integer userId, @Param("dateInt") int dateInt);

  @Select(
      "SELECT "
          + " a.*  "
          + "FROM "
          + " user_data_change a "
          + " LEFT JOIN game b ON b.id = a.game_id  "
          + " WHERE  b.date_int = #{dateInt}  "
          + " AND a.user_id = #{userId}  "
          + "ORDER BY a.game_id")
  List<UserDataChange> getDayUserData(@Param("userId") int userId, @Param("dateInt") int dateInt);
}
