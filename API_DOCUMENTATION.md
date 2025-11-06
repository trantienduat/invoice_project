# API Documentation

## Overview

This document provides detailed API documentation for all microservices in the Invoice Management System.

## Base URLs

- Invoice Service: `http://localhost:5001`
- Company Service: `http://localhost:5002`
- Issuer Service: `http://localhost:5003`
- Seller Service: `http://localhost:5004`

## Common Response Codes

- `200 OK` - Request successful
- `201 Created` - Resource created successfully
- `400 Bad Request` - Invalid request data
- `404 Not Found` - Resource not found
- `409 Conflict` - Resource already exists
- `500 Internal Server Error` - Server error

---

## Invoice Service API

### Get All Invoices
**Endpoint:** `GET /api/invoices/`

**Query Parameters:**
- `company_id` (optional) - Filter by company ID
- `status` (optional) - Filter by status (pending, paid, cancelled)

**Response:**
```json
[
  {
    "id": 1,
    "invoice_number": "INV-001",
    "company_id": 1,
    "issuer_id": 1,
    "seller_id": 1,
    "amount": 1000.00,
    "currency": "USD",
    "status": "pending",
    "issue_date": "2024-01-01T00:00:00",
    "due_date": "2024-01-31T00:00:00",
    "description": "Software services",
    "created_at": "2024-01-01T00:00:00",
    "updated_at": "2024-01-01T00:00:00"
  }
]
```

### Get Invoice by ID
**Endpoint:** `GET /api/invoices/<id>`

**Response:**
```json
{
  "id": 1,
  "invoice_number": "INV-001",
  "company_id": 1,
  "issuer_id": 1,
  "seller_id": 1,
  "amount": 1000.00,
  "currency": "USD",
  "status": "pending",
  "issue_date": "2024-01-01T00:00:00",
  "due_date": "2024-01-31T00:00:00",
  "description": "Software services",
  "created_at": "2024-01-01T00:00:00",
  "updated_at": "2024-01-01T00:00:00"
}
```

### Create Invoice
**Endpoint:** `POST /api/invoices/`

**Request Body:**
```json
{
  "invoice_number": "INV-001",
  "company_id": 1,
  "issuer_id": 1,
  "seller_id": 1,
  "amount": 1000.00,
  "currency": "USD",
  "status": "pending",
  "due_date": "2024-01-31T00:00:00",
  "description": "Software services"
}
```

**Required Fields:**
- `invoice_number`
- `company_id`
- `issuer_id`
- `seller_id`
- `amount`

### Update Invoice
**Endpoint:** `PUT /api/invoices/<id>`

**Request Body:**
```json
{
  "amount": 1200.00,
  "status": "paid",
  "description": "Updated description"
}
```

### Delete Invoice
**Endpoint:** `DELETE /api/invoices/<id>`

**Response:**
```json
{
  "message": "Invoice deleted successfully"
}
```

---

## Company Service API

### Get All Companies
**Endpoint:** `GET /api/companies/`

**Query Parameters:**
- `status` (optional) - Filter by status (active, inactive)
- `country` (optional) - Filter by country

**Response:**
```json
[
  {
    "id": 1,
    "name": "Acme Corporation",
    "tax_id": "123456789",
    "address": "123 Main St",
    "city": "New York",
    "country": "USA",
    "postal_code": "10001",
    "phone": "+1234567890",
    "email": "info@acme.com",
    "website": "https://acme.com",
    "status": "active",
    "created_at": "2024-01-01T00:00:00",
    "updated_at": "2024-01-01T00:00:00"
  }
]
```

### Get Company by ID
**Endpoint:** `GET /api/companies/<id>`

### Create Company
**Endpoint:** `POST /api/companies/`

**Request Body:**
```json
{
  "name": "Acme Corporation",
  "tax_id": "123456789",
  "address": "123 Main St",
  "city": "New York",
  "country": "USA",
  "postal_code": "10001",
  "phone": "+1234567890",
  "email": "info@acme.com",
  "website": "https://acme.com",
  "status": "active"
}
```

**Required Fields:**
- `name`
- `tax_id`

### Update Company
**Endpoint:** `PUT /api/companies/<id>`

### Delete Company
**Endpoint:** `DELETE /api/companies/<id>`

---

## Issuer Service API

### Get All Issuers
**Endpoint:** `GET /api/issuers/`

**Query Parameters:**
- `company_id` (optional) - Filter by company ID
- `status` (optional) - Filter by status (active, inactive)

**Response:**
```json
[
  {
    "id": 1,
    "name": "John Doe",
    "email": "john.doe@acme.com",
    "phone": "+1234567890",
    "department": "Finance",
    "position": "Accountant",
    "company_id": 1,
    "status": "active",
    "created_at": "2024-01-01T00:00:00",
    "updated_at": "2024-01-01T00:00:00"
  }
]
```

### Get Issuer by ID
**Endpoint:** `GET /api/issuers/<id>`

### Create Issuer
**Endpoint:** `POST /api/issuers/`

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john.doe@acme.com",
  "phone": "+1234567890",
  "department": "Finance",
  "position": "Accountant",
  "company_id": 1,
  "status": "active"
}
```

**Required Fields:**
- `name`
- `email`
- `company_id`

### Update Issuer
**Endpoint:** `PUT /api/issuers/<id>`

### Delete Issuer
**Endpoint:** `DELETE /api/issuers/<id>`

---

## Seller Service API

### Get All Sellers
**Endpoint:** `GET /api/sellers/`

**Query Parameters:**
- `company_id` (optional) - Filter by company ID
- `status` (optional) - Filter by status (active, inactive)
- `territory` (optional) - Filter by territory

**Response:**
```json
[
  {
    "id": 1,
    "name": "Jane Smith",
    "email": "jane.smith@acme.com",
    "phone": "+1234567890",
    "company_id": 1,
    "commission_rate": 0.05,
    "territory": "North America",
    "status": "active",
    "created_at": "2024-01-01T00:00:00",
    "updated_at": "2024-01-01T00:00:00"
  }
]
```

### Get Seller by ID
**Endpoint:** `GET /api/sellers/<id>`

### Create Seller
**Endpoint:** `POST /api/sellers/`

**Request Body:**
```json
{
  "name": "Jane Smith",
  "email": "jane.smith@acme.com",
  "phone": "+1234567890",
  "company_id": 1,
  "commission_rate": 0.05,
  "territory": "North America",
  "status": "active"
}
```

**Required Fields:**
- `name`
- `email`
- `company_id`

### Update Seller
**Endpoint:** `PUT /api/sellers/<id>`

### Delete Seller
**Endpoint:** `DELETE /api/sellers/<id>`

---

## Health Check Endpoints

All services provide health check endpoints:

- Invoice Service: `GET /api/invoices/health`
- Company Service: `GET /api/companies/health`
- Issuer Service: `GET /api/issuers/health`
- Seller Service: `GET /api/sellers/health`

**Response:**
```json
{
  "status": "healthy",
  "service": "invoice-service"
}
```

---

## Error Responses

### 400 Bad Request
```json
{
  "error": "Missing required field: invoice_number"
}
```

### 404 Not Found
```json
{
  "error": "404 Not Found: The requested resource was not found"
}
```

### 409 Conflict
```json
{
  "error": "Invoice number already exists"
}
```

---

## Data Models

### Invoice Model
- `id`: Integer (Primary Key)
- `invoice_number`: String (Unique)
- `company_id`: Integer
- `issuer_id`: Integer
- `seller_id`: Integer
- `amount`: Float
- `currency`: String (default: "USD")
- `status`: String (default: "pending")
- `issue_date`: DateTime
- `due_date`: DateTime (optional)
- `description`: Text (optional)
- `created_at`: DateTime
- `updated_at`: DateTime

### Company Model
- `id`: Integer (Primary Key)
- `name`: String
- `tax_id`: String (Unique)
- `address`: String (optional)
- `city`: String (optional)
- `country`: String (optional)
- `postal_code`: String (optional)
- `phone`: String (optional)
- `email`: String (optional)
- `website`: String (optional)
- `status`: String (default: "active")
- `created_at`: DateTime
- `updated_at`: DateTime

### Issuer Model
- `id`: Integer (Primary Key)
- `name`: String
- `email`: String (Unique)
- `phone`: String (optional)
- `department`: String (optional)
- `position`: String (optional)
- `company_id`: Integer
- `status`: String (default: "active")
- `created_at`: DateTime
- `updated_at`: DateTime

### Seller Model
- `id`: Integer (Primary Key)
- `name`: String
- `email`: String (Unique)
- `phone`: String (optional)
- `company_id`: Integer
- `commission_rate`: Float (default: 0.0)
- `territory`: String (optional)
- `status`: String (default: "active")
- `created_at`: DateTime
- `updated_at`: DateTime
