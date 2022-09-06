package com.manager.app.repository;

import com.manager.app.model.Client;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
public interface ClientsRepository extends MongoRepository<Client, Integer> {
    Client findByEmail(String email);
}
