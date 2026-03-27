package com.example.delivery.controller;

import com.example.delivery.entity.BaseFee;
import com.example.delivery.service.BaseFeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/base-fees")
@RequiredArgsConstructor
public class BaseFeeController {

    private final BaseFeeService baseFeeService;

    @GetMapping
    public ResponseEntity<List<BaseFee>> getAll() {
        return ResponseEntity.ok(baseFeeService.getAllBaseFees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseFee> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(baseFeeService.getBaseFeeById(id));
    }

    @PostMapping
    public ResponseEntity<BaseFee> create(@RequestBody BaseFee baseFee) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(baseFeeService.createBaseFee(baseFee));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseFee> update(@PathVariable UUID id, @RequestBody BaseFee baseFee) {
        return ResponseEntity.ok(baseFeeService.updateBaseFee(id, baseFee));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        baseFeeService.deleteBaseFee(id);
        return ResponseEntity.noContent().build();
    }
}