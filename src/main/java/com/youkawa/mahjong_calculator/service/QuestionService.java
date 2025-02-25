package com.youkawa.mahjong_calculator.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.youkawa.mahjong_calculator.model.Question;
import com.youkawa.mahjong_calculator.model.Tile;

@Service
public class QuestionService {
  private static final Random random = new Random();
  private final YakuService yakuService;
  private static final List<String> SUITS = Arrays.asList("m", "s", "p", "z");
  private static final List<String> NAKI_TYPE = Arrays.asList(null, "ron", "ti");

  public QuestionService(YakuService yakuService) {
    this.yakuService = yakuService;
  }

  public Question generaQuestion() {
    List<String> yaku = new ArrayList<>();
    Map<String, String> condition = new HashMap<>(Map.of("場風", "東", "自風", "東"));
    String winType;

    boolean iswinningHand = false;

    while (!iswinningHand) {
      List<List<Tile>> hand = genarateHand();
      if (yakuService.iswinningHand(hand)) {
        iswinningHand = true;
        yaku = yakuService.checkYaku(hand);
      }
    }

    if (random.nextBoolean()) {
      condition.put("場風", "南");
    }

    int choice = random.nextInt(4);

    switch (choice) {
      case 0 -> condition.put("自風", "南");
      case 1 -> condition.put("自風", "西");
      case 2 -> condition.put("自風", "北");
    }

    if (random.nextBoolean()) {
      winType = "tsumo";
    } else {
      winType = "ron";
    }

    Question question = new Question(yaku, condition, winType);
    return question;
  }

  private List<List<Tile>> genarateHand() {
    List<List<Tile>> hand = new ArrayList<>();

    if (random.nextBoolean()) {
      for (int i = 0; i < 6; i++) {
        hand.add(generateToitsu());
      }
    } else {
      for (int i = 0; i < 4; i++) {
        if (random.nextBoolean()) {
          hand.add(generateKotsu());
        } else {
          hand.add(generateShuntsu());
        }
      }
    }
    hand.add(generateToitsu());

    return hand;
  }

  private List<Tile> generateKotsu() {
    String suit = SUITS.get(random.nextInt(SUITS.size() - 1));
    int number;
    List<Tile> meld = new ArrayList<>();

    if (suit == "z") {
      number = random.nextInt(7) + 1;
    } else {
      number = random.nextInt(9) + 1;
    }
    for (int i = 0; i < 3; i++) {
      Tile tile;
      if (i == 2) {
        tile = new Tile(suit, number, NAKI_TYPE.get(random.nextInt(SUITS.size() - 1)));
      } else {
        tile = new Tile(suit, number, null);
        meld.add(tile);
      }
    }
    return meld;
  }

  private List<Tile> generateShuntsu() {
    String suit = SUITS.get(random.nextInt(SUITS.size() - 2));
    int number = random.nextInt(3) * 3 + 1;
    List<Tile> meld = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      Tile tile;
      if (i == 2) {
        tile = new Tile(suit, number + i, NAKI_TYPE.get(random.nextInt(SUITS.size() - 1)));
      } else {
        tile = new Tile(suit, number + i, null);
        meld.add(tile);
      }
    }
    return meld;
  }

  private List<Tile> generateToitsu() {
    String suit = SUITS.get(random.nextInt(SUITS.size() - 1));
    int number;
    List<Tile> meld = new ArrayList<>();

    if (suit == "z") {
      number = random.nextInt(7) + 1;
    } else {
      number = random.nextInt(9) + 1;
    }
    for (int i = 0; i < 2; i++) {
      Tile tile = new Tile(suit, number, null);
      meld.add(tile);
    }
    return meld;
  }

}
