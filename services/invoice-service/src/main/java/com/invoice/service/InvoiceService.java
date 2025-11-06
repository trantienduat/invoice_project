package com.invoice.service;

import com.invoice.exception.ResourceAlreadyExistsException;
import com.invoice.exception.ResourceNotFoundException;
import com.invoice.model.Invoice;
import com.invoice.repository.InvoiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {
    
    private final InvoiceRepository invoiceRepository;
    
    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }
    
    public List<Invoice> getAllInvoices(Long companyId, String status) {
        if (companyId != null && status != null) {
            return invoiceRepository.findByCompanyIdAndStatus(companyId, status);
        } else if (companyId != null) {
            return invoiceRepository.findByCompanyId(companyId);
        } else if (status != null) {
            return invoiceRepository.findByStatus(status);
        }
        return invoiceRepository.findAll();
    }
    
    public Optional<Invoice> getInvoiceById(Long id) {
        return invoiceRepository.findById(id);
    }
    
    @Transactional
    public Invoice createInvoice(Invoice invoice) {
        if (invoiceRepository.findByInvoiceNumber(invoice.getInvoiceNumber()).isPresent()) {
            throw new ResourceAlreadyExistsException("Invoice number already exists");
        }
        return invoiceRepository.save(invoice);
    }
    
    @Transactional
    public Invoice updateInvoice(Long id, Invoice invoiceDetails) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
        
        if (invoiceDetails.getAmount() != null) {
            invoice.setAmount(invoiceDetails.getAmount());
        }
        if (invoiceDetails.getCurrency() != null) {
            invoice.setCurrency(invoiceDetails.getCurrency());
        }
        if (invoiceDetails.getStatus() != null) {
            invoice.setStatus(invoiceDetails.getStatus());
        }
        if (invoiceDetails.getDescription() != null) {
            invoice.setDescription(invoiceDetails.getDescription());
        }
        if (invoiceDetails.getDueDate() != null) {
            invoice.setDueDate(invoiceDetails.getDueDate());
        }
        
        return invoiceRepository.save(invoice);
    }
    
    @Transactional
    public void deleteInvoice(Long id) {
        if (!invoiceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Invoice not found");
        }
        invoiceRepository.deleteById(id);
    }
}
