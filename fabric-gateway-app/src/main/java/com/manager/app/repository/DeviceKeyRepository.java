package com.manager.app.repository;

import com.manager.app.model.DeviceKey;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
public interface DeviceKeyRepository extends MongoRepository<DeviceKey, Integer> {
    DeviceKey findByOrg(String org);
}