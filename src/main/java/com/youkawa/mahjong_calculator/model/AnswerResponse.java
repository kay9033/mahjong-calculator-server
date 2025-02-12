package com.youkawa.mahjong_calculator.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnswerResponse {
  private boolean correct;
  private List<String> missingYaku;
  private List<String> extraYaku;
  private List<String> yaku;
  private String message;
}
