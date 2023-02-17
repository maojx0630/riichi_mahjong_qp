package com.github.maojx0630.mahjong.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author 毛家兴
 * @since 2023/2/16 10:35
 */
@Data
public class Game {

  @TableId(value = "id", type = IdType.INPUT)
  private Integer id;

  @TableField("date_int")
  private Integer dateInt;

  @TableField("date_str")
  private String dateStr;

  @TableField("yi")
  private Integer yi;

  @TableField("yi_point")
  private Integer yiPoint;

  @TableField("er")
  private Integer er;

  @TableField("er_point")
  private Integer erPoint;

  @TableField("san")
  private Integer san;

  @TableField("san_point")
  private Integer sanPoint;

  @TableField("si")
  private Integer si;

  @TableField("si_point")
  private Integer siPoint;

  @TableField("name_group")
  private String nameGroup;
}
