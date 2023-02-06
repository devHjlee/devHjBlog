package com.springjpacrud.repository;

import com.springjpacrud.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{

    /**
     * Email로 사용자 검색
     * @param email
     * @return
     */
    User findByEmail(String email);

    /**
     * Email 중복 체크
     * @param email
     * @return boolean
     */
    boolean existsByEmail(String email);

}
