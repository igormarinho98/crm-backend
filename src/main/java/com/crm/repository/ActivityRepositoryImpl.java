package com.crm.repository;

import com.crm.model.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ActivityRepositoryImpl implements ActivityRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ActivityRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Activity> findByCompanyId(String companyId) {
        // Pipeline: lookup deals by relatedDealId -> unwind -> match deal.companyId == companyId -> project activity
        Aggregation agg = Aggregation.newAggregation(
                // lookup deals into field 'deal'
                Aggregation.lookup("deals", "relatedDealId", "_id", "deal"),
                // unwind the deal array
                Aggregation.unwind("$deal"),
                // match where deal.companyId equals provided companyId
                Aggregation.match(Criteria.where("deal.companyId").is(companyId))
        );

        AggregationResults<Activity> results = mongoTemplate.aggregate(agg, "activities", Activity.class);
        return results.getMappedResults();
    }
}
