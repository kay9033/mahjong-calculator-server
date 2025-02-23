package com.youkawa.mahjong_calculator.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.youkawa.mahjong_calculator.model.AnswerRequest;
import com.youkawa.mahjong_calculator.model.Tile;

@Service
public class YakuService {

  public boolean iswinningHand(AnswerRequest request) {
    if (hasTitoitsu(request)) {
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

  private boolean isSangenpai(Tile tile) {
    return tile.getSuit().equals("z") && (tile.getNumber() == 5 || tile.getNumber() == 6 || tile.getNumber() == 7);
  }

  public boolean hasHonitsu(AnswerRequest request) {
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

  public boolean hasChinitsu(AnswerRequest request) {
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

  public boolean hasChanta(AnswerRequest request) {
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

  public boolean hasJunchan(AnswerRequest request) {
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

  public boolean hasToitoi(AnswerRequest request) {
    for (List<Tile> meld : request.getTiles()) {
      if (meld.size() == 3) {
        if (!isKoutsu(meld)) {
          return false;
        }
      }
    }
    return true;
  }

  public boolean hasPinfu(AnswerRequest request) {
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

  public boolean hasTitoitsu(AnswerRequest request) {
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

  public boolean hasTanyao(AnswerRequest request) {
    for (List<Tile> meld : request.getTiles()) {
      for (Tile tile : meld) {
        if (isYaochu(tile)) {
          return false;
        }
      }
    }
    return true;
  }

  public boolean hasHaku(AnswerRequest request) {
    boolean hasHaku = false;
    for (List<Tile> meld : request.getTiles()) {
      if (isKoutsu(meld) && meld.get(0).getSuit().equals("z") && meld.get(0).getNumber() == 5) {
        hasHaku = true;
      }
    }
    return hasHaku;
  }

  public boolean hasHatsu(AnswerRequest request) {
    boolean hasHatsu = false;
    for (List<Tile> meld : request.getTiles()) {
      if (isKoutsu(meld) && meld.get(0).getSuit().equals("z") && meld.get(0).getNumber() == 6) {
        hasHatsu = true;
      }
    }
    return hasHatsu;
  }

  public boolean hasChun(AnswerRequest request) {
    boolean hasChun = false;
    for (List<Tile> meld : request.getTiles()) {
      if (isKoutsu(meld) && meld.get(0).getSuit().equals("z") && meld.get(0).getNumber() == 7) {
        hasChun = true;
      }
    }
    return hasChun;
  }

  public boolean hasIipeikou(AnswerRequest request) {
    Map<String, Long> shuntsuCount = new HashMap<>();
    for (List<Tile> meld : request.getTiles()) {
      if (isShuntsu(meld)) {
        String key = meld.get(0).getSuit() + meld.get(0).getNumber();
        shuntsuCount.put(key, shuntsuCount.getOrDefault(key, 0L) + 1);
      }
    }
    return shuntsuCount.values().stream().filter(count -> count >= 2).count() == 1;
  }

  public boolean hasRyanpeikou(AnswerRequest request) {
    Map<String, Long> shuntsuCount = new HashMap<>();
    for (List<Tile> meld : request.getTiles()) {
      if (isShuntsu(meld)) {
        String key = meld.get(0).getSuit() + meld.get(0).getNumber();
        shuntsuCount.put(key, shuntsuCount.getOrDefault(key, 0L) + 1);
      }
    }
    return shuntsuCount.values().stream().filter(count -> count >= 2).count() == 2;
  }

  public boolean hasIttsu(AnswerRequest request) {
    Map<String, Set<Integer>> shuntsues = new HashMap<>();
    for (List<Tile> meld : request.getTiles()) {
      if (isShuntsu(meld)) {
        shuntsues.putIfAbsent(meld.get(0).getSuit(), new HashSet<>());
        shuntsues.get(meld.get(0).getSuit()).add(meld.get(0).getNumber());
      }
    }
    return shuntsues.values().stream().anyMatch(set -> set.contains(1) && set.contains(4) && set.contains(7));
  }

  public boolean hasSanAnkou(AnswerRequest request) {
    int ankouCount = 0;
    for (List<Tile> tiles : request.getTiles()) {
      if (tiles.stream().allMatch(tile -> !hasNaki(tile))) {
        ankouCount++;
      }
    }
    return ankouCount == 3;
  }

  public boolean hasSanshokudoukou(AnswerRequest request) {
    List<List<Tile>> ankouList = request.getTiles().stream().filter(tiles -> isKoutsu(tiles)).toList();

    if (ankouList.size() < 3) {
      return false;
    }

    int number = ankouList.get(0).get(0).getNumber();
    return number == ankouList.get(1).get(0).getNumber() && number == ankouList.get(2).get(0).getNumber();
  }

  public boolean hasSanshokudoujun(AnswerRequest request) {
    List<List<Tile>> shuntsuList = request.getTiles().stream().filter(tiles -> isShuntsu(tiles)).toList();

    if (shuntsuList.size() < 3) {
      return false;
    }

    int number = shuntsuList.get(0).get(0).getNumber();
    String suit1 = shuntsuList.get(0).get(0).getSuit();
    String suit2 = shuntsuList.get(1).get(0).getSuit();
    String suit3 = shuntsuList.get(2).get(0).getSuit();

    return number == shuntsuList.get(1).get(0).getNumber()
        && number == shuntsuList.get(2).get(0).getNumber()
        && !suit1.equals(suit2) && !suit1.equals(suit3) && !suit2.equals(suit3);
  }

  public boolean hasHonroutou(AnswerRequest request) {
    List<List<Tile>> tileList = request.getTiles();
    boolean hasChanchu = false;
    for (List<Tile> meld : tileList) {
      for (Tile tile : meld) {
        if (!isYaochu(tile)) {
          hasChanchu = true;
        }
      }
    }
    return !hasChanchu;
  }

  public boolean hasShousangen(AnswerRequest request) {
    int sangenCount = 0;
    for (List<Tile> meld : request.getTiles()) {
      for (Tile tile : meld) {
        if (isSangenpai(tile)) {
          sangenCount++;
        }
      }
    }
    return sangenCount == 8;
  }

  public boolean hasSankantsu(AnswerRequest request) {
    int nakiCount = 0;
    for (List<Tile> meld : request.getTiles()) {
      for (Tile tile : meld) {
        if (hasNaki(tile)) {
          nakiCount++;
        }
      }
    }
    return nakiCount == 3;
  }

}
