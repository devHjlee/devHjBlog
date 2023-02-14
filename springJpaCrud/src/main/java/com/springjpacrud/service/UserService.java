package com.springjpacrud.service;

import com.springjpacrud.domain.User;
import com.springjpacrud.dto.UserDTO;
import com.springjpacrud.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    /* Spring Data Jpa 를 통한 기능 */
    /**
     * 전체 사용자 조회
     * @return List<User>
     */
    public List<UserDTO> findAll(){

        return userRepository.findAll().stream()
                .map(m->UserDTO.builder()
                        .userName(m.getUserName())
                        .email(m.getEmail())
                        .password(m.getPassword()) //@jsonignore Test
                        .build())
                .collect(Collectors.toList());
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }
    /**
     * 회원가입
     * @param userDTO
     * @return boolean
     */
    public boolean save(UserDTO userDTO){
        if(!userRepository.existsByEmail(userDTO.getEmail())){
            User user = userDTO.toEntity();
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
