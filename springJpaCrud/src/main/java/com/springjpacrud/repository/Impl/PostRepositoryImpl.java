package com.springjpacrud.repository.Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springjpacrud.domain.Post;
import com.springjpacrud.repository.PostRepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.springjpacrud.domain.QPost.post;
import static com.springjpacrud.domain.QUser.user;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getPosts() {
        return jpaQueryFactory
                .selectFrom(post)
                .join(post.user, user)
                .fetch();
    }

    @Override
    public List<Post> getPostsFetchJoin() {
        return jpaQueryFactory
                .selectFrom(post)
                .join(post.user, user)
                .fetchJoin()
                .fetch();
    }
}
