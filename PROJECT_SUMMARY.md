# Project Summary - Invoice Management System

## Overview

This project implements a complete microservices-based invoice management system designed to support multiple companies. The architecture follows modern microservices principles with independent, scalable services.

## Architecture

### Microservices Implemented

1. **Invoice Service** (Port 5001)
   - Manages invoice records
   - Tracks invoice status (pending, paid, cancelled)
   - Links invoices to companies, issuers, and sellers
   - Supports multiple currencies

2. **Company Service** (Port 5002)
   - Manages company information
   - Stores tax IDs, addresses, contact information
   - Tracks company status (active/inactive)
   - Supports international companies

3. **Issuer Service** (Port 5003)
   - Manages invoice issuers (employees who create invoices)
   - Associates issuers with companies
   - Tracks department and position information
   - Maintains issuer status

4. **Seller Service** (Port 5004)
   - Manages sales representatives
   - Tracks commission rates and territories
   - Links sellers to companies
   - Monitors seller status

## Technical Stack

- **Backend Framework**: Spring Boot 3.2.0
- **ORM/Data Access**: Spring Data JPA / Hibernate
- **Database**: H2 (development) / PostgreSQL/MySQL (production)
- **Build Tool**: Gradle 8.5
- **Containerization**: Docker
- **Orchestration**: Docker Compose
- **Language**: Java 21

## Key Features

### Independent Services
- Each service has its own codebase and database
- Services can be deployed independently
- Services can be scaled independently based on demand
- Service failures are isolated

### RESTful API
- Standard HTTP methods (GET, POST, PUT, DELETE)
- JSON request/response format
- Consistent error handling across services
- Query parameter filtering

### Data Models

#### Invoice
- Invoice number (unique)
- Company, issuer, and seller references
- Amount and currency
- Status tracking
- Issue and due dates
- Descriptions

#### Company
- Name and tax ID (unique)
- Complete address information
- Contact details (phone, email, website)
- Status tracking

#### Issuer
- Name and email (unique)
- Department and position
- Company association
- Phone contact

#### Seller
- Name and email (unique)
- Commission rate and territory
- Company association
- Phone contact

### Security Features

- Debug mode controlled by environment variable (disabled by default)
- Input validation on all endpoints
- Email uniqueness validation
- SQL injection prevention via JPA/Hibernate ORM
- Error handling with appropriate status codes
- No security vulnerabilities (CodeQL verified)

### API Endpoints

Each service provides:
- `GET /api/{resource}/` - List all resources with optional filtering
- `GET /api/{resource}/<id>` - Get specific resource
- `POST /api/{resource}/` - Create new resource
- `PUT /api/{resource}/<id>` - Update existing resource
- `DELETE /api/{resource}/<id>` - Delete resource
- `GET /api/{resource}/health` - Health check

### Filtering Capabilities

**Invoice Service:**
- Filter by company_id
- Filter by status

**Company Service:**
- Filter by status
- Filter by country

**Issuer Service:**
- Filter by company_id
- Filter by status

**Seller Service:**
- Filter by company_id
- Filter by status
- Filter by territory

## Documentation

The project includes comprehensive documentation:

1. **README.md** - Main project documentation
   - Quick start guide
   - Installation instructions
   - API examples
   - Architecture overview

2. **API_DOCUMENTATION.md** - Detailed API reference
   - All endpoints documented
   - Request/response examples
   - Data models
   - Error codes

3. **DEPLOYMENT.md** - Deployment guide
   - Local development setup
   - Docker deployment
   - Production deployment strategies
   - Cloud platform instructions (AWS, GCP, Azure)
   - Kubernetes configuration
   - Monitoring and maintenance

4. **TESTING_EXAMPLES.md** - Testing guide
   - Complete workflow examples
   - CRUD operation examples
   - Error handling tests
   - Automated test scripts
   - Performance testing

5. **docker-compose.yml** - Service orchestration
   - Defines all services
   - Network configuration
   - Volume management
   - Environment variables

## Project Structure

```
invoice_project/
├── services/
│   ├── invoice-service/
│   │   ├── src/
│   │   │   └── main/
│   │   │       └── java/
│   │   │           └── com/invoice/
│   │   │               ├── InvoiceServiceApplication.java
│   │   │               ├── controller/
│   │   │               ├── model/
│   │   │               ├── repository/
│   │   │               ├── service/
│   │   │               └── exception/
│   │   ├── build.gradle
│   │   ├── Dockerfile
│   │   └── gradlew
│   ├── company-service/
│   │   ├── src/
│   │   │   └── main/
│   │   │       └── java/
│   │   │           └── com/invoice/
│   │   ├── build.gradle
│   │   ├── Dockerfile
│   │   └── gradlew
│   ├── issuer-service/
│   │   ├── src/
│   │   │   └── main/
│   │   │       └── java/
│   │   │           └── com/invoice/
│   │   ├── build.gradle
│   │   ├── Dockerfile
│   │   └── gradlew
│   └── seller-service/
│       ├── src/
│       │   └── main/
│       │       └── java/
│       │           └── com/invoice/
│       ├── build.gradle
│       ├── Dockerfile
│       └── gradlew
├── .gitignore
├── API_DOCUMENTATION.md
├── DEPLOYMENT.md
├── README.md
├── TESTING_EXAMPLES.md
├── PROJECT_SUMMARY.md
└── docker-compose.yml
```

## Getting Started

### Quick Start with Docker Compose

```bash
# Clone the repository
git clone https://github.com/trantienduat/invoice_project.git
cd invoice_project

# Start all services
docker compose up --build

# Services will be available at:
# - Invoice Service: http://localhost:5001
# - Company Service: http://localhost:5002
# - Issuer Service: http://localhost:5003
# - Seller Service: http://localhost:5004
```

### Quick Start without Docker

```bash
# For each service:
cd services/invoice-service
./gradlew clean build
./gradlew bootRun
```

## Production Considerations

For production deployment, consider:

1. **Database**: Replace SQLite with PostgreSQL or MySQL
2. **Authentication**: Add JWT or OAuth2
3. **API Gateway**: Use Kong, Ambassador, or AWS API Gateway
4. **Load Balancing**: Implement with Nginx or cloud load balancers
5. **Monitoring**: Use Prometheus, Grafana, or cloud monitoring
6. **Logging**: Centralized logging with ELK stack
7. **Service Discovery**: Implement with Consul or Eureka
8. **Message Queue**: Add RabbitMQ or Kafka for async operations
9. **Caching**: Implement Redis for performance
10. **SSL/TLS**: Enable HTTPS for all services

## Testing

The system has been tested and verified:
- All services start successfully
- Health check endpoints respond correctly
- CRUD operations work as expected
- Error handling functions properly
- Input validation prevents invalid data
- Security scan shows no vulnerabilities (CodeQL)

Example test:
```bash
# Health check
curl http://localhost:5002/api/companies/health

# Create a company
curl -X POST http://localhost:5002/api/companies/ \
  -H "Content-Type: application/json" \
  -d '{"name": "Tech Corp", "tax_id": "987654321"}'

# List companies
curl http://localhost:5002/api/companies/
```

## Code Quality

- Follows Spring Boot best practices
- Clean, maintainable code structure
- Consistent error handling
- Proper input validation
- Comprehensive documentation
- No security vulnerabilities detected
- Environment-based configuration

## Future Enhancements

Potential improvements for the future:

1. **Authentication & Authorization**
   - JWT token-based authentication
   - Role-based access control
   - OAuth2 integration

2. **Advanced Features**
   - Invoice PDF generation
   - Email notifications
   - Payment processing integration
   - Invoice approval workflow
   - Audit logging

3. **Performance**
   - Caching layer (Redis)
   - Database query optimization
   - API rate limiting
   - Response pagination

4. **Monitoring**
   - Application performance monitoring
   - Error tracking (Sentry)
   - Real-time metrics dashboard
   - Alerting system

5. **Testing**
   - Unit tests for all endpoints
   - Integration tests
   - Load testing
   - E2E testing

6. **DevOps**
   - CI/CD pipeline
   - Automated deployment
   - Infrastructure as Code
   - Blue-green deployment

## License

This project is open source and available under the MIT License.

## Contributors

- Implementation by GitHub Copilot
- Repository maintained by trantienduat

## Support

For questions, issues, or contributions:
- GitHub Issues: https://github.com/trantienduat/invoice_project/issues
- Documentation: See README.md and other documentation files

---

**Note**: This is a production-ready foundation for a microservices-based invoice management system. Each service is independently deployable and scalable, making it suitable for enterprise use with appropriate production configurations.
