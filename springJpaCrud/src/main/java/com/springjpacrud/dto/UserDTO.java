package com.springjpacrud.dto;

import com.springjpacrud.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
@Builder
public class UserDTO {
    private String email;

    private String userName;

    private String password;



    public User toEntity(){
        return User.builder()
                .email(email)
                .userName(userName)
                .password(password)
                .build();
    }
}