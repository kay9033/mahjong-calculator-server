package com.youkawa.mahjong_calculator.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.youkawa.mahjong_calculator.model.Hand;
import com.youkawa.mahjong_calculator.model.ScoreResult;

@Service
public class ScoreCalculatorService {
  public ScoreResult calculateScore(Hand hand) {
    int han = calculateHan(hand);
    int fu = caluculateFu(hand);
    List<String> yaku = new ArrayList<>(List.of("リーチ", "ツモ"));
    int totalScore = caluculateTotalScore(han, fu, hand.isIdDealer());

    return new ScoreResult(han, fu, totalScore, yaku);
  }

  private int calculateHan(Hand hand) {
    int han = 0;
    if (hand.isRiichi()) {
      han++;
    }
    if (hand.isTsumo()) {
      han++;
    }
    if (hand.getSpecialConditions().contains("haitei")) {
      han++;
    }
    return han;
  }

  private int caluculateFu(Hand hand) {
    int fu = 20;
    if (hand.isRiichi()) {
      fu += 2;
    }
    return fu;
  }

  private List<String> detectYaku(Hand hand) {
    List<String> yaku = new ArrayList<>();
    if (hand.isRiichi()) {
      yaku.add("リーチ");
    }
    if (hand.isTsumo()) {
      yaku.add("ツモ");
    }
    if (hand.getSpecialConditions().contains("haitei")) {
      yaku.add("海底");
    }
    return yaku;
  }

  private int caluculateTotalScore(int han, int fu, boolean isDealer) {
    int basePoints = fu * (int) Math.pow(2, han + 2);
    return isDealer ? basePoints * 4 : basePoints * 6;
  }
}
