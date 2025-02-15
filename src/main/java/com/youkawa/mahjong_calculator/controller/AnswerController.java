package com.youkawa.mahjong_calculator.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.youkawa.mahjong_calculator.model.AnswerRequest;
import com.youkawa.mahjong_calculator.model.AnswerResponse;
import com.youkawa.mahjong_calculator.service.AnswerService;

@RestController
public class AnswerController {
  private final AnswerService answerService;

  private AnswerController(AnswerService answerService) {
    this.answerService = answerService;
  }

  @PostMapping("/answer")
  public AnswerResponse checkAnswer(@RequestBody AnswerRequest request) {
    return answerService.checkAnswer(request);
  }

}
