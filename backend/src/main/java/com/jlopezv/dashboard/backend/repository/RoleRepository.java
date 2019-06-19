package com.jlopezv.dashboard.backend.repository;

import com.jlopezv.dashboard.backend.model.Role;
import com.jlopezv.dashboard.backend.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}
