package com.youkawa.mahjong_calculator.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.youkawa.mahjong_calculator.model.Tile;

@Service
public class YakuService {

  public boolean iswinningHand(List<List<Tile>> hand) {
    if (hasTitoitsu(hand)) {
      return true;
    }

    int mentsuSize = 0;
    for (List<Tile> meld : hand) {
      if (isKoutsu(meld) || isShuntsu(meld)) {
        mentsuSize++;
      }
    }
    return hasJantou(hand) && mentsuSize == 4;
  }

  private boolean hasJantou(List<List<Tile>> hand) {
    for (List<Tile> meld : hand) {
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

  public boolean hasHonitsu(List<List<Tile>> hand) {
    boolean isHonor = false;
    String suit = null;
    for (List<Tile> meld : hand) {
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

  public boolean hasChinitsu(List<List<Tile>> hand) {
    String suit = null;
    for (List<Tile> meld : hand) {
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

  public boolean hasChanta(List<List<Tile>> hand) {
    for (List<Tile> meld : hand) {
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

  public boolean hasJunchan(List<List<Tile>> hand) {
    for (List<Tile> meld : hand) {
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

  public boolean hasToitoi(List<List<Tile>> hand) {
    for (List<Tile> meld : hand) {
      if (meld.size() == 3) {
        if (!isKoutsu(meld)) {
          return false;
        }
      }
    }
    return true;
  }

  public boolean hasPinfu(List<List<Tile>> hand) {
    for (List<Tile> meld : hand) {
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

  public boolean hasTitoitsu(List<List<Tile>> hand) {
    if (hand.size() != 7) {
      return false;
    }
    for (List<Tile> meld : hand) {
      if (!(meld.size() == 2 && meld.get(0).equals(meld.get(1)))) {
        return false;
      }
    }
    return true;
  }

  public boolean hasTanyao(List<List<Tile>> hand) {
    for (List<Tile> meld : hand) {
      for (Tile tile : meld) {
        if (isYaochu(tile)) {
          return false;
        }
      }
    }
    return true;
  }

  public boolean hasHaku(List<List<Tile>> hand) {
    boolean hasHaku = false;
    for (List<Tile> meld : hand) {
      if (isKoutsu(meld) && meld.get(0).getSuit().equals("z") && meld.get(0).getNumber() == 5) {
        hasHaku = true;
      }
    }
    return hasHaku;
  }

  public boolean hasHatsu(List<List<Tile>> hand) {
    boolean hasHatsu = false;
    for (List<Tile> meld : hand) {
      if (isKoutsu(meld) && meld.get(0).getSuit().equals("z") && meld.get(0).getNumber() == 6) {
        hasHatsu = true;
      }
    }
    return hasHatsu;
  }

  public boolean hasChun(List<List<Tile>> hand) {
    boolean hasChun = false;
    for (List<Tile> meld : hand) {
      if (isKoutsu(meld) && meld.get(0).getSuit().equals("z") && meld.get(0).getNumber() == 7) {
        hasChun = true;
      }
    }
    return hasChun;
  }

  public boolean hasIipeikou(List<List<Tile>> hand) {
    Map<String, Long> shuntsuCount = new HashMap<>();
    for (List<Tile> meld : hand) {
      if (isShuntsu(meld)) {
        String key = meld.get(0).getSuit() + meld.get(0).getNumber();
        shuntsuCount.put(key, shuntsuCount.getOrDefault(key, 0L) + 1);
      }
    }
    return shuntsuCount.values().stream().filter(count -> count >= 2).count() == 1;
  }

  public boolean hasRyanpeikou(List<List<Tile>> hand) {
    Map<String, Long> shuntsuCount = new HashMap<>();
    for (List<Tile> meld : hand) {
      if (isShuntsu(meld)) {
        String key = meld.get(0).getSuit() + meld.get(0).getNumber();
        shuntsuCount.put(key, shuntsuCount.getOrDefault(key, 0L) + 1);
      }
    }
    return shuntsuCount.values().stream().filter(count -> count >= 2).count() == 2;
  }

  public boolean hasIttsu(List<List<Tile>> hand) {
    Map<String, Set<Integer>> shuntsues = new HashMap<>();
    for (List<Tile> meld : hand) {
      if (isShuntsu(meld)) {
        shuntsues.putIfAbsent(meld.get(0).getSuit(), new HashSet<>());
        shuntsues.get(meld.get(0).getSuit()).add(meld.get(0).getNumber());
      }
    }
    return shuntsues.values().stream().anyMatch(set -> set.contains(1) && set.contains(4) && set.contains(7));
  }

  public boolean hasSanAnkou(List<List<Tile>> hand) {
    int ankouCount = 0;
    for (List<Tile> tiles : hand) {
      if (tiles.stream().allMatch(tile -> !hasNaki(tile))) {
        ankouCount++;
      }
    }
    return ankouCount == 3;
  }

  public boolean hasSanshokudoukou(List<List<Tile>> hand) {
    List<List<Tile>> ankouList = hand.stream().filter(tiles -> isKoutsu(tiles)).toList();

    if (ankouList.size() < 3) {
      return false;
    }

    int number = ankouList.get(0).get(0).getNumber();
    return number == ankouList.get(1).get(0).getNumber() && number == ankouList.get(2).get(0).getNumber();
  }

  public boolean hasSanshokudoujun(List<List<Tile>> hand) {
    List<List<Tile>> shuntsuList = hand.stream().filter(tiles -> isShuntsu(tiles)).toList();

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

  public boolean hasHonroutou(List<List<Tile>> hand) {
    List<List<Tile>> tileList = hand;
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

  public boolean hasShousangen(List<List<Tile>> hand) {
    int sangenCount = 0;
    for (List<Tile> meld : hand) {
      for (Tile tile : meld) {
        if (isSangenpai(tile)) {
          sangenCount++;
        }
      }
    }
    return sangenCount == 8;
  }

  public boolean hasSankantsu(List<List<Tile>> hand) {
    int nakiCount = 0;
    for (List<Tile> meld : hand) {
      for (Tile tile : meld) {
        if (hasNaki(tile)) {
          nakiCount++;
        }
      }
    }
    return nakiCount == 3;
  }

  public List<String> checkYaku(List<List<Tile>> hand) {
    List<String> detectedYaku = new ArrayList<>();

    if (hasTitoitsu(hand)) {
      detectedYaku.add("七対子");
    } else {
      if (hasToitoi(hand)) {
        detectedYaku.add("対々和");
      }

      if (hasPinfu(hand)) {
        detectedYaku.add("平和");
      }

      if (hasTanyao(hand)) {
        detectedYaku.add("断么九");
      }

      if (hasChanta(hand)) {
        detectedYaku.add("チャンタ");
      }

      if (hasJunchan(hand)) {
        detectedYaku.add("純チャン");
      }

      if (hasHaku(hand)) {
        detectedYaku.add("白");
      }

      if (hasHatsu(hand)) {
        detectedYaku.add("發");
      }

      if (hasChun(hand)) {
        detectedYaku.add("中");
      }

      if (hasIipeikou(hand)) {
        detectedYaku.add("一盃口");
      }

      if (hasRyanpeikou(hand)) {
        detectedYaku.add("二盃口");
      }

      if (hasIttsu(hand)) {
        detectedYaku.add("一気通貫");
      }

      if (hasSanAnkou(hand)) {
        detectedYaku.add("三暗刻");
      }

      if (hasSanshokudoujun(hand)) {
        detectedYaku.add("三色同順");
      }

      if (hasSanshokudoukou(hand)) {
        detectedYaku.add("三色同刻");
      }

      if (hasShousangen(hand)) {
        detectedYaku.add("小三元");
      }

      if (hasSankantsu(hand)) {
        detectedYaku.add("三槓子");
      }

    }

    if (hasHonitsu(hand)) {
      detectedYaku.add("混一色");
    }

    if (hasChinitsu(hand)) {
      detectedYaku.add("清一色");
    }

    if (hasHonroutou(hand)) {
      detectedYaku.add("混老頭");
    }

    return detectedYaku;
  }

}
