package com.invoice.repository;

import com.invoice.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByTaxId(String taxId);
    List<Company> findByStatus(String status);
    List<Company> findByCountry(String country);
}
