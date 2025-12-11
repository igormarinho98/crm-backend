package com.crm.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "FunnelStageStats", description = "Estatísticas consolidadas por estágio do pipeline")
public class FunnelStageStats {
    @Schema(description = "Nome do estágio (PipelineStage)")
    private String pipelineStage;

    @Schema(description = "Total (soma) do valor dos deals neste estágio")
    private Double totalValue;

    @Schema(description = "Quantidade de deals neste estágio")
    private Long count;

    public FunnelStageStats() {}

    public FunnelStageStats(String pipelineStage, Double totalValue, Long count) {
        this.pipelineStage = pipelineStage;
        this.totalValue = totalValue;
        this.count = count;
    }

    public String getPipelineStage() {
        return pipelineStage;
    }

    public void setPipelineStage(String pipelineStage) {
        this.pipelineStage = pipelineStage;
    }

    public Double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
