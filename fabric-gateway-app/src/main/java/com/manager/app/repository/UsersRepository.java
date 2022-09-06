package com.manager.app.repository;

import com.manager.app.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
public interface UsersRepository extends MongoRepository<User, Integer> {
    User findByEmail(String email);
}