package com.springjpacrud.service;

import com.springjpacrud.domain.Post;
import com.springjpacrud.domain.User;
import com.springjpacrud.dto.PostDTO;
import com.springjpacrud.repository.PostRepository;
import com.springjpacrud.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<PostDTO> findAll(){
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(m-> PostDTO.builder().title(m.getTitle()).content(m.getContent()).build())
                .collect(Collectors.toList());
    }

    public boolean save(String email, PostDTO postDTO){
        User user = userRepository.findByEmail(email);
        if(user.getId() != null){
            postDTO.setUser(user);
            postRepository.save(postDTO.toEntity());
            return true;
        }
        return false;
    }
}
