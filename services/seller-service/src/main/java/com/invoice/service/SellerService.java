package com.invoice.service;

import com.invoice.exception.ResourceAlreadyExistsException;
import com.invoice.exception.ResourceNotFoundException;
import com.invoice.model.Seller;
import com.invoice.repository.SellerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SellerService {
    
    private final SellerRepository sellerRepository;
    
    public SellerService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }
    
    public List<Seller> getAllSellers(Long companyId, String status, String territory) {
        if (companyId != null) {
            return sellerRepository.findByCompanyId(companyId);
        } else if (status != null) {
            return sellerRepository.findByStatus(status);
        } else if (territory != null) {
            return sellerRepository.findByTerritory(territory);
        }
        return sellerRepository.findAll();
    }
    
    public Optional<Seller> getSellerById(Long id) {
        return sellerRepository.findById(id);
    }
    
    @Transactional
    public Seller createSeller(Seller seller) {
        if (sellerRepository.findByEmail(seller.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistsException("Seller with this email already exists");
        }
        return sellerRepository.save(seller);
    }
    
    @Transactional
    public Seller updateSeller(Long id, Seller sellerDetails) {
        Seller seller = sellerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found"));
        
        if (sellerDetails.getEmail() != null && !sellerDetails.getEmail().equals(seller.getEmail())) {
            if (sellerRepository.findByEmail(sellerDetails.getEmail()).isPresent()) {
                throw new ResourceAlreadyExistsException("Seller with this email already exists");
            }
            seller.setEmail(sellerDetails.getEmail());
        }
        
        if (sellerDetails.getName() != null) {
            seller.setName(sellerDetails.getName());
        }
        if (sellerDetails.getPhone() != null) {
            seller.setPhone(sellerDetails.getPhone());
        }
        if (sellerDetails.getCommissionRate() != null) {
            seller.setCommissionRate(sellerDetails.getCommissionRate());
        }
        if (sellerDetails.getTerritory() != null) {
            seller.setTerritory(sellerDetails.getTerritory());
        }
        if (sellerDetails.getStatus() != null) {
            seller.setStatus(sellerDetails.getStatus());
        }
        
        return sellerRepository.save(seller);
    }
    
    @Transactional
    public void deleteSeller(Long id) {
        if (!sellerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Seller not found");
        }
        sellerRepository.deleteById(id);
    }
}
