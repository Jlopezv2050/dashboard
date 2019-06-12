package com.jlopezv.dashboard.backend.repository;

import com.jlopezv.dashboard.backend.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
