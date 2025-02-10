package com.youkawa.mahjong_calculator.service;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.youkawa.mahjong_calculator.model.Question;

@Service
public class QuestionService {
  private final List<Question> questionList = List.of(
      new Question(1, List.of("混一色", "チャンタ"),
          Map.of("場風", "東", "自風", "南"), "ron"),
      new Question(2, List.of("平和", "一盃口"),
          Map.of("場風", "南", "自風", "西"), "tsumo"),
      new Question(3, List.of("対々和", "役牌"),
          Map.of("場風", "西", "自風", "北"), "ron"));

  public Question getRandomQuestion() {
    Random random = new Random();
    return questionList.get(random.nextInt(questionList.size()));
  }
}
