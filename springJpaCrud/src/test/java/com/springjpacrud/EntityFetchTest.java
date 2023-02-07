package com.springjpacrud;

import com.springjpacrud.domain.Post;
import com.springjpacrud.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@Rollback(value = false)
public class EntityFetchTest {

    @PersistenceContext
    EntityManager em;

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
        Post post = em.find(Post.class,2L);
        System.out.println(post.getUser().getUserName());
        System.out.println("===================================");
        System.out.println(post.getUser().getPosts().get(0).getTitle());
        System.out.println(post.getUser());
        //User user = em.find(User.class,1L);
        //System.out.println(user);
    }

}
