package com.crm.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import java.time.Instant;

@Document(collection = "contacts")
@Schema(name = "Contact", description = "Representa um contato/lead no CRM")
public class Contact {

    @Id
    @Schema(description = "ID do contato (gerado pelo MongoDB)")
    private String id;

    @Schema(description = "Nome")
    private String firstName;

    @Schema(description = "Sobrenome")
    private String lastName;

    @Email
    @Schema(description = "Email do contato")
    private String email;

    @Schema(description = "Cargo / função")
    private String jobTitle;

    /** Id da Company (string do _id) */
    @Schema(description = "ID da empresa relacionada (companyId)")
    private String companyId;

    @Schema(description = "URL do LinkedIn do contato")
    private String linkedinProfile;

    @CreatedDate
    @Schema(description = "Timestamp de criação")
    private Instant createdAt;

    @LastModifiedDate
    @Schema(description = "Timestamp da última atualização")
    private Instant updatedAt;

    public Contact() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getLinkedinProfile() {
        return linkedinProfile;
    }

    public void setLinkedinProfile(String linkedinProfile) {
        this.linkedinProfile = linkedinProfile;
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
}
