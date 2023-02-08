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

    /* Spring Data Jpa 를 통한 기능 */

    /**
     * 게시글 조회
     * @param title
     * @param content
     * @return List<PostDTO>
     */
    public List<PostDTO> findByTitleOrContent(String title, String content) {
        return postRepository.findPostByTitleOrContent(title, content).stream()
                .map(m-> PostDTO.builder()
                        .title(m.getTitle())
                        .content(m.getContent())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 게시글 상세
     * @param id
     * @return PostDTO
     */
    public PostDTO findPostById (Long id) {
        Post post = postRepository.findPostById(id);
        return PostDTO.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    /**
     * 전체 게시글 조회
     * @return List<PostDTO>
     */
    public List<PostDTO> findAll() {
        return postRepository.findAll().stream()
                .map(m-> PostDTO.builder()
                        .title(m.getTitle())
                        .content(m.getContent())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 게시글 저장
     * @param email
     * @param postDTO
     * @return
     */
    public boolean save(String email, PostDTO postDTO) {
        User user = userRepository.findByEmail(email);
        if(user.getId() != null){
            postDTO.setUser(user);
            postRepository.save(postDTO.toEntity());
            return true;
        }
        return false;
    }

    /**
     * 게시글 수정
     * @param postDTO
     * @return boolean
     */
    public boolean updatePost(PostDTO postDTO) {
        Post post = postRepository.findPostById(postDTO.getId());
        post.updatePost(postDTO.getTitle(),postDTO.getContent());
        return true;
    }
}
