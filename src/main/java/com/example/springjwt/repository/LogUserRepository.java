package com.example.springjwt.repository;

import com.example.springjwt.entity.LogUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LogUserRepository extends MongoRepository<LogUser, String> {
    Optional<LogUser> findByUsername(String username);
}
