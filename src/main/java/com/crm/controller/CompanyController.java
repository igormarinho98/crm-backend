package com.crm.controller;

import com.crm.model.Company;
import com.crm.repository.CompanyRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyRepository companyRepository;

    public CompanyController(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @PostMapping
    public ResponseEntity<Company> createCompany(@RequestBody Company company) {
        // normalize CNPJ (simple): remove non-digits
        if (company.getCnpj() != null) {
            company.setCnpj(company.getCnpj().replaceAll("\\D",""));
        }
        Company saved = companyRepository.save(company);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable String id) {
        return companyRepository.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable String id) {
        return companyRepository.findById(id).map(c -> {
            companyRepository.deleteById(id);
            return ResponseEntity.noContent().<Void>build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
