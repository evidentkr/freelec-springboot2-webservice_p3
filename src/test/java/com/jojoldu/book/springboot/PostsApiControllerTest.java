package com.jojoldu.book.springboot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jojoldu.book.springboot.domain.posts.Posts;
import com.jojoldu.book.springboot.domain.posts.PostsRepository;
import com.jojoldu.book.springboot.web.dto.PostsSaveRequestDto;
import com.jojoldu.book.springboot.web.dto.PostsUpdateRequestDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {

    //p109 일반적인 호출 테스트 (로그인 미포함)
    @LocalServerPort
    private int port;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @After
    public void tearDown() throws Exception {
        postsRepository.deleteAll();
    }


    //MockMvc 사용위한 설정 - OAuth 는 Mock 으로만 테스트 가능
    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;
    @Before
    public void setup(){
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    //컨트롤러를 통해 값 저장 후 읽어오기
    @Test
    @WithMockUser(roles="USER")
    public void posts_일반저장후_저장값확인() throws Exception{
        //변수 준비
        String title = "title";
        String content = "content";
        //dto 생성
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder().title(title).content(content).author("저자").build();
        //입력 url 준비
        String url = "http://localhost:" + port + "/api/v1/posts";

        //저장 후 값 확인
//        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        //저장 후 값 확인 - mock
        mvc.perform(
                post(url).contentType(MediaType.APPLICATION_JSON_UTF8).content(new ObjectMapper().writeValueAsString(requestDto))
        )
        .andExpect(status().isOk());

        //저장된 값 비교 확인
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }

    @Test
    @WithMockUser(roles="USER")
    public void Posts_입력후_수정() throws Exception{
        //저장액션 - 레파지토리에 직접 입력
        Posts savedPosts = postsRepository.save(Posts.builder().title("title").content("content").author("author").build());
        //수정할값
        Long updateId = savedPosts.getId();
        String expectedTitle = "수정된타이틀";
        String expectedContent = "수정된컨텐츠";
        //수정입력dto
        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder().title(expectedTitle).content(expectedContent).build();
        //입력URL
        String url = "http://localhost:" + port + "/api/v1/posts/"+updateId;
        //requestEntity 준비
        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        //업데이트액션 및 확인
//        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        //업데이트액션 및 확인 - Mock
        mvc.perform(
                put(url).contentType(MediaType.APPLICATION_JSON_UTF8).content(new ObjectMapper().writeValueAsString(requestDto))
        )
        .andExpect(status().isOk());

        //업데이트값 확인
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);

    }
}