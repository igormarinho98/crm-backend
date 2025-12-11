package com.crm.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.Arrays;

@Document(collection = "companies")
@Schema(name = "Company", description = "Representa uma empresa cliente ou lead")
public class Company {

    @Id
    @Schema(description = "ID da empresa (gerado pelo MongoDB)")
    private String id;

    @Schema(description = "Nome da empresa")
    private String name;

    @Schema(description = "CNPJ (formatado sem máscaras)")
    @Indexed
    private String cnpj;

    @Schema(description = "Telefone de contato")
    private String phone;

    @Schema(description = "Website da empresa")
    private String website;

    @Schema(description = "Endereço da empresa")
    private Address address;

    @Schema(description = "Status da empresa no CRM")
    private CompanyStatus status;

    @Schema(description = "Tags para classificação")
    private String[] tags;

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

    public Company() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public CompanyStatus getStatus() {
        return status;
    }

    public void setStatus(CompanyStatus status) {
        this.status = status;
    }

    public String[] getTags() {
        return tags == null ? new String[0] : tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
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

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public String toString() {
        return "Company{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", cnpj='" + cnpj + '\'' +
                ", phone='" + phone + '\'' +
                ", website='" + website + '\'' +
                ", address=" + address +
                ", status=" + status +
                ", tags=" + Arrays.toString(tags) +
                '}';
    }
}
