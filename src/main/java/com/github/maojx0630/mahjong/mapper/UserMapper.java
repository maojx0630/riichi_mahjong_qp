package com.github.maojx0630.mahjong.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.maojx0630.mahjong.model.User;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 毛家兴
 * @since 2022/2/21 16:32
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

	@Select("select ifnull(max(id)+1,1) from user")
	Integer getNewId();

	@Select("SELECT * FROM user ORDER BY dan_value DESC, pt DESC, rate DESC, id")
	List<User> getRankUserList();

}
