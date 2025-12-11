package com.crm.service;

import com.crm.dto.FunnelStageStats;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DealService {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public DealService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Retorna estatísticas consolidadas por pipelineStage: soma de dealValue e contagem de deals.
     * Se companyId for fornecido, aplica filtro por companyId.
     */
    public List<FunnelStageStats> getFunnelStats(String companyId) {
        List<AggregationOperation> ops = new ArrayList<>();
        if (companyId != null && !companyId.isEmpty()) {
            ops.add(Aggregation.match(Criteria.where("companyId").is(companyId)));
        }

        // group by pipelineStage, count and sum dealValue
        ops.add(Aggregation.group("pipelineStage").count().as("count").sum("dealValue").as("totalValue"));
        // project to include pipelineStage field
        ops.add(Aggregation.project("count", "totalValue").and("_id").as("pipelineStage"));

        Aggregation agg = Aggregation.newAggregation(ops);
        AggregationResults<Document> results = mongoTemplate.aggregate(agg, "deals", Document.class);

        List<FunnelStageStats> stats = new ArrayList<>();
        for (Document doc : results.getMappedResults()) {
            String stage = doc.getString("pipelineStage");
            Number count = (Number) doc.get("count");
            Number total = (Number) doc.get("totalValue");
            Long countLong = count != null ? count.longValue() : 0L;
            Double totalDouble = total != null ? total.doubleValue() : 0.0;
            stats.add(new FunnelStageStats(stage, totalDouble, countLong));
        }

        return stats;
    }
}
