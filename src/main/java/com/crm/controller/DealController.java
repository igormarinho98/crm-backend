package com.crm.controller;

import com.crm.model.Deal;
import com.crm.model.PipelineStage;
import com.crm.repository.CompanyRepository;
import com.crm.repository.ContactRepository;
import com.crm.repository.DealRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/deals")
@Tag(name = "Deals", description = "APIs para gerenciar oportunidades (deals)")
public class DealController {

    private final DealRepository dealRepository;
    private final CompanyRepository companyRepository;
    private final ContactRepository contactRepository;

    public DealController(DealRepository dealRepository, CompanyRepository companyRepository, ContactRepository contactRepository) {
        this.dealRepository = dealRepository;
        this.companyRepository = companyRepository;
        this.contactRepository = contactRepository;
    }

    @Operation(summary = "Criar oportunidade", description = "Cria um novo negócio (deal). companyId é obrigatório.")
    @PostMapping
    public ResponseEntity<Deal> createDeal(@RequestBody Deal deal) {
        if (deal.getCompanyId() == null || !companyRepository.existsById(deal.getCompanyId())) {
            return ResponseEntity.badRequest().build();
        }
        if (deal.getContactId() != null && !contactRepository.existsById(deal.getContactId())) {
            return ResponseEntity.badRequest().build();
        }
        if (deal.getDealValue() == null) deal.setDealValue(BigDecimal.ZERO);
        Deal saved = dealRepository.save(deal);
        return ResponseEntity.created(URI.create("/api/deals/" + saved.getId())).body(saved);
    }

    @Operation(summary = "Listar oportunidades", description = "Lista negócios; filtrar por companyId, contactId ou pipelineStage")
    @GetMapping
    public List<Deal> listDeals(@RequestParam(value = "companyId", required = false) String companyId,
                                @RequestParam(value = "contactId", required = false) String contactId,
                                @RequestParam(value = "pipelineStage", required = false) PipelineStage pipelineStage) {
        if (companyId != null) return dealRepository.findByCompanyId(companyId);
        if (contactId != null) return dealRepository.findByContactId(contactId);
        if (pipelineStage != null) return dealRepository.findByPipelineStage(pipelineStage);
        return dealRepository.findAll();
    }

    @Operation(summary = "Buscar oportunidade", description = "Retorna um negócio por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Deal> getDeal(@PathVariable String id) {
        return dealRepository.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Atualizar oportunidade", description = "Atualiza um negócio existente")
    @PutMapping("/{id}")
    public ResponseEntity<Deal> updateDeal(@PathVariable String id, @RequestBody Deal updated) {
        var opt = dealRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        Deal existing = opt.get();
        existing.setTitle(updated.getTitle());
        if (updated.getCompanyId() != null && !companyRepository.existsById(updated.getCompanyId())) return ResponseEntity.badRequest().build();
        existing.setCompanyId(updated.getCompanyId());
        if (updated.getContactId() != null && !contactRepository.existsById(updated.getContactId())) return ResponseEntity.badRequest().build();
        existing.setContactId(updated.getContactId());
        existing.setDealValue(updated.getDealValue());
        existing.setCurrency(updated.getCurrency());
        existing.setPipelineStage(updated.getPipelineStage());
        existing.setProbability(updated.getProbability());
        existing.setExpectedCloseDate(updated.getExpectedCloseDate());
        existing.setNotes(updated.getNotes());
        dealRepository.save(existing);
        return ResponseEntity.ok(existing);
    }

    @Operation(summary = "Alterar estágio", description = "Atualiza o estágio do pipeline e campos relacionados")
    @PatchMapping("/{id}/stage")
    public ResponseEntity<Deal> changeStage(@PathVariable String id, @RequestBody Deal partial) {
        var opt = dealRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        Deal existing = opt.get();
        if (partial.getPipelineStage() != null) existing.setPipelineStage(partial.getPipelineStage());
        if (partial.getProbability() != null) existing.setProbability(partial.getProbability());
        if (partial.getExpectedCloseDate() != null) existing.setExpectedCloseDate(partial.getExpectedCloseDate());
        dealRepository.save(existing);
        return ResponseEntity.ok(existing);
    }

    @Operation(summary = "Excluir oportunidade", description = "Remove um negócio por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeal(@PathVariable String id) {
        if (!dealRepository.existsById(id)) return ResponseEntity.notFound().build();
        dealRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
