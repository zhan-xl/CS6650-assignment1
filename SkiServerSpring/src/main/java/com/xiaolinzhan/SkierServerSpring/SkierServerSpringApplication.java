package com.xiaolinzhan.SkierServerSpring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class SkierServerSpringApplication {

  public static void main(String[] args) {
    SpringApplication.run(SkierServerSpringApplication.class, args);
  }
}
