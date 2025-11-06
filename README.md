# Invoice Management System - Microservices Architecture

A microservices-based invoice management system designed to support multiple companies. The system provides independent services for managing invoices, companies, invoice issuers, and sellers, allowing flexible scaling and separation of concerns.

## Architecture

This project follows a microservices architecture with the following independent services:

- **Invoice Service** (Port 5001): Manages invoices
- **Company Service** (Port 5002): Manages company information
- **Issuer Service** (Port 5003): Manages invoice issuers
- **Seller Service** (Port 5004): Manages sellers

Each microservice:
- Runs independently
- Has its own data storage
- Communicates via REST API
- Can be deployed and scaled independently

## Project Structure

```
invoice_project/
├── services/
│   ├── invoice-service/
│   │   ├── app/
│   │   │   ├── __init__.py
│   │   │   ├── models.py
│   │   │   └── routes.py
│   │   ├── Dockerfile
│   │   ├── requirements.txt
│   │   └── run.py
│   ├── company-service/
│   │   ├── app/
│   │   │   ├── __init__.py
│   │   │   ├── models.py
│   │   │   └── routes.py
│   │   ├── Dockerfile
│   │   ├── requirements.txt
│   │   └── run.py
│   ├── issuer-service/
│   │   ├── app/
│   │   │   ├── __init__.py
│   │   │   ├── models.py
│   │   │   └── routes.py
│   │   ├── Dockerfile
│   │   ├── requirements.txt
│   │   └── run.py
│   └── seller-service/
│       ├── app/
│       │   ├── __init__.py
│       │   ├── models.py
│       │   └── routes.py
│       ├── Dockerfile
│       ├── requirements.txt
│       └── run.py
├── docker-compose.yml
└── README.md
```

## Prerequisites

- Docker and Docker Compose
- Java 21 (for local development)
- Maven 3.8+ (for local development)

## Quick Start with Docker Compose

1. **Clone the repository**
```bash
git clone https://github.com/trantienduat/invoice_project.git
cd invoice_project
```

2. **Start all services**
```bash
docker-compose up --build
```

3. **Access the services**
- Invoice Service: http://localhost:5001
- Company Service: http://localhost:5002
- Issuer Service: http://localhost:5003
- Seller Service: http://localhost:5004

## Local Development (without Docker)

### Setup for each service

1. **Navigate to a service directory**
```bash
cd services/invoice-service
```

2. **Build the service**
```bash
mvn clean package
```

3. **Run the service**
```bash
java -jar target/invoice-service-1.0.0.jar
```

Or use Maven Spring Boot plugin:
```bash
mvn spring-boot:run
```

Repeat for each service using their respective ports.

## API Documentation

### Invoice Service (Port 5001)

#### Endpoints:
- `GET /api/invoices/` - Get all invoices
- `GET /api/invoices/<id>` - Get specific invoice
- `POST /api/invoices/` - Create new invoice
- `PUT /api/invoices/<id>` - Update invoice
- `DELETE /api/invoices/<id>` - Delete invoice
- `GET /api/invoices/health` - Health check

#### Create Invoice Example:
```bash
curl -X POST http://localhost:5001/api/invoices/ \
  -H "Content-Type: application/json" \
  -d '{
    "invoice_number": "INV-001",
    "company_id": 1,
    "issuer_id": 1,
    "seller_id": 1,
    "amount": 1000.00,
    "currency": "USD",
    "status": "pending",
    "description": "Software services"
  }'
```

### Company Service (Port 5002)

#### Endpoints:
- `GET /api/companies/` - Get all companies
- `GET /api/companies/<id>` - Get specific company
- `POST /api/companies/` - Create new company
- `PUT /api/companies/<id>` - Update company
- `DELETE /api/companies/<id>` - Delete company
- `GET /api/companies/health` - Health check

#### Create Company Example:
```bash
curl -X POST http://localhost:5002/api/companies/ \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Acme Corporation",
    "tax_id": "123456789",
    "address": "123 Main St",
    "city": "New York",
    "country": "USA",
    "email": "info@acme.com",
    "phone": "+1234567890"
  }'
```

### Issuer Service (Port 5003)

#### Endpoints:
- `GET /api/issuers/` - Get all issuers
- `GET /api/issuers/<id>` - Get specific issuer
- `POST /api/issuers/` - Create new issuer
- `PUT /api/issuers/<id>` - Update issuer
- `DELETE /api/issuers/<id>` - Delete issuer
- `GET /api/issuers/health` - Health check

#### Create Issuer Example:
```bash
curl -X POST http://localhost:5003/api/issuers/ \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@acme.com",
    "company_id": 1,
    "department": "Finance",
    "position": "Accountant"
  }'
```

### Seller Service (Port 5004)

#### Endpoints:
- `GET /api/sellers/` - Get all sellers
- `GET /api/sellers/<id>` - Get specific seller
- `POST /api/sellers/` - Create new seller
- `PUT /api/sellers/<id>` - Update seller
- `DELETE /api/sellers/<id>` - Delete seller
- `GET /api/sellers/health` - Health check

#### Create Seller Example:
```bash
curl -X POST http://localhost:5004/api/sellers/ \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jane Smith",
    "email": "jane.smith@acme.com",
    "company_id": 1,
    "commission_rate": 0.05,
    "territory": "North America"
  }'
```

## Testing the System

### Health Check All Services:
```bash
curl http://localhost:5001/api/invoices/health
curl http://localhost:5002/api/companies/health
curl http://localhost:5003/api/issuers/health
curl http://localhost:5004/api/sellers/health
```

### Complete Workflow Example:

1. **Create a Company:**
```bash
curl -X POST http://localhost:5002/api/companies/ \
  -H "Content-Type: application/json" \
  -d '{"name": "Tech Corp", "tax_id": "987654321"}'
```

2. **Create an Issuer:**
```bash
curl -X POST http://localhost:5003/api/issuers/ \
  -H "Content-Type: application/json" \
  -d '{"name": "Alice Brown", "email": "alice@techcorp.com", "company_id": 1}'
```

3. **Create a Seller:**
```bash
curl -X POST http://localhost:5004/api/sellers/ \
  -H "Content-Type: application/json" \
  -d '{"name": "Bob Wilson", "email": "bob@techcorp.com", "company_id": 1}'
```

4. **Create an Invoice:**
```bash
curl -X POST http://localhost:5001/api/invoices/ \
  -H "Content-Type: application/json" \
  -d '{"invoice_number": "INV-2024-001", "company_id": 1, "issuer_id": 1, "seller_id": 1, "amount": 5000.00}'
```

## Features

- **Independent Services**: Each microservice can be deployed, scaled, and maintained independently
- **RESTful API**: Standard REST endpoints for all operations
- **Data Isolation**: Each service has its own database
- **Docker Support**: Easy deployment with Docker and Docker Compose
- **Flexible Scaling**: Scale individual services based on demand
- **Health Monitoring**: Health check endpoints for service monitoring
- **Error Handling**: Comprehensive error responses
- **Data Validation**: Input validation for all endpoints

## Technology Stack

- **Language**: Java 21 (LTS)
- **Backend Framework**: Spring Boot 3.2.0
- **Database ORM**: Spring Data JPA / Hibernate
- **Database**: H2 (development) - can be replaced with PostgreSQL/MySQL for production
- **Build Tool**: Maven
- **Containerization**: Docker
- **Orchestration**: Docker Compose

## Production Considerations

For production deployment, consider:

1. **Database**: Replace SQLite with PostgreSQL or MySQL
2. **Authentication**: Add JWT or OAuth2 authentication
3. **API Gateway**: Use Kong, Ambassador, or AWS API Gateway
4. **Service Discovery**: Implement Consul or Eureka
5. **Message Queue**: Add RabbitMQ or Kafka for async communication
6. **Monitoring**: Implement Prometheus and Grafana
7. **Logging**: Use ELK stack (Elasticsearch, Logstash, Kibana)
8. **Load Balancing**: Use Nginx or HAProxy
9. **HTTPS**: Configure SSL/TLS certificates
10. **Secrets Management**: Use Vault or AWS Secrets Manager

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is open source and available under the MIT License.