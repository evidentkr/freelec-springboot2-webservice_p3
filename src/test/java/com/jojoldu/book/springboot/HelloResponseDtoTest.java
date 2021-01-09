package com.jojoldu.book.springboot;

import com.jojoldu.book.springboot.web.dto.HelloResponseDto;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

//DTO 테스트 클래스
public class HelloResponseDtoTest {
    
    //롬복dto 가 정상 작동하는지 확인 하는 테스트 - 컨트롤러 미경유
    @Test
    public void 롬복_기능_테스트(){
        //입력 및 확인 변수
        String name = "test";
        int amount = 1000;

        //dto 객체 임의 생성
        HelloResponseDto dto = new HelloResponseDto(name, amount);
        
        //입력한 값과 리턴된 값이 같은지 확인
        assertThat(dto.getName()).isEqualTo(name);
        assertThat(dto.getAmount()).isEqualTo(amount);
    }
}