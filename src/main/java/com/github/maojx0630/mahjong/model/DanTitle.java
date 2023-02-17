package com.github.maojx0630.mahjong.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 段位称号信息
 *
 * @author 毛家兴
 * @since 2022/2/21 16:32
 */
@Data
@TableName("dan_title")
public class DanTitle {

  /** 段位顺序 */
  @TableId(value = "title_value", type = IdType.INPUT)
  private Integer titleValue;

  /** 段位名称 */
  @TableField("title_name")
  private String titleName;

  @TableField("title_first")
  private Integer titleFirst;

  @TableField("title_second")
  private Integer titleSecond;

  @TableField("title_third")
  private Integer titleThird;
}
