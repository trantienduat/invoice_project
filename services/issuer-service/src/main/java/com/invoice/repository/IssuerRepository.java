package com.invoice.repository;

import com.invoice.model.Issuer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IssuerRepository extends JpaRepository<Issuer, Long> {
    Optional<Issuer> findByEmail(String email);
    List<Issuer> findByCompanyId(Long companyId);
    List<Issuer> findByStatus(String status);
    List<Issuer> findByCompanyIdAndStatus(Long companyId, String status);
}
