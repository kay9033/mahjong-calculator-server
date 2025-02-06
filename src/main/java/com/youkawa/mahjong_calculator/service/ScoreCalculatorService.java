package com.youkawa.mahjong_calculator.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.youkawa.mahjong_calculator.model.Hand;
import com.youkawa.mahjong_calculator.model.ScoreResult;
import com.youkawa.mahjong_calculator.model.Tile;

@Service
public class ScoreCalculatorService {
  public ScoreResult calculateScore(Hand hand) {
    int han = 0;
    List<String> yaku = new ArrayList<>();

    // 役判定
    if (hand.isRiichi()) {
      han += 1;
      yaku.add("リーチ");
    }
    if (hand.isTsumo()) {
      han += 1;
      yaku.add("ツモ");
    }
    if (hand.getSpecialConditions().contains("haitei")) {
      han += 1;
      yaku.add("海底");
    }
    if (isTanyao(hand)) {
      han += 1;
      yaku.add("タンヤオ");
    }
    if (isPinfu(hand)) {
      han += 1;
      yaku.add("ピンフ");
    }
    if (isIipeikou(hand)) {
      han += 1;
      yaku.add("一盃口");
    }

    int fu = calculateFu(hand);
    int totalScore = calculateTotalScore(han, fu, hand.isDealer());

    return new ScoreResult(han, fu, totalScore, yaku);
  }

  private int calculateFu(Hand hand) {
    int fu = 20;
    if (hand.isTsumo()) {
      fu += 2;
    }
    return fu;
  }

  private boolean isTanyao(Hand hand) {
    return hand.getTiles().stream()
        .allMatch(tile -> !tile.getSuit().equals("z") && tile.getNumber() >= 2 && tile.getNumber() <= 8);
  }

  private boolean isPinfu(Hand hand) {
    List<Tile> tiles = hand.getTiles();
    tiles.sort(Comparator.comparing(Tile::getSuit).thenComparing(Tile::getNumber));

    // 順子のリストを作成
    List<String> shuntsuList = new ArrayList<>();
    Map<String, Long> tileCount = new HashMap<>();
    for (Tile tile : tiles) {
      String key = tile.getSuit() + tile.getNumber();
      tileCount.put(key, tileCount.getOrDefault(key, 0L) + 1);
    }

    List<Tile> remainingTiles = new ArrayList<>(tiles);
    String pair = null;

    // 雀頭（ジャントウ）を探す
    for (Map.Entry<String, Long> entry : tileCount.entrySet()) {
      if (entry.getValue() == 2) {
        pair = entry.getKey();
        remainingTiles.removeIf(t -> (t.getSuit() + t.getNumber()).equals(entry.getKey()));
        break;
      }
    }

    if (pair == null || pair.startsWith("z")) {
      return false; // 字牌が雀頭ならピンフ不成立
    }

    // 順子を抽出
    for (int i = 0; i < remainingTiles.size() - 2; i++) {
      Tile t1 = remainingTiles.get(i);
      Tile t2 = remainingTiles.get(i + 1);
      Tile t3 = remainingTiles.get(i + 2);

      if (t1.getSuit().equals(t2.getSuit()) && t2.getSuit().equals(t3.getSuit()) &&
          t1.getNumber() + 1 == t2.getNumber() && t2.getNumber() + 1 == t3.getNumber()) {

        // 順子をリストに追加
        String shuntsu = t1.getSuit() + t1.getNumber() + t2.getNumber() + t3.getNumber();
        shuntsuList.add(shuntsu);

        // 順子を取り除く
        remainingTiles.remove(i + 2);
        remainingTiles.remove(i + 1);
        remainingTiles.remove(i);
        i -= 1; // インデックスを戻して次の順子を探す
      }
    }

    // 残りの牌がすべて順子ならピンフ
    return remainingTiles.isEmpty();
  }

  private boolean isIipeikou(Hand hand) {
    // 手牌をソートして順子を抽出
    List<Tile> tiles = hand.getTiles();
    tiles.sort(Comparator.comparing(Tile::getSuit).thenComparing(Tile::getNumber));

    // 順子のリストを作成
    List<String> shuntsuList = new ArrayList<>();
    for (int i = 0; i < tiles.size() - 2; i++) {
      Tile t1 = tiles.get(i);
      Tile t2 = tiles.get(i + 1);
      Tile t3 = tiles.get(i + 2);

      // 3連続しているかチェック
      if (t1.getSuit().equals(t2.getSuit()) && t2.getSuit().equals(t3.getSuit()) &&
          t1.getNumber() + 1 == t2.getNumber() && t2.getNumber() + 1 == t3.getNumber()) {

        // 順子の文字列を作成 (例: "man234")
        String shuntsu = t1.getSuit() + t1.getNumber() + t2.getNumber() + t3.getNumber();
        shuntsuList.add(shuntsu);
      }
    }

    // 順子をカウント
    Map<String, Long> shuntsuCount = shuntsuList.stream()
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    // 同じ順子が2回以上ある種類が2つ以上あれば一盃口
    return shuntsuCount.values().stream().filter(count -> count >= 2).count() >= 1;
  }

  private int calculateTotalScore(int han, int fu, boolean isDealer) {
    int basePoints = fu * (int) Math.pow(2, 2 + han);
    return isDealer ? basePoints * 6 : basePoints * 4;
  }
}
