package com.springjpacrud.service;

import com.springjpacrud.dto.PostDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class PostServiceTest {
    @Autowired
    PostService postService;
    @Test
    void findAll() {
        List<PostDTO> rs = postService.findAll();
        System.out.println(rs);
    }

    @Test
    void save(){
        PostDTO postDTO = PostDTO.builder().title("ABC").content("TEST!!!").build();
        boolean rs = postService.save("dlgudwo11@naver.com",postDTO);

    }
}