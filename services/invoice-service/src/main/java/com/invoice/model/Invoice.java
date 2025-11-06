package com.invoice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Invoice number is required")
    @Column(unique = true, nullable = false, length = 100)
    private String invoiceNumber;
    
    @NotNull(message = "Company ID is required")
    private Long companyId;
    
    @NotNull(message = "Issuer ID is required")
    private Long issuerId;
    
    @NotNull(message = "Seller ID is required")
    private Long sellerId;
    
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;
    
    @Column(length = 3)
    private String currency = "USD";
    
    @Column(length = 20)
    private String status = "pending";
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime issueDate;
    
    private LocalDateTime dueDate;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
