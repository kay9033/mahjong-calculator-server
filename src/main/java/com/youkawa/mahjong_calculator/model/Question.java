package com.youkawa.mahjong_calculator.model;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Question {
  private int id;
  private List<String> yaku;
  private Map<String, String> conditions;
  private String winType;
}
