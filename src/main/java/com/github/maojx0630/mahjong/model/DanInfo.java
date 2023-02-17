package com.github.maojx0630.mahjong.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 段位基础信息
 *
 * @author 毛家兴
 * @since 2022/2/21 16:32
 */
@Data
@TableName("dan_info")
public class DanInfo {

  /** 段位顺序 */
  @TableId(value = "dan_value", type = IdType.INPUT)
  private Integer danValue;

  /** 段位名称 */
  @TableField("dan_name")
  private String danName;

  /** 称号ID */
  @TableField("title_value")
  private String titleValue;

  /** 升级后基础点数 */
  @TableField("basis_pt")
  private Integer basisPt;

  /** 吃4点数 */
  @TableField("fourth")
  private Integer fourth;

  /** 是否降级 0 降级 1 不降级 */
  @TableField("demotion")
  private Integer demotion;

  /** 升级需要pt */
  @TableField("upgrade_pt")
  private Integer upgradePt;

  /** 平均rate */
  @TableField("average_rate")
  private Integer averageRate;
}
