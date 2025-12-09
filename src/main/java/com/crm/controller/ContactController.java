package com.crm.controller;

import com.crm.model.Contact;
import com.crm.repository.CompanyRepository;
import com.crm.repository.ContactRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    private final ContactRepository contactRepository;
    private final CompanyRepository companyRepository;

    public ContactController(ContactRepository contactRepository, CompanyRepository companyRepository) {
        this.contactRepository = contactRepository;
        this.companyRepository = companyRepository;
    }

    @PostMapping
    public ResponseEntity<Contact> createContact(@RequestBody Contact contact) {
        if (contact.getCompanyId() != null && !companyRepository.existsById(contact.getCompanyId())) {
            return ResponseEntity.badRequest().build();
        }
        // basic normalization
        if (contact.getEmail() != null) contact.setEmail(contact.getEmail().trim().toLowerCase());
        Contact saved = contactRepository.save(contact);
        return ResponseEntity.created(URI.create("/api/contacts/" + saved.getId())).body(saved);
    }

    @GetMapping
    public List<Contact> listContacts(@RequestParam(value = "companyId", required = false) String companyId) {
        if (companyId != null && !companyId.isBlank()) {
            return contactRepository.findByCompanyId(companyId);
        }
        return contactRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContact(@PathVariable String id) {
        return contactRepository.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contact> updateContact(@PathVariable String id, @RequestBody Contact updated) {
        var opt = contactRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        Contact existing = opt.get();
        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        if (updated.getEmail() != null) existing.setEmail(updated.getEmail().trim().toLowerCase());
        existing.setJobTitle(updated.getJobTitle());
        if (updated.getCompanyId() != null && !companyRepository.existsById(updated.getCompanyId())) {
            return ResponseEntity.badRequest().build();
        }
        existing.setCompanyId(updated.getCompanyId());
        existing.setLinkedinProfile(updated.getLinkedinProfile());
        contactRepository.save(existing);
        return ResponseEntity.ok(existing);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable String id) {
        if (!contactRepository.existsById(id)) return ResponseEntity.notFound().build();
        contactRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
