package com.github.maojx0630.mahjong.monthly;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 毛家兴
 * @since 2023/2/16 09:01
 */
@Data
@NoArgsConstructor
public class MonthlyPoint {

  private String name;

  private List<Integer> points;

  private Double sumPoint;

  public MonthlyPoint(String name) {
    this.name = name;
    this.points = new ArrayList<>();
  }
}
