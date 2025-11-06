package com.invoice.controller;

import com.invoice.model.Seller;
import com.invoice.service.SellerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sellers")
public class SellerController {
    
    @Autowired
    private SellerService sellerService;
    
    @GetMapping
    public ResponseEntity<List<Seller>> getAllSellers(
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String territory) {
        List<Seller> sellers = sellerService.getAllSellers(companyId, status, territory);
        return ResponseEntity.ok(sellers);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable Long id) {
        return sellerService.getSellerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<?> createSeller(@Valid @RequestBody Seller seller) {
        try {
            Seller createdSeller = sellerService.createSeller(seller);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSeller);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSeller(@PathVariable Long id, @RequestBody Seller sellerDetails) {
        try {
            Seller updatedSeller = sellerService.updateSeller(id, sellerDetails);
            return ResponseEntity.ok(updatedSeller);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSeller(@PathVariable Long id) {
        try {
            sellerService.deleteSeller(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Seller deleted successfully");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> health = new HashMap<>();
        health.put("status", "healthy");
        health.put("service", "seller-service");
        return ResponseEntity.ok(health);
    }
}
