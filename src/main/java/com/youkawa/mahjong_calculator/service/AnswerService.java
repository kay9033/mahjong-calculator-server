package com.youkawa.mahjong_calculator.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.youkawa.mahjong_calculator.model.AnswerRequest;
import com.youkawa.mahjong_calculator.model.AnswerResponse;

@Service
public class AnswerService {
  private final YakuService yakuService;

  public AnswerService(QuestionService questionService, YakuService yakuService) {
    this.yakuService = yakuService;
  }

  public AnswerResponse checkAnswer(AnswerRequest request) {

    if (!yakuService.iswinningHand(request.getTiles())) {
      return new AnswerResponse(false, null, null, null, "和了形になっていません");
    }

    List<String> userYaku = yakuService.checkYaku(request.getTiles());
    List<String> missingYaku = new ArrayList<>();
    List<String> extraYaku = new ArrayList<>();

    missingYaku = request.getYakuList().stream().filter(yaku -> !userYaku.contains(yaku)).toList();
    extraYaku = userYaku.stream().filter(yaku -> !request.getYakuList().contains(yaku)).toList();

    boolean correct = missingYaku.isEmpty() && extraYaku.isEmpty();
    String message = correct ? "正解です！" : "お題に合っていません。";

    return new AnswerResponse(correct, missingYaku, extraYaku, userYaku, message);

  }

}
