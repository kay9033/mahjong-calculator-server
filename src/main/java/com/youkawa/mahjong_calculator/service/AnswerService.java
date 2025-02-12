package com.youkawa.mahjong_calculator.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.youkawa.mahjong_calculator.model.AnswerRequest;
import com.youkawa.mahjong_calculator.model.AnswerResponse;
import com.youkawa.mahjong_calculator.model.Question;

@Service
public class AnswerService {
  private final QuestionService questionService;

  public AnswerService(QuestionService questionService) {
    this.questionService = questionService;
  }

  public AnswerResponse checkAnswer(AnswerRequest request) {
    Question question = questionService.getQuestionById(request.getQuestionId());
    if (question == null) {
      return new AnswerResponse(false, null, null, null, "お題が見つかりません");
    }

    List<String> missingYaku = new ArrayList<>();
    List<String> extraYaku = new ArrayList<>();
    List<String> userYaku = detectYaku(request);

    for (String yaku : question.getYaku()) {
      if (!userYaku.contains(yaku)) {
        missingYaku.add(yaku);
      }
    }
    for (String yaku : userYaku) {
      if (!question.getYaku().contains(yaku)) {
        extraYaku.add(yaku);
      }
    }

    boolean correct = missingYaku.isEmpty() && extraYaku.isEmpty();
    String message = correct ? "正解です！" : "お題に合っていません。";

    return new AnswerResponse(correct, missingYaku, extraYaku, userYaku, message);

  }

  private List<String> detectYaku(AnswerRequest request) {
    List<String> detectedYaku = new ArrayList<>();

    if (isHonits(request)) {
      detectedYaku.add("混一色");
    }

    return detectedYaku;
  }

  private boolean isHonits(AnswerRequest request) {
    return true;
  }

}
