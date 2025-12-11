package com.crm.controller;

import com.crm.model.Activity;
import com.crm.repository.ActivityRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@Tag(name = "Activities", description = "APIs para gerenciar atividades (chamadas, tarefas, e-mails)")
public class ActivityController {

    private final ActivityRepository activityRepository;

    public ActivityController(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Operation(summary = "Criar activity", description = "Cria uma nova atividade (call, task, email)")
    @PostMapping
    public ResponseEntity<Activity> create(@RequestBody Activity activity) {
        Activity saved = activityRepository.save(activity);
        return ResponseEntity.ok(saved);
    }

    @Operation(summary = "Listar activities", description = "Retorna todas as activities; filtrar por companyId via query param")
    @GetMapping
    public List<Activity> getAll(@Parameter(description = "Filtrar por companyId") @RequestParam(value = "companyId", required = false) String companyId) {
        if (companyId != null && !companyId.isBlank()) {
            return activityRepository.findByCompanyId(companyId);
        }
        return activityRepository.findAll();
    }

    @Operation(summary = "Buscar activity", description = "Retorna activity por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Activity> getById(@PathVariable String id) {
        return activityRepository.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Atualizar activity", description = "Atualiza uma activity existente")
    @PutMapping("/{id}")
    public ResponseEntity<Activity> update(@PathVariable String id, @RequestBody Activity updated) {
        return activityRepository.findById(id).map(existing -> {
            existing.setType(updated.getType());
            existing.setDescription(updated.getDescription());
            existing.setDueDate(updated.getDueDate());
            existing.setRelatedDealId(updated.getRelatedDealId());
            existing.setAssignedToUserId(updated.getAssignedToUserId());
            activityRepository.save(existing);
            return ResponseEntity.ok(existing);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Excluir activity", description = "Remove uma activity por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        return activityRepository.findById(id).map(a -> {
            activityRepository.deleteById(id);
            return ResponseEntity.noContent().<Void>build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
