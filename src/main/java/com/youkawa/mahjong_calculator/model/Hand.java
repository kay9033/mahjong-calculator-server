package com.youkawa.mahjong_calculator.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Hand {
  private List<Tile> tiles;
  private boolean isDealer;
  private boolean isRiichi;
  private boolean isTsumo;
  private List<String> specialConditions;
}
