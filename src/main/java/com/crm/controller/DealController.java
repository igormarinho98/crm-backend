package com.crm.controller;

import com.crm.model.Deal;
import com.crm.model.PipelineStage;
import com.crm.repository.CompanyRepository;
import com.crm.repository.ContactRepository;
import com.crm.repository.DealRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import com.crm.dto.DealDetailsDTO;
import com.crm.dto.FunnelStageStats;
import com.crm.service.DealService;
import com.crm.model.Activity;
import com.crm.model.ActivityType;
import com.crm.repository.ActivityRepository;

@RestController
@RequestMapping("/api/deals")
@Tag(name = "Deals", description = "APIs para gerenciar oportunidades (deals)")
public class DealController {

    private final DealRepository dealRepository;
    private final CompanyRepository companyRepository;
    private final ContactRepository contactRepository;
    private final ActivityRepository activityRepository;
    private final DealService dealService;

    public DealController(DealRepository dealRepository, CompanyRepository companyRepository, ContactRepository contactRepository, ActivityRepository activityRepository, DealService dealService) {
        this.dealRepository = dealRepository;
        this.companyRepository = companyRepository;
        this.contactRepository = contactRepository;
        this.activityRepository = activityRepository;
        this.dealService = dealService;
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
    public List<Deal> listDeals(@Parameter(description = "Filtrar por companyId") @RequestParam(value = "companyId", required = false) String companyId,
                                @Parameter(description = "Filtrar por contactId") @RequestParam(value = "contactId", required = false) String contactId,
                                @Parameter(description = "Filtrar por pipelineStage") @RequestParam(value = "pipelineStage", required = false) PipelineStage pipelineStage) {
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

    @Operation(summary = "Detalhes do negócio", description = "Retorna um negócio com company e contact embutidos")
    @GetMapping("/{id}/details")
    public ResponseEntity<DealDetailsDTO> getDealDetails(@PathVariable String id) {
        return dealRepository.findDealWithRelations(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Dashboard de funil", description = "Retorna soma dos valores e contagem de deals por pipeline stage. Opcional: filtrar por companyId")
    @GetMapping("/funnel")
    public ResponseEntity<List<FunnelStageStats>> getFunnel(@RequestParam(value = "companyId", required = false) String companyId) {
        List<FunnelStageStats> stats = dealService.getFunnelStats(companyId);
        return ResponseEntity.ok(stats);
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
        PipelineStage previousStage = existing.getPipelineStage();
        PipelineStage newStage = updated.getPipelineStage();
        existing.setPipelineStage(newStage);
        existing.setProbability(updated.getProbability());
        existing.setExpectedCloseDate(updated.getExpectedCloseDate());
        existing.setNotes(updated.getNotes());
        // If stage moved to FECHADO_GANHO, update timestamp and register an activity
        if (newStage == PipelineStage.FECHADO_GANHO && previousStage != PipelineStage.FECHADO_GANHO) {
            existing.setUpdatedAt(java.time.Instant.now());
            // create a closing activity
            try {
                Activity act = new Activity();
                act.setType(ActivityType.TASK);
                act.setDescription("Deal closed (won): " + (existing.getTitle() != null ? existing.getTitle() : existing.getId()));
                act.setRelatedDealId(existing.getId());
                activityRepository.save(act);
            } catch (Exception ex) {
                // log and continue
                System.err.println("Failed to create activity on deal close: " + ex.getMessage());
            }
        }

        dealRepository.save(existing);
        return ResponseEntity.ok(existing);
    }

    @Operation(summary = "Alterar estágio", description = "Atualiza o estágio do pipeline e campos relacionados")
    @PatchMapping("/{id}/stage")
    public ResponseEntity<Deal> changeStage(@PathVariable String id, @RequestBody Deal partial) {
        var opt = dealRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        Deal existing = opt.get();
        PipelineStage previous = existing.getPipelineStage();
        if (partial.getPipelineStage() != null) {
            existing.setPipelineStage(partial.getPipelineStage());
            // if changed to FECHADO_GANHO, set updatedAt and create activity
            if (partial.getPipelineStage() == PipelineStage.FECHADO_GANHO && previous != PipelineStage.FECHADO_GANHO) {
                existing.setUpdatedAt(java.time.Instant.now());
                try {
                    Activity act = new Activity();
                    act.setType(ActivityType.TASK);
                    act.setDescription("Deal closed (won): " + (existing.getTitle() != null ? existing.getTitle() : existing.getId()));
                    act.setRelatedDealId(existing.getId());
                    activityRepository.save(act);
                } catch (Exception ex) {
                    System.err.println("Failed to create activity on deal close: " + ex.getMessage());
                }
            }
        }
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
