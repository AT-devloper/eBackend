package com.example.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.Model.User;



@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByPhone(String phone);

    Optional<User> findByUsername(String username);

    // Returns Optional<User> for better type safety
    Optional<User> findByEmailOrUsernameOrPhone(String email, String username, String phone);

    boolean existsByUsername(String username); // <-- needed for unique username check
}
