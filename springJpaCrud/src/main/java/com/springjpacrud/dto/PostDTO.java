package com.springjpacrud.dto;

import com.springjpacrud.domain.Post;
import com.springjpacrud.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
@Builder
public class PostDTO {
    private String title;
    private String content;
    private User user;

    public Post toEntity(){
        return Post.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();
    }
}
