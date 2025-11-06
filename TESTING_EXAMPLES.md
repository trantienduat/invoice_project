# Testing Examples

This document provides comprehensive examples for testing the Invoice Management System microservices.

## Prerequisites

Ensure all services are running:
- Invoice Service: http://localhost:5001
- Company Service: http://localhost:5002
- Issuer Service: http://localhost:5003
- Seller Service: http://localhost:5004

---

## Health Check Tests

Test that all services are running properly:

```bash
# Invoice Service
curl http://localhost:5001/api/invoices/health

# Company Service
curl http://localhost:5002/api/companies/health

# Issuer Service
curl http://localhost:5003/api/issuers/health

# Seller Service
curl http://localhost:5004/api/sellers/health
```

Expected response for each:
```json
{
  "status": "healthy",
  "service": "<service-name>"
}
```

---

## Complete Workflow Test

### Step 1: Create a Company

```bash
curl -X POST http://localhost:5002/api/companies/ \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Tech Innovations Inc",
    "tax_id": "12-3456789",
    "address": "123 Silicon Valley Blvd",
    "city": "San Francisco",
    "country": "USA",
    "postal_code": "94105",
    "phone": "+1-415-555-0100",
    "email": "contact@techinnovations.com",
    "website": "https://techinnovations.com"
  }'
```

Expected response:
```json
{
  "id": 1,
  "name": "Tech Innovations Inc",
  "tax_id": "12-3456789",
  "address": "123 Silicon Valley Blvd",
  "city": "San Francisco",
  "country": "USA",
  "postal_code": "94105",
  "phone": "+1-415-555-0100",
  "email": "contact@techinnovations.com",
  "website": "https://techinnovations.com",
  "status": "active",
  "created_at": "2024-01-01T00:00:00",
  "updated_at": "2024-01-01T00:00:00"
}
```

### Step 2: Create an Issuer

```bash
curl -X POST http://localhost:5003/api/issuers/ \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Alice Johnson",
    "email": "alice.johnson@techinnovations.com",
    "phone": "+1-415-555-0101",
    "department": "Finance",
    "position": "Senior Accountant",
    "company_id": 1
  }'
```

Expected response:
```json
{
  "id": 1,
  "name": "Alice Johnson",
  "email": "alice.johnson@techinnovations.com",
  "phone": "+1-415-555-0101",
  "department": "Finance",
  "position": "Senior Accountant",
  "company_id": 1,
  "status": "active",
  "created_at": "2024-01-01T00:00:00",
  "updated_at": "2024-01-01T00:00:00"
}
```

### Step 3: Create a Seller

```bash
curl -X POST http://localhost:5004/api/sellers/ \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Bob Williams",
    "email": "bob.williams@techinnovations.com",
    "phone": "+1-415-555-0102",
    "company_id": 1,
    "commission_rate": 0.08,
    "territory": "West Coast"
  }'
```

Expected response:
```json
{
  "id": 1,
  "name": "Bob Williams",
  "email": "bob.williams@techinnovations.com",
  "phone": "+1-415-555-0102",
  "company_id": 1,
  "commission_rate": 0.08,
  "territory": "West Coast",
  "status": "active",
  "created_at": "2024-01-01T00:00:00",
  "updated_at": "2024-01-01T00:00:00"
}
```

### Step 4: Create an Invoice

```bash
curl -X POST http://localhost:5001/api/invoices/ \
  -H "Content-Type: application/json" \
  -d '{
    "invoice_number": "INV-2024-0001",
    "company_id": 1,
    "issuer_id": 1,
    "seller_id": 1,
    "amount": 15000.00,
    "currency": "USD",
    "status": "pending",
    "due_date": "2024-02-15T00:00:00",
    "description": "Software Development Services - Q1 2024"
  }'
```

Expected response:
```json
{
  "id": 1,
  "invoice_number": "INV-2024-0001",
  "company_id": 1,
  "issuer_id": 1,
  "seller_id": 1,
  "amount": 15000.00,
  "currency": "USD",
  "status": "pending",
  "issue_date": "2024-01-15T00:00:00",
  "due_date": "2024-02-15T00:00:00",
  "description": "Software Development Services - Q1 2024",
  "created_at": "2024-01-15T00:00:00",
  "updated_at": "2024-01-15T00:00:00"
}
```

---

## CRUD Operations Examples

### Company Service

**List all companies:**
```bash
curl http://localhost:5002/api/companies/
```

**Get specific company:**
```bash
curl http://localhost:5002/api/companies/1
```

**Update company:**
```bash
curl -X PUT http://localhost:5002/api/companies/1 \
  -H "Content-Type: application/json" \
  -d '{
    "phone": "+1-415-555-0199",
    "website": "https://www.techinnovations.com"
  }'
```

**Filter companies by status:**
```bash
curl "http://localhost:5002/api/companies/?status=active"
```

**Filter companies by country:**
```bash
curl "http://localhost:5002/api/companies/?country=USA"
```

**Delete company:**
```bash
curl -X DELETE http://localhost:5002/api/companies/1
```

### Invoice Service

**List all invoices:**
```bash
curl http://localhost:5001/api/invoices/
```

**Get specific invoice:**
```bash
curl http://localhost:5001/api/invoices/1
```

**Update invoice status:**
```bash
curl -X PUT http://localhost:5001/api/invoices/1 \
  -H "Content-Type: application/json" \
  -d '{
    "status": "paid"
  }'
```

**Filter invoices by company:**
```bash
curl "http://localhost:5001/api/invoices/?company_id=1"
```

**Filter invoices by status:**
```bash
curl "http://localhost:5001/api/invoices/?status=pending"
```

**Delete invoice:**
```bash
curl -X DELETE http://localhost:5001/api/invoices/1
```

### Issuer Service

**List all issuers:**
```bash
curl http://localhost:5003/api/issuers/
```

**Get specific issuer:**
```bash
curl http://localhost:5003/api/issuers/1
```

**Update issuer:**
```bash
curl -X PUT http://localhost:5003/api/issuers/1 \
  -H "Content-Type: application/json" \
  -d '{
    "position": "Lead Accountant",
    "phone": "+1-415-555-0105"
  }'
```

**Filter issuers by company:**
```bash
curl "http://localhost:5003/api/issuers/?company_id=1"
```

**Filter issuers by status:**
```bash
curl "http://localhost:5003/api/issuers/?status=active"
```

**Delete issuer:**
```bash
curl -X DELETE http://localhost:5003/api/issuers/1
```

### Seller Service

**List all sellers:**
```bash
curl http://localhost:5004/api/sellers/
```

**Get specific seller:**
```bash
curl http://localhost:5004/api/sellers/1
```

**Update seller:**
```bash
curl -X PUT http://localhost:5004/api/sellers/1 \
  -H "Content-Type: application/json" \
  -d '{
    "commission_rate": 0.10,
    "territory": "West Coast & Mountain States"
  }'
```

**Filter sellers by company:**
```bash
curl "http://localhost:5004/api/sellers/?company_id=1"
```

**Filter sellers by territory:**
```bash
curl "http://localhost:5004/api/sellers/?territory=West%20Coast"
```

**Filter sellers by status:**
```bash
curl "http://localhost:5004/api/sellers/?status=active"
```

**Delete seller:**
```bash
curl -X DELETE http://localhost:5004/api/sellers/1
```

---

## Bulk Data Creation

### Create Multiple Companies

```bash
# Company 1
curl -X POST http://localhost:5002/api/companies/ \
  -H "Content-Type: application/json" \
  -d '{"name": "Global Corp", "tax_id": "98-7654321", "country": "USA"}'

# Company 2
curl -X POST http://localhost:5002/api/companies/ \
  -H "Content-Type: application/json" \
  -d '{"name": "European Industries", "tax_id": "EU-123456", "country": "Germany"}'

# Company 3
curl -X POST http://localhost:5002/api/companies/ \
  -H "Content-Type: application/json" \
  -d '{"name": "Asia Pacific Ltd", "tax_id": "AP-789012", "country": "Singapore"}'
```

### Create Multiple Issuers

```bash
# Issuer 1
curl -X POST http://localhost:5003/api/issuers/ \
  -H "Content-Type: application/json" \
  -d '{"name": "Carol Davis", "email": "carol.davis@globalcorp.com", "company_id": 2, "department": "Accounting"}'

# Issuer 2
curl -X POST http://localhost:5003/api/issuers/ \
  -H "Content-Type: application/json" \
  -d '{"name": "David Chen", "email": "david.chen@asiapacific.com", "company_id": 3, "department": "Finance"}'
```

### Create Multiple Sellers

```bash
# Seller 1
curl -X POST http://localhost:5004/api/sellers/ \
  -H "Content-Type: application/json" \
  -d '{"name": "Emma Thompson", "email": "emma.t@globalcorp.com", "company_id": 2, "commission_rate": 0.05, "territory": "East Coast"}'

# Seller 2
curl -X POST http://localhost:5004/api/sellers/ \
  -H "Content-Type: application/json" \
  -d '{"name": "Frank Lee", "email": "frank.lee@asiapacific.com", "company_id": 3, "commission_rate": 0.07, "territory": "APAC"}'
```

### Create Multiple Invoices

```bash
# Invoice 1
curl -X POST http://localhost:5001/api/invoices/ \
  -H "Content-Type: application/json" \
  -d '{"invoice_number": "INV-2024-0002", "company_id": 2, "issuer_id": 2, "seller_id": 2, "amount": 25000.00, "description": "Consulting Services"}'

# Invoice 2
curl -X POST http://localhost:5001/api/invoices/ \
  -H "Content-Type: application/json" \
  -d '{"invoice_number": "INV-2024-0003", "company_id": 3, "issuer_id": 3, "seller_id": 3, "amount": 18500.00, "description": "Product License"}'
```

---

## Error Handling Tests

### Test Duplicate Invoice Number

```bash
# First invoice
curl -X POST http://localhost:5001/api/invoices/ \
  -H "Content-Type: application/json" \
  -d '{"invoice_number": "INV-DUP-001", "company_id": 1, "issuer_id": 1, "seller_id": 1, "amount": 1000.00}'

# Try to create duplicate
curl -X POST http://localhost:5001/api/invoices/ \
  -H "Content-Type: application/json" \
  -d '{"invoice_number": "INV-DUP-001", "company_id": 1, "issuer_id": 1, "seller_id": 1, "amount": 2000.00}'
```

Expected error:
```json
{
  "error": "Invoice number already exists"
}
```

### Test Missing Required Field

```bash
curl -X POST http://localhost:5002/api/companies/ \
  -H "Content-Type: application/json" \
  -d '{"name": "Incomplete Company"}'
```

Expected error:
```json
{
  "error": "Missing required field: tax_id"
}
```

### Test Non-existent Resource

```bash
curl http://localhost:5001/api/invoices/99999
```

Expected error:
```json
{
  "error": "404 Not Found: The requested resource was not found"
}
```

---

## Performance Testing

### Load Test Example (using Apache Bench)

```bash
# Test invoice health endpoint
ab -n 1000 -c 10 http://localhost:5001/api/invoices/health

# Test company list endpoint
ab -n 500 -c 5 http://localhost:5002/api/companies/
```

### Response Time Testing

```bash
# Test response time
time curl http://localhost:5001/api/invoices/

# Test with verbose output
curl -w "\nTime: %{time_total}s\n" http://localhost:5002/api/companies/
```

---

## Automated Test Script

Create a file `test_all_services.sh`:

```bash
#!/bin/bash

echo "==================================="
echo "Invoice Management System Tests"
echo "==================================="
echo ""

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

test_endpoint() {
    local url=$1
    local description=$2
    
    response=$(curl -s -o /dev/null -w "%{http_code}" "$url")
    
    if [ "$response" -eq 200 ] || [ "$response" -eq 201 ]; then
        echo -e "${GREEN}✓${NC} $description (HTTP $response)"
    else
        echo -e "${RED}✗${NC} $description (HTTP $response)"
    fi
}

echo "1. Health Checks"
test_endpoint "http://localhost:5001/api/invoices/health" "Invoice Service"
test_endpoint "http://localhost:5002/api/companies/health" "Company Service"
test_endpoint "http://localhost:5003/api/issuers/health" "Issuer Service"
test_endpoint "http://localhost:5004/api/sellers/health" "Seller Service"

echo ""
echo "2. List Endpoints"
test_endpoint "http://localhost:5001/api/invoices/" "List Invoices"
test_endpoint "http://localhost:5002/api/companies/" "List Companies"
test_endpoint "http://localhost:5003/api/issuers/" "List Issuers"
test_endpoint "http://localhost:5004/api/sellers/" "List Sellers"

echo ""
echo "==================================="
echo "All tests completed!"
echo "==================================="
```

Make it executable and run:
```bash
chmod +x test_all_services.sh
./test_all_services.sh
```

---

## Using Python for Testing

Create a file `test_services.py`:

```python
import requests
import json

BASE_URLS = {
    'invoice': 'http://localhost:5001/api/invoices',
    'company': 'http://localhost:5002/api/companies',
    'issuer': 'http://localhost:5003/api/issuers',
    'seller': 'http://localhost:5004/api/sellers'
}

def test_health_checks():
    print("Testing Health Checks...")
    for service, url in BASE_URLS.items():
        try:
            response = requests.get(f"{url}/health")
            if response.status_code == 200:
                print(f"✓ {service} service is healthy")
            else:
                print(f"✗ {service} service returned {response.status_code}")
        except Exception as e:
            print(f"✗ {service} service error: {e}")

def test_create_company():
    print("\nTesting Company Creation...")
    company_data = {
        "name": "Test Company",
        "tax_id": f"TEST-{hash('test')}",
        "country": "USA"
    }
    
    response = requests.post(
        f"{BASE_URLS['company']}/",
        json=company_data,
        headers={'Content-Type': 'application/json'}
    )
    
    if response.status_code == 201:
        print("✓ Company created successfully")
        return response.json()['id']
    else:
        print(f"✗ Failed to create company: {response.text}")
        return None

if __name__ == "__main__":
    test_health_checks()
    company_id = test_create_company()
    print(f"\nCreated company with ID: {company_id}")
```

Run with:
```bash
pip install requests
python test_services.py
```

---

## Notes

- Replace `localhost` with your server's IP address or domain if testing remotely
- Use tools like Postman or Insomnia for more interactive testing
- For production testing, ensure you have proper authentication in place
- Always test on a non-production environment first
