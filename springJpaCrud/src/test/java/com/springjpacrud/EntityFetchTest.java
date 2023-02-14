package com.springjpacrud;

import com.springjpacrud.domain.Post;
import com.springjpacrud.domain.User;
import com.springjpacrud.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@Rollback(value = false)
public class EntityFetchTest {
    Logger log =(Logger) LoggerFactory.getLogger(EntityFetchTest.class);
    @PersistenceContext
    EntityManager em;
    @Autowired
    PostRepository postRepository;

    @Test
    void save(){
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
    void find(){

        System.out.println("Post Find STATR");
        Post post = em.find(Post.class,7L);
        System.out.println("Post Find END");

        System.out.println("===================================");

        System.out.println("USER Find START");
        System.out.println(post.getUser().getUserName());
        System.out.println("USER Find END");

        System.out.println("===================================");
        System.out.println("USER.POSTS Find START");
        //System.out.println(post.getUser().getPosts().get(0).getTitle());
        System.out.println("USER.POSTS Find END");

        //POST ManyToOne 이 EAGER 일시 select p from post p 수행 후 user 를 채우기 위해 select u from user u 실행
        List<Post> postList = postRepository.findAll();


    }

}
