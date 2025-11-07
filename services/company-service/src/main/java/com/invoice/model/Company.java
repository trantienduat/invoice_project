package com.invoice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "companies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Company name is required")
    @Column(nullable = false, length = 200)
    private String name;
    
    @NotBlank(message = "Tax ID is required")
    @Column(unique = true, nullable = false, length = 50)
    private String taxId;
    
    @Column(length = 500)
    private String address;
    
    @Column(length = 100)
    private String city;
    
    @Column(length = 100)
    private String country;
    
    @Column(length = 20)
    private String postalCode;
    
    @Column(length = 50)
    private String phone;
    
    @Column(length = 100)
    private String email;
    
    @Column(length = 200)
    private String website;
    
    @Column(length = 20)
    private String status = "active";
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
