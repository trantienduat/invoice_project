package com.invoice.service;

import com.invoice.model.Issuer;
import com.invoice.repository.IssuerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class IssuerService {
    
    @Autowired
    private IssuerRepository issuerRepository;
    
    public List<Issuer> getAllIssuers(Long companyId, String status) {
        if (companyId != null && status != null) {
            return issuerRepository.findByCompanyIdAndStatus(companyId, status);
        } else if (companyId != null) {
            return issuerRepository.findByCompanyId(companyId);
        } else if (status != null) {
            return issuerRepository.findByStatus(status);
        }
        return issuerRepository.findAll();
    }
    
    public Optional<Issuer> getIssuerById(Long id) {
        return issuerRepository.findById(id);
    }
    
    @Transactional
    public Issuer createIssuer(Issuer issuer) {
        if (issuerRepository.findByEmail(issuer.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Issuer with this email already exists");
        }
        return issuerRepository.save(issuer);
    }
    
    @Transactional
    public Issuer updateIssuer(Long id, Issuer issuerDetails) {
        Issuer issuer = issuerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Issuer not found"));
        
        if (issuerDetails.getEmail() != null && !issuerDetails.getEmail().equals(issuer.getEmail())) {
            if (issuerRepository.findByEmail(issuerDetails.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Issuer with this email already exists");
            }
            issuer.setEmail(issuerDetails.getEmail());
        }
        
        if (issuerDetails.getName() != null) {
            issuer.setName(issuerDetails.getName());
        }
        if (issuerDetails.getPhone() != null) {
            issuer.setPhone(issuerDetails.getPhone());
        }
        if (issuerDetails.getDepartment() != null) {
            issuer.setDepartment(issuerDetails.getDepartment());
        }
        if (issuerDetails.getPosition() != null) {
            issuer.setPosition(issuerDetails.getPosition());
        }
        if (issuerDetails.getStatus() != null) {
            issuer.setStatus(issuerDetails.getStatus());
        }
        
        return issuerRepository.save(issuer);
    }
    
    @Transactional
    public void deleteIssuer(Long id) {
        if (!issuerRepository.existsById(id)) {
            throw new IllegalArgumentException("Issuer not found");
        }
        issuerRepository.deleteById(id);
    }
}
