package com.youkawa.mahjong_calculator.service;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.youkawa.mahjong_calculator.model.Question;

@Service
public class QuestionService {
  private final List<Question> questionList = List.of(
      new Question(1, List.of("混一色", "チャンタ"), Map.of("場風", "東", "自風", "南"), "ron"));

  public Question getRandomQuestion() {
    Random random = new Random();
    return questionList.get(random.nextInt(questionList.size()));
  }
}
