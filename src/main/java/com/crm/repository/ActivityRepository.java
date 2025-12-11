package com.crm.repository;

import com.crm.model.Activity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ActivityRepository extends MongoRepository<Activity, String>, ActivityRepositoryCustom {
}
