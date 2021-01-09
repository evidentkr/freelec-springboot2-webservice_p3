package com.jojoldu.book.springboot.web;

import com.jojoldu.book.springboot.web.dto.HelloResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//스프링부트 전반적 기능 테스트 컨트롤러 클래스
@RestController
public class HelloController {

    //단순 스트링 리턴
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    //dto 객체 리턴 - json
    @GetMapping("/hello/dto")
    public HelloResponseDto helloDto(@RequestParam("name") String name, @RequestParam("amount") int amount){
        return new HelloResponseDto(name, amount);
    }
    
}
