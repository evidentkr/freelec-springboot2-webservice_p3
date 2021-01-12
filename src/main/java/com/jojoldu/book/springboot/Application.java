package com.jojoldu.book.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//스프링 부트 어플리케이션 클래스
@EnableJpaAuditing // JapAuditing 활성화
@SpringBootApplication
public class Application {
    public static void main(String [] args) {
        SpringApplication.run(Application.class, args);
    }
}
