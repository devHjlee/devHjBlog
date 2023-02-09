package com.springjpacrud.dto;

import com.springjpacrud.domain.Post;
import com.springjpacrud.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter@Setter
@Builder
public class PostDTO {
    @Column(name = "post_id")
    private Long id;
    private String title;
    private String content;
    private User user;
    private Long writer;

    public Post toEntity(){
        return Post.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();
    }
}
