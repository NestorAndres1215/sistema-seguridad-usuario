package com.example.backend_security.repository;


import com.example.backend_security.dto.UserStatusPercentageDTO;
import com.example.backend_security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    List<User> findByRole_Name(String roleName);

    List<User> findByStatus_Code(String statusCode);

    List<User> findByRole_NameAndStatus_Code(String roleName, String statusCode);

    @Query("""
                SELECT new com.example.backend_security.dto.UserStatusPercentageDTO(
                    u.status.code,
                    COUNT(u),
                    ROUND((COUNT(u) * 100.0 / (SELECT COUNT(u2) FROM User u2)), 2)
                )
                FROM User u
                GROUP BY u.status.code
                ORDER BY u.status.code
            """)
    List<UserStatusPercentageDTO> getUserStatusPercentages();


}
