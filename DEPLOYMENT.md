# Deployment Guide

This guide provides instructions for deploying the Invoice Management System microservices in various environments.

## Table of Contents
1. [Local Development](#local-development)
2. [Docker Deployment](#docker-deployment)
3. [Production Deployment](#production-deployment)
4. [Monitoring and Maintenance](#monitoring-and-maintenance)

---

## Local Development

### Prerequisites
- Java 21
- Gradle 8.5+ (included via wrapper)
- Docker (optional, for containerized deployment)

### Setup Each Service Individually

For each service (invoice-service, company-service, issuer-service, seller-service):

```bash
# Navigate to service directory
cd services/invoice-service

# Build the service
./gradlew clean build

# Run the service
./gradlew bootRun

# Or run the JAR directly
java -jar build/libs/invoice-service-1.0.0.jar
```

### Service Ports
- Invoice Service: 5001
- Company Service: 5002
- Issuer Service: 5003
- Seller Service: 5004

---

## Docker Deployment

### Prerequisites
- Docker 20.10+
- Docker Compose 1.29+

### Using Docker Compose (Recommended)

#### Start All Services
```bash
# Build and start all services
docker-compose up --build

# Run in detached mode (background)
docker-compose up -d --build
```

#### Stop All Services
```bash
docker-compose down
```

#### View Logs
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f invoice-service
```

#### Restart a Service
```bash
docker-compose restart invoice-service
```

### Individual Service Deployment

Build and run a single service:

```bash
# Build the image
cd services/invoice-service
docker build -t invoice-service:latest .

# Run the container
docker run -d \
  --name invoice-service \
  -p 5001:5001 \
  -e PORT=5001 \
  -e DATABASE_URL=sqlite:///invoice.db \
  invoice-service:latest
```

---

## Production Deployment

### Cloud Platform Options

#### AWS Deployment

**Using AWS ECS (Elastic Container Service):**

1. **Push Images to ECR:**
```bash
# Authenticate Docker to ECR
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin <account-id>.dkr.ecr.us-east-1.amazonaws.com

# Tag and push each service
docker tag invoice-service:latest <account-id>.dkr.ecr.us-east-1.amazonaws.com/invoice-service:latest
docker push <account-id>.dkr.ecr.us-east-1.amazonaws.com/invoice-service:latest
```

2. **Create ECS Task Definitions** for each service

3. **Deploy with AWS ECS Service** with load balancer

**Using AWS Lambda + API Gateway:**
- Use Zappa or Serverless Framework
- Package each Flask app as a Lambda function

#### Google Cloud Platform

**Using Google Cloud Run:**

```bash
# Build and deploy
gcloud builds submit --tag gcr.io/<project-id>/invoice-service
gcloud run deploy invoice-service \
  --image gcr.io/<project-id>/invoice-service \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated
```

#### Azure

**Using Azure Container Instances:**

```bash
az container create \
  --resource-group myResourceGroup \
  --name invoice-service \
  --image <registry>/invoice-service:latest \
  --ports 5001 \
  --environment-variables PORT=5001
```

#### Kubernetes Deployment

Create deployment files for each service:

**invoice-service-deployment.yaml:**
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: invoice-service
spec:
  replicas: 3
  selector:
    matchLabels:
      app: invoice-service
  template:
    metadata:
      labels:
        app: invoice-service
    spec:
      containers:
      - name: invoice-service
        image: invoice-service:latest
        ports:
        - containerPort: 5001
        env:
        - name: PORT
          value: "5001"
        - name: DATABASE_URL
          valueFrom:
            secretKeyRef:
              name: db-credentials
              key: invoice-db-url
---
apiVersion: v1
kind: Service
metadata:
  name: invoice-service
spec:
  selector:
    app: invoice-service
  ports:
  - port: 80
    targetPort: 5001
  type: LoadBalancer
```

Deploy to Kubernetes:
```bash
kubectl apply -f invoice-service-deployment.yaml
kubectl apply -f company-service-deployment.yaml
kubectl apply -f issuer-service-deployment.yaml
kubectl apply -f seller-service-deployment.yaml
```

### Production Database Setup

Replace SQLite with PostgreSQL or MySQL:

**Docker Compose with PostgreSQL:**

```yaml
services:
  postgres:
    image: postgres:15
    environment:
      POSTGRES_USER: invoiceuser
      POSTGRES_PASSWORD: invoicepass
      POSTGRES_DB: invoice_db
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - invoice-network

  invoice-service:
    build: ./services/invoice-service
    environment:
      - DATABASE_URL=postgresql://invoiceuser:invoicepass@postgres:5432/invoice_db
    depends_on:
      - postgres

volumes:
  postgres_data:
```

### Environment Variables

Create `.env` file for production:

```env
# Invoice Service
INVOICE_DB_URL=postgresql://user:pass@host:5432/invoice_db
INVOICE_PORT=5001

# Company Service
COMPANY_DB_URL=postgresql://user:pass@host:5432/company_db
COMPANY_PORT=5002

# Issuer Service
ISSUER_DB_URL=postgresql://user:pass@host:5432/issuer_db
ISSUER_PORT=5003

# Seller Service
SELLER_DB_URL=postgresql://user:pass@host:5432/seller_db
SELLER_PORT=5004

# Security
SECRET_KEY=your-secret-key-here
JWT_SECRET=your-jwt-secret-here

# API Gateway (if used)
API_GATEWAY_URL=https://api.yourcompany.com
```

### API Gateway Setup

Use NGINX as a reverse proxy:

**nginx.conf:**
```nginx
upstream invoice_service {
    server invoice-service:5001;
}

upstream company_service {
    server company-service:5002;
}

upstream issuer_service {
    server issuer-service:5003;
}

upstream seller_service {
    server seller-service:5004;
}

server {
    listen 80;
    server_name api.yourcompany.com;

    location /api/invoices {
        proxy_pass http://invoice_service;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }

    location /api/companies {
        proxy_pass http://company_service;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }

    location /api/issuers {
        proxy_pass http://issuer_service;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }

    location /api/sellers {
        proxy_pass http://seller_service;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

---

## Monitoring and Maintenance

### Health Checks

Monitor service health:

```bash
# Create a health check script
cat > health_check.sh << 'EOF'
#!/bin/bash
services=("5001:invoice" "5002:company" "5003:issuer" "5004:seller")

for service in "${services[@]}"; do
    IFS=':' read -r port name <<< "$service"
    response=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:$port/api/${name}s/health)
    if [ $response -eq 200 ]; then
        echo "✓ ${name}-service is healthy"
    else
        echo "✗ ${name}-service is down (HTTP $response)"
    fi
done
EOF

chmod +x health_check.sh
./health_check.sh
```

### Logging

Collect logs from all services:

```bash
# Docker Compose
docker-compose logs -f --tail=100

# Docker
docker logs -f invoice-service

# Kubernetes
kubectl logs -f deployment/invoice-service
```

### Backup Database

```bash
# SQLite backup
cp services/invoice-service/invoice.db backups/invoice-$(date +%Y%m%d).db

# PostgreSQL backup
pg_dump -U invoiceuser invoice_db > backup-$(date +%Y%m%d).sql
```

### Scaling Services

**Docker Compose:**
```bash
docker-compose up -d --scale invoice-service=3
```

**Kubernetes:**
```bash
kubectl scale deployment invoice-service --replicas=5
```

### Update Deployment

```bash
# Build new images
docker-compose build

# Rolling update
docker-compose up -d --no-deps --build invoice-service

# Kubernetes rolling update
kubectl set image deployment/invoice-service invoice-service=invoice-service:v2
```

---

## Security Considerations

### Production Checklist

- [ ] Change all default passwords
- [ ] Use environment variables for sensitive data
- [ ] Enable HTTPS/TLS
- [ ] Implement authentication (JWT, OAuth2)
- [ ] Add rate limiting
- [ ] Enable CORS with specific origins
- [ ] Use secrets management (AWS Secrets Manager, HashiCorp Vault)
- [ ] Implement API key authentication
- [ ] Add input validation and sanitization
- [ ] Enable audit logging
- [ ] Regular security updates
- [ ] Database connection encryption

### SSL/TLS Configuration

Use Let's Encrypt with Certbot:

```bash
# Install certbot
sudo apt-get install certbot python3-certbot-nginx

# Get certificate
sudo certbot --nginx -d api.yourcompany.com

# Auto-renewal
sudo certbot renew --dry-run
```

---

## Troubleshooting

### Service Won't Start

```bash
# Check logs
docker-compose logs invoice-service

# Check if port is in use
lsof -i :5001

# Rebuild without cache
docker-compose build --no-cache invoice-service
```

### Database Connection Issues

```bash
# Test database connection
python -c "from sqlalchemy import create_engine; engine = create_engine('your-db-url'); print(engine.connect())"
```

### Performance Issues

```bash
# Check resource usage
docker stats

# Monitor service response time
ab -n 1000 -c 10 http://localhost:5001/api/invoices/health
```

---

## Support

For issues and questions:
- GitHub Issues: https://github.com/trantienduat/invoice_project/issues
- Documentation: See README.md and API_DOCUMENTATION.md
