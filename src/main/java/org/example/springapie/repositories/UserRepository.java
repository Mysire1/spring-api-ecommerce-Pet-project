package org.example.springapie.repositories;

import jakarta.servlet.http.HttpServletRequest;
import org.example.springapie.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> , CrudRepository<User, Long> {

    User findByName(String name);
    Optional<User> findByEmail(String email);
    Optional<User> findByVerificationCode(String code);

}
