package com.github.maojx0630.mahjong.dto;

import cn.hutool.core.date.DateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 毛家兴
 * @since 2023/2/16 08:46
 */
@Data
@NoArgsConstructor
public class ImportGame {

  private DateTime dateTime;

  private List<String> userName;

  private List<Integer> pointList;
}
