package com.crm.repository;

import com.crm.dto.DealDetailsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DealRepositoryImpl implements DealRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public DealRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Optional<DealDetailsDTO> findDealWithRelations(String id) {
        MatchOperation match = Aggregation.match(Criteria.where("_id").is(id));

        LookupOperation lookupCompany = LookupOperation.newLookup()
                .from("companies")
                .localField("companyId")
                .foreignField("_id")
                .as("company");
        UnwindOperation unwindCompany = Aggregation.unwind("$company", true);

        LookupOperation lookupContact = LookupOperation.newLookup()
                .from("contacts")
                .localField("contactId")
                .foreignField("_id")
                .as("contact");
        UnwindOperation unwindContact = Aggregation.unwind("$contact", true);

        ProjectionOperation project = Aggregation.project("title", "dealValue", "currency", "expectedCloseDate", "notes", "companyId", "contactId")
                .and("company").as("company")
                .and("contact").as("contact")
                .andExclude("_class");

        Aggregation agg = Aggregation.newAggregation(match, lookupCompany, unwindCompany, lookupContact, unwindContact, project);

        AggregationResults<DealDetailsDTO> results = mongoTemplate.aggregate(agg, "deals", DealDetailsDTO.class);
        DealDetailsDTO dto = results.getUniqueMappedResult();
        return Optional.ofNullable(dto);
    }
}
