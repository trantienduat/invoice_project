package com.invoice.controller;

import com.invoice.exception.ResourceAlreadyExistsException;
import com.invoice.exception.ResourceNotFoundException;
import com.invoice.model.Issuer;
import com.invoice.service.IssuerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/issuers")
public class IssuerController {
    
    private final IssuerService issuerService;
    
    public IssuerController(IssuerService issuerService) {
        this.issuerService = issuerService;
    }
    
    @GetMapping
    public ResponseEntity<List<Issuer>> getAllIssuers(
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) String status) {
        List<Issuer> issuers = issuerService.getAllIssuers(companyId, status);
        return ResponseEntity.ok(issuers);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Issuer> getIssuerById(@PathVariable Long id) {
        return issuerService.getIssuerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<?> createIssuer(@Valid @RequestBody Issuer issuer) {
        try {
            Issuer createdIssuer = issuerService.createIssuer(issuer);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdIssuer);
        } catch (ResourceAlreadyExistsException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateIssuer(@PathVariable Long id, @RequestBody Issuer issuerDetails) {
        try {
            Issuer updatedIssuer = issuerService.updateIssuer(id, issuerDetails);
            return ResponseEntity.ok(updatedIssuer);
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (ResourceAlreadyExistsException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteIssuer(@PathVariable Long id) {
        try {
            issuerService.deleteIssuer(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Issuer deleted successfully");
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (ResourceAlreadyExistsException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> health = new HashMap<>();
        health.put("status", "healthy");
        health.put("service", "issuer-service");
        return ResponseEntity.ok(health);
    }
}
