package com.springjpacrud.service;

import com.springjpacrud.dto.PostDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@Rollback(value = false)
class PostServiceTest {
    @Autowired
    PostService postService;
    @PersistenceContext
    EntityManager em;

    @Test
    void findAll() {
        List<PostDTO> rs = postService.findAll();
        assertThat(2).isEqualTo(rs.size());
    }

    @Test
    void save(){
        PostDTO postDTO = PostDTO.builder().title("ABC").content("TEST!!!").email("Test1@naver.com").build();
        boolean rs = postService.save(postDTO);
        assertThat(true).isEqualTo(rs);
    }

    @Test
    void updatePost(){
        PostDTO postDTO = PostDTO.builder().id(9L).title("TEST9").content("TEST99").build();
        boolean rs = postService.updatePost(postDTO);
        assertThat(true).isEqualTo(rs);
    }
}