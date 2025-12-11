package com.crm.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Document(collection = "activities")
@Schema(name = "Activity", description = "Registra tarefas, chamadas e e-mails relacionados a deals")
public class Activity {

    @Id
    @Schema(description = "ID da activity (gerado pelo MongoDB)")
    private String id;

    @Schema(description = "Tipo da activity (CALL, TASK, EMAIL)")
    private ActivityType type;

    @Schema(description = "Descrição ou observação da activity")
    private String description;

    @Schema(description = "Data e hora prevista/realização da activity")
    private Instant dueDate;

    @Schema(description = "ID do deal relacionado (opcional)")
    private String relatedDealId;

    @Schema(description = "ID do usuário responsável/atribuído")
    private String assignedToUserId;

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

    public Activity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ActivityType getType() {
        return type;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getDueDate() {
        return dueDate;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }

    public String getRelatedDealId() {
        return relatedDealId;
    }

    public void setRelatedDealId(String relatedDealId) {
        this.relatedDealId = relatedDealId;
    }

    public String getAssignedToUserId() {
        return assignedToUserId;
    }

    public void setAssignedToUserId(String assignedToUserId) {
        this.assignedToUserId = assignedToUserId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
