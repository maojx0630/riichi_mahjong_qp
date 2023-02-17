package com.github.maojx0630.mahjong.model;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @author 毛家兴
 * @since 2023/2/16 10:41
 */
@Data
public class UserDataChange {

	@TableField("user_id")
	private Integer userId;

	@TableField("game_id")
	private Integer gameId;

	@TableField("before_pt")
	private Integer beforePt;

	@TableField("after_pt")
	private Integer afterPt;

	@TableField("change_pt")
	private Integer changePt;

	@TableField("before_rate")
	private String beforeRate;

	@TableField("after_rate")
	private String afterRate;

	@TableField("change_rate")
	private String changeRate;

	@TableField("before_dan_value")
	private Integer beforeDanValue;

	@TableField("after_dan_value")
	private Integer afterDanValue;

	@TableField("game_seq")
	private Integer gameSeq;

	@TableField("game_point")
	private Integer gamePoint;

	@TableField("remark")
	private String remark;

}
