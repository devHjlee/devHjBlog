package com.springjpacrud.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false,columnDefinition = "TEXT")
    private String content;

    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no")
    private User user;

    @Builder
    public Post(String title, String content, String email, User user){
        this.title = title;
        this.content = content;
        this.email = email;
        this.user = user;
    }

    /* 비지니스로직 */
    /* 게시글 수정 */
    public void updatePost(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
