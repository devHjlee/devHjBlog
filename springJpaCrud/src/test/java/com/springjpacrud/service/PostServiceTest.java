package com.springjpacrud.service;

import com.springjpacrud.domain.Post;
import com.springjpacrud.dto.PostDTO;
import com.springjpacrud.dto.PostUserDTO;
import com.springjpacrud.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@Rollback(value = false)
class PostServiceTest {
    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    void findAll() {
        List<PostDTO> rs = postService.findAll();
        assertThat(2).isEqualTo(rs.size());
    }

    @Test
    void save(){
        PostDTO postDTO = PostDTO.builder().title("CBA").content("TEST!!!").email("Test2@naver.com").build();
        boolean rs = postService.save(postDTO);
        assertThat(true).isEqualTo(rs);
    }

    @Test
    void updatePost(){
        PostDTO postDTO = PostDTO.builder().id(9L).title("TEST9").content("TEST99").build();
        boolean rs = postService.updatePost(postDTO);
        assertThat(true).isEqualTo(rs);
    }

    @PersistenceUnit
    EntityManagerFactory emf;
    @Test
    void getPosts(){
        boolean loaded;
        //연관관계 Entity 조회
        //JpaRepository
        List<Post> posts = postRepository.findAll();
        loaded = emf.getPersistenceUnitUtil().isLoaded(posts.get(0).getUser());
        assertThat(false).isEqualTo(loaded);

        //join
        List<Post> posts2 = postService.getPosts();
        loaded = emf.getPersistenceUnitUtil().isLoaded(posts2.get(0).getUser());
        assertThat(false).isEqualTo(loaded);

        posts2.get(0).getUser().toString();
        loaded = emf.getPersistenceUnitUtil().isLoaded(posts2.get(0).getUser());
        assertThat(true).isEqualTo(loaded);

        //FetchJoin
        List<Post> posts3 = postService.getPostsFetchJoin();
        loaded = emf.getPersistenceUnitUtil().isLoaded(posts3.get(0).getUser());
        assertThat(true).isEqualTo(loaded);

        //연관관계 없을시 join on
        List<Post> posts4 = postService.getPostsNoRelation();
        loaded = emf.getPersistenceUnitUtil().isLoaded(posts4.get(0).getUser());
        assertThat(true).isEqualTo(loaded);

        //@QueryProjection DTO
        List<PostUserDTO> posts5 = postService.getDto();
        loaded = emf.getPersistenceUnitUtil().isLoaded(posts5.get(0).getUserName());
        assertThat(true).isEqualTo(loaded);
    }
}