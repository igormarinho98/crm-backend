package com.crm.controller;

import com.crm.model.Company;
import com.crm.repository.CompanyRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@Tag(name = "Companies", description = "APIs para gerenciar empresas (companies)")
public class CompanyController {

    private final CompanyRepository companyRepository;

    public CompanyController(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Operation(summary = "Criar empresa", description = "Cria uma nova empresa")
    @PostMapping
    public ResponseEntity<Company> createCompany(@RequestBody Company company) {
        // normalize CNPJ (simple): remove non-digits
        if (company.getCnpj() != null) {
            company.setCnpj(company.getCnpj().replaceAll("\\D",""));
        }
        Company saved = companyRepository.save(company);
        return ResponseEntity.ok(saved);
    }

    @Operation(summary = "Listar empresas", description = "Retorna a lista de empresas cadastradas")
    @GetMapping
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Operation(summary = "Buscar empresa", description = "Retorna uma empresa por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable String id) {
        return companyRepository.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Atualizar empresa", description = "Atualiza os dados de uma empresa existente")
    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable String id, @RequestBody Company updated) {
        return companyRepository.findById(id).map(existing -> {
            existing.setName(updated.getName());
            existing.setCnpj(updated.getCnpj() == null ? existing.getCnpj() : updated.getCnpj().replaceAll("\\D",""));
            existing.setPhone(updated.getPhone());
            existing.setWebsite(updated.getWebsite());
            existing.setAddress(updated.getAddress());
            existing.setStatus(updated.getStatus());
            existing.setTags(updated.getTags());
            companyRepository.save(existing);
            return ResponseEntity.ok(existing);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Excluir empresa", description = "Remove uma empresa por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable String id) {
        return companyRepository.findById(id).map(c -> {
            companyRepository.deleteById(id);
            return ResponseEntity.noContent().<Void>build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
