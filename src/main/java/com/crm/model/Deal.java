package com.crm.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Document(collection = "deals")
@Schema(name = "Deal", description = "Representa uma oportunidade / negócio")
public class Deal {

    @Id
    @Schema(description = "ID do negócio")
    private String id;

    @Schema(description = "Título / nome do negócio")
    private String title;

    @Indexed
    @Schema(description = "ID da empresa relacionada")
    private String companyId;

    @Indexed
    @Schema(description = "ID do contato relacionado")
    private String contactId;

    @Schema(description = "Valor do negócio")
    private BigDecimal dealValue;

    @Schema(description = "Moeda (ex: BRL)")
    private String currency = "BRL";

    @Schema(description = "Estágio no pipeline")
    private PipelineStage pipelineStage;

    @Schema(description = "Probabilidade de fechamento (0.0 - 1.0)")
    private Double probability;

    @Schema(description = "Data esperada de fechamento")
    private LocalDate expectedCloseDate;

    @Schema(description = "Observações internas")
    private String notes;

    @Schema(description = "Usuário que criou o registro")
    private String createdBy;

    @Schema(description = "Último usuário que atualizou o registro")
    private String updatedBy;

    @CreatedDate
    @Schema(description = "Timestamp de criação")
    private Instant createdAt;

    @LastModifiedDate
    @Schema(description = "Timestamp da última atualização")
    private Instant updatedAt;

    @Schema(description = "Indicador de exclusão lógica")
    private boolean deleted = false;

    @Schema(description = "Timestamp de exclusão lógica")
    private Instant deletedAt;

    public Deal() {}

    // getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }

    public String getContactId() { return contactId; }
    public void setContactId(String contactId) { this.contactId = contactId; }

    public BigDecimal getDealValue() { return dealValue; }
    public void setDealValue(BigDecimal dealValue) { this.dealValue = dealValue; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public PipelineStage getPipelineStage() { return pipelineStage; }
    public void setPipelineStage(PipelineStage pipelineStage) { this.pipelineStage = pipelineStage; }

    public Double getProbability() { return probability; }
    public void setProbability(Double probability) { this.probability = probability; }

    public LocalDate getExpectedCloseDate() { return expectedCloseDate; }
    public void setExpectedCloseDate(LocalDate expectedCloseDate) { this.expectedCloseDate = expectedCloseDate; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }

    public Instant getDeletedAt() { return deletedAt; }
    public void setDeletedAt(Instant deletedAt) { this.deletedAt = deletedAt; }
}
