package com.youkawa.mahjong_calculator.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Tile {
  private String suit;
  private int number;
  private String nakiType;
}
