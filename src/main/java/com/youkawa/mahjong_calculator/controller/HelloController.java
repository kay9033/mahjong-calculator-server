package com.youkawa.mahjong_calculator.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class HelloController {

  @GetMapping("/hello")
  public Map<String, String> hello() {
    return Map.of("message", "Hello, Spring Boot!");
  }
}
