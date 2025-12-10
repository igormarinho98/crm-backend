package com.crm.repository;

import com.crm.model.Deal;
import com.crm.model.PipelineStage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DealRepository extends MongoRepository<Deal, String> {
    List<Deal> findByCompanyId(String companyId);
    List<Deal> findByContactId(String contactId);
    List<Deal> findByPipelineStage(PipelineStage pipelineStage);
}
