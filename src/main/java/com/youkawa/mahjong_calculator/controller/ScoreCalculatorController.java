package com.youkawa.mahjong_calculator.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.youkawa.mahjong_calculator.model.Hand;
import com.youkawa.mahjong_calculator.model.ScoreResult;
import com.youkawa.mahjong_calculator.service.ScoreCalculatorService;

@RestController
public class ScoreCalculatorController {

  private final ScoreCalculatorService scoreCalculatorService;

  public ScoreCalculatorController(ScoreCalculatorService scoreCalculatorService) {
    this.scoreCalculatorService = scoreCalculatorService;
  }

  @PostMapping("/calculate")
  public ScoreResult calculateScore(@RequestBody Hand hand) {
    return scoreCalculatorService.calculateScore(hand);
  }
}
