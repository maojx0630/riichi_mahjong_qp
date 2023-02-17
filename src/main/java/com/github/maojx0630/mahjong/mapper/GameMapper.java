package com.github.maojx0630.mahjong.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.maojx0630.mahjong.model.Game;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author 毛家兴
 * @since 2022/2/21 16:32
 */
@Repository
public interface GameMapper extends BaseMapper<Game> {

	@Select("select ifnull(max(id)+1,1) from game")
	Integer getNewId();
}
