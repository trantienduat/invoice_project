package com.invoice.service;

import com.invoice.model.Company;
import com.invoice.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    
    @Autowired
    private CompanyRepository companyRepository;
    
    public List<Company> getAllCompanies(String status, String country) {
        if (status != null) {
            return companyRepository.findByStatus(status);
        } else if (country != null) {
            return companyRepository.findByCountry(country);
        }
        return companyRepository.findAll();
    }
    
    public Optional<Company> getCompanyById(Long id) {
        return companyRepository.findById(id);
    }
    
    @Transactional
    public Company createCompany(Company company) {
        if (companyRepository.findByTaxId(company.getTaxId()).isPresent()) {
            throw new IllegalArgumentException("Company with this tax ID already exists");
        }
        return companyRepository.save(company);
    }
    
    @Transactional
    public Company updateCompany(Long id, Company companyDetails) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));
        
        if (companyDetails.getName() != null) {
            company.setName(companyDetails.getName());
        }
        if (companyDetails.getAddress() != null) {
            company.setAddress(companyDetails.getAddress());
        }
        if (companyDetails.getCity() != null) {
            company.setCity(companyDetails.getCity());
        }
        if (companyDetails.getCountry() != null) {
            company.setCountry(companyDetails.getCountry());
        }
        if (companyDetails.getPostalCode() != null) {
            company.setPostalCode(companyDetails.getPostalCode());
        }
        if (companyDetails.getPhone() != null) {
            company.setPhone(companyDetails.getPhone());
        }
        if (companyDetails.getEmail() != null) {
            company.setEmail(companyDetails.getEmail());
        }
        if (companyDetails.getWebsite() != null) {
            company.setWebsite(companyDetails.getWebsite());
        }
        if (companyDetails.getStatus() != null) {
            company.setStatus(companyDetails.getStatus());
        }
        
        return companyRepository.save(company);
    }
    
    @Transactional
    public void deleteCompany(Long id) {
        if (!companyRepository.existsById(id)) {
            throw new IllegalArgumentException("Company not found");
        }
        companyRepository.deleteById(id);
    }
}
