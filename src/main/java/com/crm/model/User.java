package com.crm.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import io.swagger.v3.oas.annotations.media.Schema;

@Document(collection = "users")
@Schema(name = "User", description = "Usuário do sistema (autenticação/administrativo)")
public class User {
    @Id
    @Schema(description = "ID do usuário")
    private String id;

    @Schema(description = "Nome")
    private String firstName;

    @Schema(description = "Sobrenome")
    private String lastName;

    @Schema(description = "Email")
    private String email;

    public User() {
    }

    public User(String id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
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
}
