package com.youkawa.mahjong_calculator.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScoreResult {
  private int han;
  private int fu;
  private int totalScore;
  private List<String> yaku;
}
