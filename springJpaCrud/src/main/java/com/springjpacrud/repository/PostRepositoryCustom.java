package com.springjpacrud.repository;

import com.springjpacrud.domain.Post;
import com.springjpacrud.dto.PostUserDTO;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> getPosts();
    List<Post> getPostsFetchJoin();
    List<Post> getPostsNoRelation();
    List<PostUserDTO> getDto();
}
