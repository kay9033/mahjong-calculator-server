package com.youkawa.mahjong_calculator.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.youkawa.mahjong_calculator.model.AnswerRequest;
import com.youkawa.mahjong_calculator.model.AnswerResponse;
import com.youkawa.mahjong_calculator.model.Question;
import com.youkawa.mahjong_calculator.model.Tile;

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

    if (!iswinningHand(request)) {
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

  private boolean iswinningHand(AnswerRequest request) {
    if (isTitoitsu(request)) {
      return true;
    }

    int mentsuSize = 0;
    for (List<Tile> meld : request.getTiles()) {
      if (isKoutsu(meld) || isShuntsu(meld)) {
        mentsuSize++;
      }
    }
    return hasJantou(request) && mentsuSize == 4;
  }

  private List<String> detectYaku(AnswerRequest request) {
    List<String> detectedYaku = new ArrayList<>();

    if (isTitoitsu(request)) {
      detectedYaku.add("七対子");
    } else {
      if (isToitoi(request)) {
        detectedYaku.add("対々和");
      }

      if (isPinfu(request)) {
        detectedYaku.add("平和");
      }

      if (isTanyao(request)) {
        detectedYaku.add("断么九");
      }

      if (isChanta(request)) {
        detectedYaku.add("チャンタ");
      }

      if (isJunchan(request)) {
        detectedYaku.add("純チャン");
      }
    }

    if (isHonitsu(request)) {
      detectedYaku.add("混一色");
    }

    if (isChinitsu(request)) {
      detectedYaku.add("清一色");
    }

    return detectedYaku;
  }

  private boolean hasJantou(AnswerRequest request) {
    for (List<Tile> meld : request.getTiles()) {
      if (meld.size() == 2) {
        if (meld.get(0).equals(meld.get(1))) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean isKoutsu(List<Tile> meld) {
    if (meld.size() != 3) {
      return false;
    }
    boolean isSameSuit = meld.get(0).getSuit().equals(meld.get(1).getSuit())
        && meld.get(1).getSuit().equals(meld.get(2).getSuit());

    boolean isSameNumber = meld.get(0).getNumber() == meld.get(1).getNumber()
        && meld.get(1).getNumber() == meld.get(2).getNumber();

    return isSameSuit && isSameNumber;
  }

  private boolean isShuntsu(List<Tile> meld) {
    if (meld.size() != 3) {
      return false;
    }

    meld.sort(Comparator.comparing(Tile::getNumber));

    Boolean isShupai = !meld.get(0).getSuit().equals("z");

    Boolean isSameSuit = meld.get(0).getSuit().equals(meld.get(1).getSuit())
        && meld.get(1).getSuit().equals(meld.get(2).getSuit());

    Boolean isConsective = meld.get(0).getNumber() + 1 == meld.get(1).getNumber()
        && meld.get(1).getNumber() + 1 == meld.get(2).getNumber();

    return isShupai && isSameSuit && isConsective;
  }

  private boolean hasNaki(Tile tile) {
    return tile.getNakiType() != null;
  }

  private boolean isYaochu(Tile tile) {
    return tile.getSuit().equals("z") || tile.getNumber() == 1 || tile.getNumber() == 9;
  }

  private boolean isHonitsu(AnswerRequest request) {
    boolean isHonor = false;
    String suit = null;
    for (List<Tile> meld : request.getTiles()) {
      for (Tile tile : meld) {
        if (tile.getSuit().equals("z")) {
          isHonor = true;
        } else {
          if (suit == null) {
            suit = tile.getSuit();
          } else if (!tile.getSuit().equals(suit)) {
            return false;
          }
        }
      }
    }
    return isHonor;
  }

  private boolean isChinitsu(AnswerRequest request) {
    String suit = null;
    for (List<Tile> meld : request.getTiles()) {
      for (Tile tile : meld) {
        if (tile.getSuit().equals("z")) {
          return false;
        } else {
          if (suit == null) {
            suit = tile.getSuit();
          } else if (!tile.getSuit().equals(suit)) {
            return false;
          }
        }
      }
    }
    return true;
  }

  private boolean isChanta(AnswerRequest request) {
    for (List<Tile> meld : request.getTiles()) {
      boolean hasYaochu = false;
      for (Tile tile : meld) {
        if (isYaochu(tile)) {
          hasYaochu = true;
        }
      }
      if (!hasYaochu) {
        return false;
      }
    }
    return true;
  }

  private boolean isJunchan(AnswerRequest request) {
    for (List<Tile> meld : request.getTiles()) {
      boolean hasYaochu = false;
      for (Tile tile : meld) {
        if (isYaochu(tile)) {
          hasYaochu = true;
        }
        if (tile.getSuit().equals("z")) {
          return false;
        }
      }
      if (!hasYaochu) {
        return false;
      }
    }
    return true;
  }

  private boolean isToitoi(AnswerRequest request) {
    for (List<Tile> meld : request.getTiles()) {
      if (meld.size() == 3) {
        if (!isKoutsu(meld)) {
          return false;
        }
      }
    }
    return true;
  }

  private boolean isPinfu(AnswerRequest request) {
    for (List<Tile> meld : request.getTiles()) {
      if (meld.size() == 3) {
        if (!isShuntsu(meld)) {
          return false;
        }
        for (Tile tile : meld) {
          if (hasNaki(tile)) {
            return false;
          }
        }
      }
      if (meld.size() == 2) {
        if (meld.get(0).getSuit().equals("z")) {
          return false;
        }
      }
    }
    return true;
  }

  private boolean isTitoitsu(AnswerRequest request) {
    if (request.getTiles().size() != 7) {
      return false;
    }
    for (List<Tile> meld : request.getTiles()) {
      if (!(meld.size() == 2 && meld.get(0).equals(meld.get(1)))) {
        return false;
      }
    }
    return true;
  }

  private boolean isTanyao(AnswerRequest request) {
    for (List<Tile> meld : request.getTiles()) {
      for (Tile tile : meld) {
        if (isYaochu(tile)) {
          return false;
        }
      }
    }
    return true;
  }

}
