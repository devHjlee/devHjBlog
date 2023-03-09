package com.springjpacrud.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostUserDTO {
    private String title;
    private String content;
    private String email;
    private String userName;

    @QueryProjection
    public PostUserDTO(String title, String content, String email, String UserName) {
        this.title = title;
        this.content = content;
        this.email = email;
        this.userName = getUserName();
    }
}
