package com.springjpacrud.service;

import com.springjpacrud.domain.User;
import com.springjpacrud.dto.UserDTO;

import com.springjpacrud.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@Rollback(value = false)
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void jpaRepositoryFindAll(){
        List<UserDTO> results = userService.findAll();
        assertThat(3).isEqualTo(results.size());
    }

    @Test
    void save() {
        boolean result = false;
        boolean result2 = false;
        UserDTO userDTO = UserDTO.builder()
                .userName("Test1")
                .email("Test1@naver.com")
                .password("a")
                .build();
        result = userService.save(userDTO);

        UserDTO userDTO2 = UserDTO.builder()
                .userName("Test2")
                .email("Test2@naver.com")
                .password("b")
                .build();
        result2 = userService.save(userDTO2);

        assertThat(result).isEqualTo(true);
        assertThat(result2).isEqualTo(true);
    }
}