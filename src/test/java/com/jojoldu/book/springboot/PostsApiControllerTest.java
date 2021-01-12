package com.jojoldu.book.springboot;

import com.jojoldu.book.springboot.domain.posts.Posts;
import com.jojoldu.book.springboot.domain.posts.PostsRepository;
import com.jojoldu.book.springboot.web.dto.PostsSaveRequestDto;
import com.jojoldu.book.springboot.web.dto.PostsUpdateRequestDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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

    //컨트롤러를 통해 값 저장 후 읽어오기
    @Test
    public void posts_일반저장후_저장값확인() {
        //변수 준비
        String title = "title";
        String content = "content";
        //dto 생성
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder().title(title).content(content).author("저자").build();
        //입력 url 준비
        String url = "http://localhost:" + port + "/api/v1/posts";
        //저장
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        //저장액션 잘되었는지 확인
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        //저장된 값 비교 확인
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }

    @Test
    public void Posts_입력후_수정() {
        //저장액션
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
        //업데이트액션
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);
        //업데이트액션 확인
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);
        //업데이트값 확인
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);

    }
}