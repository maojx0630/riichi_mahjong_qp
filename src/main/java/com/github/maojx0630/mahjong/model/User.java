package com.github.maojx0630.mahjong.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author 毛家兴
 * @since 2023/2/16 10:33
 */
@Data
public class User {

  @TableId(value = "id", type = IdType.INPUT)
  private Integer id;

  @TableField("name")
  private String name;

  @TableField("dan_value")
  private Integer danValue;

  @TableField("rate")
  private String rate;

  @TableField("pt")
  private Integer pt;

  @TableField("register_date")
  private String registerDate;
}
