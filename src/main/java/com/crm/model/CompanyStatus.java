package com.crm.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status de uma empresa no CRM")
public enum CompanyStatus {
    ATIVO,
    INATIVO,
    SUSPENSO
}
