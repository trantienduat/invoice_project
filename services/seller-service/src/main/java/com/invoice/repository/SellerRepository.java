package com.invoice.repository;

import com.invoice.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
    Optional<Seller> findByEmail(String email);
    List<Seller> findByCompanyId(Long companyId);
    List<Seller> findByStatus(String status);
    List<Seller> findByTerritory(String territory);
}
