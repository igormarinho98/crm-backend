package com.crm.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Estágios possíveis do pipeline de vendas")
public enum PipelineStage {
    PROSPECCAO,
    NEGOCIACAO,
    FECHADO_GANHO,
    FECHADO_PERDIDO
}
