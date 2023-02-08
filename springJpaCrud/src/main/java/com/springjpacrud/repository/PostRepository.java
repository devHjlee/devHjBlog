package com.springjpacrud.repository;

import com.springjpacrud.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findPostByTitleOrContent(String content, String title);

    Post findPostById(Long id);
}
