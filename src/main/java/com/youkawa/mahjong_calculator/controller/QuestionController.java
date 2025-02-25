package com.youkawa.mahjong_calculator.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youkawa.mahjong_calculator.model.Question;
import com.youkawa.mahjong_calculator.service.QuestionService;

@RestController
public class QuestionController {
  private final QuestionService questionService;

  public QuestionController(QuestionService questionService) {
    this.questionService = questionService;
  }

  @GetMapping("/question")
  public Question getQuestion() {
    return questionService.generaQuestion();
  }
}
