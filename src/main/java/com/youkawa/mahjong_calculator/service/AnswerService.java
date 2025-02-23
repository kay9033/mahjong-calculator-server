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
  private final YakuService yakuService;

  public AnswerService(QuestionService questionService, YakuService yakuService) {
    this.questionService = questionService;
    this.yakuService = yakuService;
  }

  public AnswerResponse checkAnswer(AnswerRequest request) {
    Question question = questionService.getQuestionById(request.getQuestionId());
    if (question == null) {
      return new AnswerResponse(false, null, null, null, "お題が見つかりません");
    }

    if (!yakuService.iswinningHand(request)) {
      return new AnswerResponse(false, null, null, null, "和了形になっていません");
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

    if (yakuService.hasTitoitsu(request)) {
      detectedYaku.add("七対子");
    } else {
      if (yakuService.hasToitoi(request)) {
        detectedYaku.add("対々和");
      }

      if (yakuService.hasPinfu(request)) {
        detectedYaku.add("平和");
      }

      if (yakuService.hasTanyao(request)) {
        detectedYaku.add("断么九");
      }

      if (yakuService.hasChanta(request)) {
        detectedYaku.add("チャンタ");
      }

      if (yakuService.hasJunchan(request)) {
        detectedYaku.add("純チャン");
      }

      if (yakuService.hasHaku(request)) {
        detectedYaku.add("白");
      }

      if (yakuService.hasHatsu(request)) {
        detectedYaku.add("發");
      }

      if (yakuService.hasChun(request)) {
        detectedYaku.add("中");
      }

      if (yakuService.hasIipeikou(request)) {
        detectedYaku.add("一盃口");
      }

      if (yakuService.hasRyanpeikou(request)) {
        detectedYaku.add("二盃口");
      }

      if (yakuService.hasIttsu(request)) {
        detectedYaku.add("一気通貫");
      }

      if (yakuService.hasSanAnkou(request)) {
        detectedYaku.add("三暗刻");
      }

      if (yakuService.hasSanshokudoujun(request)) {
        detectedYaku.add("三色同順");
      }

      if (yakuService.hasSanshokudoukou(request)) {
        detectedYaku.add("三色同刻");
      }

      if (yakuService.hasShousangen(request)) {
        detectedYaku.add("小三元");
      }

      if (yakuService.hasSankantsu(request)) {
        detectedYaku.add("三槓子");
      }

    }

    if (yakuService.hasHonitsu(request)) {
      detectedYaku.add("混一色");
    }

    if (yakuService.hasChinitsu(request)) {
      detectedYaku.add("清一色");
    }

    if (yakuService.hasHonroutou(request)) {
      detectedYaku.add("混老頭");
    }

    return detectedYaku;
  }

}
