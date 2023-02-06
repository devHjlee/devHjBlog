package com.springjpacrud.service;

import com.springjpacrud.dto.PostDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@Rollback(value = false)
class PostServiceTest {
    @Autowired
    PostService postService;

    @Test
    void findBy(){
        List<PostDTO> postDTO = postService.findByTitleOrContent("ABC","TAA!");
        assertThat("TEST!!!").isEqualTo(postDTO.get(0).getContent());
    }

    @Test
    void findAll() {
        List<PostDTO> rs = postService.findAll();
        assertThat(2).isEqualTo(rs.size());
    }

    @Test
    void save(){
        PostDTO postDTO = PostDTO.builder().title("ABC").content("TEST!!!").build();
        boolean rs = postService.save("dlgudwo11@naver.com",postDTO);
        assertThat(true).isEqualTo(rs);

    }
}