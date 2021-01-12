package com.jojoldu.book.springboot;

import com.jojoldu.book.springboot.domain.posts.Posts;
import com.jojoldu.book.springboot.domain.posts.PostsRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostsRepositoryTest {
    @Autowired
    PostsRepository postsRepository;

    @After
    public void cleanup() {
        postsRepository.deleteAll();
    }

    //레파지토리에 직접 저장 테스트
    @Test
    public void 레파지토리직접_게실글저장_게시글불러오기() {
        String title = "테스트 게시글";
        String content = "테스트 본문";
        postsRepository.save(Posts.builder().title(title).content(content).build());

        List<Posts> postsList = postsRepository.findAll();
        Posts posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);

        System.out.println("생성/수정 시간 >>> "+ posts.getCreateDate() +"/"+posts.getModifiedDate());
    }
}