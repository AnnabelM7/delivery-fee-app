package com.example.delivery.controller;

import com.example.delivery.entity.ExtraFee;
import com.example.delivery.service.ExtraFeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/extra-fees")
@RequiredArgsConstructor
public class ExtraFeeController {

    private final ExtraFeeService extraFeeService;

    /**
     * Returns all extra fee rules.
     */
    @GetMapping
    public ResponseEntity<List<ExtraFee>> getAll() {
        return ResponseEntity.ok(extraFeeService.getAllRules());
    }

    /**
     * Returns an extra fee rule by ID.
     *
     * @param id the rule ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExtraFee> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(extraFeeService.getRuleById(id));
    }

    /**
     * Creates a new extra fee rule.
     *
     * @param rule the rule to create
     */
    @PostMapping
    public ResponseEntity<ExtraFee> create(@RequestBody ExtraFee rule) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(extraFeeService.createRule(rule));
    }

    /**
     * Updates an existing extra fee rule.
     *
     * @param id   the ID of the rule to update
     * @param rule the new values
     */
    @PutMapping("/{id}")
    public ResponseEntity<ExtraFee> update(@PathVariable UUID id, @RequestBody ExtraFee rule) {
        return ResponseEntity.ok(extraFeeService.updateRule(id, rule));
    }

    /**
     * Deletes an extra fee rule by ID.
     *
     * @param id the ID of the rule to delete
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        extraFeeService.deleteRule(id);
        return ResponseEntity.noContent().build();
    }
}