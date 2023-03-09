package com.springjpacrud.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_no")
    private Long id;

    private String email;
    private String userName;
    private String password;

    @Builder
    public User(String email, String userName, String password){
        this.email = email;
        this.userName = userName;
        this.password = password;
    }
}