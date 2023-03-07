package com.springjpacrud.repository;

import com.springjpacrud.domain.Post;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> getPosts();
    List<Post> getPostsFetchJoin();
}
