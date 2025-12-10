package com.crm.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Address", description = "Endereço postal")
public class Address {
    @Schema(description = "Logradouro")
    private String street;

    @Schema(description = "Número")
    private String number;

    @Schema(description = "Complemento")
    private String complement;

    @Schema(description = "Bairro")
    private String neighborhood;

    @Schema(description = "Cidade")
    private String city;

    @Schema(description = "Estado")
    private String state;

    @Schema(description = "País")
    private String country;

    @Schema(description = "CEP / código postal")
    private String postalCode;

    public Address() {
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
