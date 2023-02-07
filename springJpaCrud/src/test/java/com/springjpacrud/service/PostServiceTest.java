package com.springjpacrud.service;

import com.springjpacrud.domain.Member;
import com.springjpacrud.domain.Post;
import com.springjpacrud.domain.Team;
import com.springjpacrud.domain.User;
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
    void 연관관계_테스트(){
        User user = User.builder().userName("Test1").email("Test@naver.com").build();
        User user2 = User.builder().userName("Test2").email("Test2@naver.com").build();

        em.persist(user);
        em.persist(user2);

        Post post = Post.builder().title("TEST").content("TEST").user(user).build();
        em.persist(post);
        post = Post.builder().title("TEST1").content("TEST1").user(user).build();
        em.persist(post);
        post = Post.builder().title("TEST2").content("TEST2").user(user2).build();
        em.persist(post);

    }

    @Test
    void 연관관계_테스트2(){

        Post post = em.find(Post.class,2L);
        System.out.println(post.getUser().getUserName());
        System.out.println("===================================");
        //System.out.println(post.getUser().getPosts().get(0).getTitle());
        //System.out.println(post.getUser());
        //User user = em.find(User.class,1L);
        //System.out.println(user);
    }

    @Test
    void findBy(){
        //List<PostDTO> postDTO = postService.findByTitleOrContent("ABC","TAA!");
        //assertThat("TEST!!!").isEqualTo(postDTO.get(0).getContent());
        //Post post = em.find(Post.class,);

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