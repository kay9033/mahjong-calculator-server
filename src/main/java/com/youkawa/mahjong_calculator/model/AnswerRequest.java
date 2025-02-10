package com.youkawa.mahjong_calculator.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnswerRequest {
  private int questionId;
  private List<List<Tile>> tiles;
  private String winType;
}
