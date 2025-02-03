package com.youkawa.mahjong_calculator.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.youkawa.mahjong_calculator.model.Hand;
import com.youkawa.mahjong_calculator.model.ScoreResult;

@Service
public class ScoreCalculatorService {
  public ScoreResult calculateScore(Hand hand) {
    int han = 2;
    int fu = 30;
    List<String> yaku = new ArrayList<>(List.of("リーチ", "ツモ"));
    int totalScore = 2000;

    return new ScoreResult(han, fu, totalScore, yaku);
  }
}
