# Portfolio Manager - Deployment Guide

## Table of Contents

- [Quick Start](#quick-start)
- [Prerequisites](#prerequisites)
- [Docker Architecture](#docker-architecture)
- [Building Locally](#building-locally)
- [Running with Docker Compose](#running-with-docker-compose)
- [Jenkins Pipeline](#jenkins-pipeline)
- [Accessing the Application](#accessing-the-application)
- [Stopping & Cleanup](#stopping--cleanup)
- [Troubleshooting](#troubleshooting)
- [Production Deployment](#production-deployment)

---

## Quick Start

```bash
# Clone the repository
cd /path/to/Portfolio/Manager

# Build and start the entire stack
docker-compose up -d

# Verify services are running
docker-compose ps

# Access the application
# Frontend: http://localhost:8082
# Backend API: http://localhost:8081
```

---

## Prerequisites

### System Requirements

- **OS**: Linux, macOS, or Windows (with WSL2 or Docker Desktop)
- **Disk Space**: At least 5GB free
- **Memory**: At least 4GB RAM recommended (6GB+ for production)

### Required Software

1. **Docker** v20.10+
   ```bash
   # Verify installation
   docker --version
   ```

2. **Docker Compose** v1.29+
   ```bash
   # Verify installation
   docker-compose --version
   ```

3. **Git** (for cloning repository)
   ```bash
   git --version
   ```

### For Local Development (Without Docker)

- **Java 17+** (Eclipse Temurin)
- **Maven 3.9+**
- **Node.js LTS**
- **MySQL 8.0**

---

## Docker Architecture

### System Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                      Host Machine (10.9.64.59)                   │
│                                                                  │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │           Docker Network: portfolionet (Bridge)            │  │
│  │                                                            │  │
│  │  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐   │  │
│  │  │    MySQL     │  │  Backend     │  │  Frontend    │   │  │
│  │  │   (3306)     │  │  (8080)      │  │  Nginx (80)  │   │  │
│  │  │              │  │              │  │              │   │  │
│  │  │ Container    │  │ Container    │  │ Container    │   │  │
│  │  └──────────────┘  └──────────────┘  └──────────────┘   │  │
│  │         ▲                   ▲                    ▲        │  │
│  │         │                   │                    │        │  │
│  │         └───────────────────┴────────────────────┘        │  │
│  │           Docker Service DNS Resolution                    │  │
│  │                                                            │  │
│  └───────────────────────────────────────────────────────────┘  │
│                         ▲         ▲          ▲                  │
│                         │         │          │                  │
│      Port Mapping:   3306:3306  8081:8080   8082:80             │
│                  (not exposed)  (API)      (UI)                 │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### Services Overview

| Service | Image | Port (Host) | Port (Container) | Purpose |
|---------|-------|-------------|-----------------|---------|
| **MySQL** | `mysql:8.0` | 3306* | 3306 | Database storage |
| **Backend** | `portfolio-backend:latest` | 8081 | 8080 | Spring Boot REST API |
| **Frontend** | `portfolio-frontend:latest` | 8082 | 80 | React/Vite SPA + Nginx |

*MySQL port not exposed to host for security; only accessible via Docker network

### Networking

- **Network Type**: Bridge network (`portfolionet`)
- **Service Discovery**: Uses Docker DNS (service name resolution)
  - Frontend → Backend: `http://backend:8080`
  - Backend → MySQL: `mysql:3306`
  - External access: `http://10.9.64.59:PORT`

### Data Persistence

- **MySQL Data**: Named volume `mysql-data`
  - Location: `/var/lib/docker/volumes/mysql-data/_data`
  - Persists across container restarts and removal
  - Survives `docker-compose down` (but not `docker-compose down -v`)

---

## Building Locally

### Prerequisites

- Java 17+ (Eclipse Temurin JDK)
- Maven 3.9+
- Node.js LTS
- MySQL 8.0 running locally on port 3306

### Build Steps

#### 1. Build Backend

```bash
cd Backend

# Build with Maven
mvn clean package

# Or use Maven wrapper (if available)
./mvnw clean package

# Output: Backend/target/portfolioManager-0.0.1-SNAPSHOT.jar
```

#### 2. Build Frontend

```bash
cd frontend

# Install dependencies
npm ci

# Build production bundle
npm run build

# Output: frontend/dist/ (static files ready for serving)
```

### Running Locally (Without Docker)

#### Start MySQL

```bash
# macOS (Homebrew)
brew services start mysql

# Linux (systemd)
sudo systemctl start mysql

# Or use Docker
docker run -d \
  -e MYSQL_ROOT_PASSWORD=n3u3da! \
  -e MYSQL_DATABASE=portfolio_manager \
  -p 3306:3306 \
  mysql:8.0
```

#### Start Backend

```bash
cd Backend

# Run JAR directly
java -jar target/portfolioManager-0.0.1-SNAPSHOT.jar

# Or run via Maven
mvn spring-boot:run

# Or run via Maven Wrapper
./mvnw spring-boot:run
```

Backend will start on `http://localhost:8081`

#### Start Frontend (Development)

```bash
cd frontend

# Start Vite dev server
npm run dev

# Frontend will start on http://localhost:5173
```

Or serve production build with Nginx:

```bash
# Copy dist/ to Nginx
cp -r frontend/dist /usr/local/var/www

# Start Nginx
nginx

# Access on http://localhost
```

---

## Running with Docker Compose

### Starting the Stack

```bash
# Navigate to project root
cd /path/to/Portfolio/Manager

# Build and start services (detached mode)
docker-compose up -d

# Follow logs in real-time (optional)
docker-compose logs -f

# View service status
docker-compose ps
```

**Expected Output:**
```
NAME            COMMAND                  SERVICE      STATUS      PORTS
portfolio-mysql        "docker-entrypoint.s…"   mysql       Up 30s      3306/tcp
portfolio-backend      "java -jar app.jar"      backend     Up 25s      0.0.0.0:8081->8080/tcp
portfolio-frontend     "nginx -g 'daemon of…"   frontend    Up 20s      0.0.0.0:8082->80/tcp
```

All services should be in "Up" state with ✓ health checks passing.

### Rebuilding Images

```bash
# Rebuild images if source code changed
docker-compose build

# Rebuild and restart services
docker-compose up -d --build
```

### Viewing Logs

```bash
# View all service logs
docker-compose logs

# Follow logs in real-time
docker-compose logs -f

# View logs for specific service
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f mysql

# Show last 100 lines
docker-compose logs --tail=100
```

---

## Jenkins Pipeline

### Setup

1. **Install Jenkins** on Linux server (if not already installed)
   ```bash
   # Jenkins runs on http://10.9.64.59:8080
   ```

2. **Configure Git Integration**
   - Jenkins → Manage Jenkins → Configure System
   - Add Git repository credentials

3. **Create Pipeline Job**
   - New Item → Pipeline
   - Name: `Portfolio-Manager-Deployment`
   - Pipeline → Definition: `Pipeline script from SCM`
   - SCM: Git
   - Repository URL: Your Git repository
   - Branch: `feature/deployment` (initially)

### Pipeline Stages

The Jenkinsfile includes 9 automated stages:

1. **Checkout** - Clone repository from Git
2. **Build Backend** - Maven compile and package
3. **Run Backend Tests** - Execute unit tests
4. **Build Frontend** - Install dependencies and build bundle
5. **Build Docker Images** - Create Docker images
6. **Stop Existing Containers** - Gracefully stop current deployment
7. **Deploy Using Docker Compose** - Start new containers
8. **Health Check** - Verify all services are healthy
9. **Cleanup** - Remove build artifacts

### Triggering Pipeline

**Option 1: Push to Branch**
```bash
# Commit to feature/deployment branch
git checkout -b feature/deployment
git commit -m "Deploy changes"
git push origin feature/deployment

# Jenkins will automatically trigger pipeline
```

**Option 2: Trigger Manually**
- Go to Jenkins UI
- Select `Portfolio-Manager-Deployment` job
- Click "Build Now"

### Monitoring Pipeline

```bash
# View build logs in Jenkins UI
# Jenkins → Portfolio-Manager-Deployment → Build #N → Console Output

# View running containers
docker ps

# View application logs
docker-compose logs -f
```

### Changing Target Branch

To change from `feature/deployment` to another branch (e.g., `dev` or `main`):

1. Edit `Jenkinsfile` in the repository
2. Update `branch` in SCM configuration:
   ```groovy
   // In Jenkinsfile (if needed)
   when {
       branch 'dev'  // Change from 'feature/deployment'
   }
   ```
3. Commit and push changes

---

## Accessing the Application

### External Access (From Outside Docker)

Once deployed on Linux server at `10.9.64.59`:

- **Frontend (UI)**: `http://10.9.64.59:8082`
- **Backend API**: `http://10.9.64.59:8081`
- **Jenkins CI/CD**: `http://10.9.64.59:8080`

### Local Access (Docker Desktop)

When running Docker Compose locally:

- **Frontend (UI)**: `http://localhost:8082`
- **Backend API**: `http://localhost:8081`
- **MySQL**: `localhost:3306` (not exposed to host)

### API Endpoints

Backend REST API endpoints:

```bash
# Get investment options
curl http://10.9.64.59:8081/investment-options/

# Get portfolio summary
curl http://10.9.64.59:8081/api/portfolio/summary

# Execute trade (buy)
curl -X PUT http://10.9.64.59:8081/api/v1/buy \
  -H "Content-Type: application/json" \
  -d '{"optionId": 1, "quantity": 10, "currentPrice": 100.0, "action": "buy"}'
```

### API Documentation

If Swagger/OpenAPI is enabled in backend:

```bash
# Swagger UI
http://10.9.64.59:8081/swagger-ui.html

# OpenAPI JSON
http://10.9.64.59:8081/v3/api-docs
```

---

## Stopping & Cleanup

### Stop Services (Preserve Data)

```bash
# Stop all containers but keep volumes
docker-compose down

# Verify containers are stopped
docker-compose ps
```

Data in MySQL named volume (`mysql-data`) is preserved.

### Restart Services

```bash
# Start services again (uses same data)
docker-compose up -d
```

### Stop and Remove Everything

```bash
# Stop containers and remove volumes (WARNING: Deletes database data!)
docker-compose down -v

# This removes:
# - All containers
# - All networks
# - mysql-data volume (DATABASE IS LOST)
```

### Remove Docker Images

```bash
# Remove specific images
docker rmi portfolio-backend:latest portfolio-frontend:latest

# Remove all dangling images
docker image prune -a
```

### Clean Docker System

```bash
# Remove unused containers, networks, images, and build cache
docker system prune -a

# Remove unused volumes
docker volume prune
```

---

## Troubleshooting

### Issue: Containers Won't Start

**Symptom**: `docker-compose ps` shows containers in "Exited" state

**Solutions**:

1. Check container logs:
   ```bash
   docker-compose logs mysql
   docker-compose logs backend
   docker-compose logs frontend
   ```

2. Check disk space:
   ```bash
   df -h
   ```

3. Check Docker daemon:
   ```bash
   docker ps  # Should work without errors
   ```

4. Restart Docker:
   ```bash
   sudo systemctl restart docker
   ```

### Issue: Backend Can't Connect to MySQL

**Symptom**: Backend logs show "Connection refused" or "Cannot connect to mysql:3306"

**Cause**: MySQL container not healthy yet

**Solutions**:

1. Check MySQL status:
   ```bash
   docker-compose ps mysql
   ```

2. Wait for health check to pass (30-60 seconds):
   ```bash
   docker-compose logs -f mysql
   ```

3. Restart MySQL:
   ```bash
   docker-compose restart mysql
   ```

4. Verify network connectivity:
   ```bash
   docker-compose exec backend ping mysql
   ```

### Issue: Frontend Shows "Cannot Reach Backend"

**Symptom**: Frontend displays API error messages

**Causes**:
- Backend service not healthy
- API endpoint misconfigured

**Solutions**:

1. Check backend health:
   ```bash
   curl http://localhost:8081/actuator/health
   ```

2. Check frontend API configuration:
   ```bash
   # Verify .env.production has correct API_BASE_URL
   cat frontend/.env.production
   ```

3. Rebuild frontend with new configuration:
   ```bash
   docker-compose build frontend
   docker-compose up -d frontend
   ```

4. Clear browser cache:
   - Press Ctrl+Shift+Delete
   - Clear cached images and files
   - Refresh page

### Issue: Port Already in Use

**Symptom**: Error "bind: address already in use"

**Solution**: Change port in docker-compose.yml

```yaml
ports:
  - "8083:8080"  # Change 8081 to 8083 if 8081 is in use
```

Then restart:
```bash
docker-compose up -d
```

### Issue: Out of Disk Space

**Symptom**: Build fails with "no space left on device"

**Solutions**:

1. Clean up Docker:
   ```bash
   docker system prune -a
   docker volume prune
   ```

2. Remove old images:
   ```bash
   docker image ls
   docker rmi <image-id>
   ```

3. Check disk usage:
   ```bash
   du -sh /var/lib/docker
   ```

### Issue: Jenkins Pipeline Fails

**Symptom**: Build fails at a specific stage

**Solutions**:

1. View Jenkins console output:
   - Jenkins UI → Build → Console Output

2. Check logs:
   ```bash
   docker-compose logs -f
   ```

3. Re-run specific stage:
   - Jenkins UI → Replay Build

4. Debug locally:
   ```bash
   # Build backend locally
   cd Backend
   mvn clean package

   # Build frontend locally
   cd frontend
   npm ci && npm run build
   ```

### Issue: Database Data Lost

**Symptom**: Data disappeared after `docker-compose down -v`

**Solution**: Restore from backup or reinitialize

```bash
# The -v flag removes volumes (data is lost)
# Use: docker-compose down (without -v) to preserve data

# If data is lost, reinitialize:
docker-compose up -d
# schema.sql and data.sql will run automatically
```

### Issue: Memory/CPU Limits Exceeded

**Symptom**: Services getting OOM killed or slow performance

**Solution**: Increase resource limits in docker-compose.yml

```yaml
backend:
  deploy:
    resources:
      limits:
        cpus: '1'
        memory: 1G
      reservations:
        cpus: '0.5'
        memory: 512M
```

---

## Production Deployment

### Pre-Production Checklist

- [ ] Test entire stack on staging environment
- [ ] Verify all health checks pass
- [ ] Load test the application
- [ ] Backup MySQL database
- [ ] Review and update environment variables
- [ ] Enable HTTPS/SSL for frontend and backend
- [ ] Configure firewall rules
- [ ] Set up monitoring and alerting
- [ ] Document any custom configurations

### Production Security

1. **Use Environment Variables for Secrets**
   ```bash
   # Instead of hardcoding credentials, use:
   export MYSQL_ROOT_PASSWORD=your-secure-password
   export SPRING_DATASOURCE_PASSWORD=your-secure-password
   ```

2. **Enable HTTPS**
   - Update docker-compose.yml to use SSL certificates
   - Configure Nginx to proxy HTTPS requests

3. **Database Access Control**
   - Don't expose MySQL port to host (already not exposed)
   - Use strong passwords
   - Restrict database user permissions

4. **Jenkins Security**
   - Enable authentication
   - Use API tokens instead of passwords
   - Restrict pipeline execution to authenticated users

5. **Docker Security**
   - Use specific image versions (not `latest`)
   - Scan images for vulnerabilities
   - Use private Docker registry

### Monitoring & Logging

1. **Container Logs**
   ```bash
   docker-compose logs -f
   ```

2. **System Monitoring**
   - CPU usage: `docker stats`
   - Disk usage: `df -h`
   - Memory usage: `free -h`

3. **Application Monitoring** (Optional)
   - Integrate with Prometheus/Grafana
   - Set up ELK stack for centralized logging
   - Enable APM (Application Performance Monitoring)

### Backup Strategy

```bash
# Backup MySQL database
docker-compose exec mysql mysqldump -u root -pn3u3da! portfolio_manager > backup.sql

# Backup Docker volumes
docker run --rm \
  -v mysql-data:/data \
  -v $(pwd):/backup \
  alpine tar czf /backup/mysql-data.tar.gz /data

# Restore from backup
docker-compose exec -T mysql mysql -u root -pn3u3da! portfolio_manager < backup.sql
```

---

## Performance Optimization

### Docker Compose Best Practices

1. **Use Specific Image Versions**
   ```yaml
   image: mysql:8.0.32  # Not 8.0 (latest)
   image: nginx:1.25-alpine  # Specific version
   ```

2. **Enable BuildKit for Faster Builds**
   ```bash
   export DOCKER_BUILDKIT=1
   docker build -t portfolio-backend:latest ./Backend
   ```

3. **Use .dockerignore** (Already configured)
   - Reduces build context size
   - Faster image builds

4. **Multi-Stage Builds** (Already implemented)
   - Smaller production images
   - Faster deployments

### Application Optimization

1. **Backend**
   - Adjust JVM heap size in Dockerfile
   - Enable caching (Spring Cache abstraction)
   - Use connection pooling (HikariCP)

2. **Frontend**
   - Enable Nginx gzip compression (Already configured)
   - Configure browser caching (Already configured)
   - Optimize image assets
   - Lazy load components

3. **Database**
   - Create indexes on frequently queried columns
   - Optimize queries
   - Regular maintenance (VACUUM, ANALYZE)

---

## Additional Resources

- [Docker Documentation](https://docs.docker.com/)
- [Docker Compose Reference](https://docs.docker.com/compose/compose-file/)
- [Spring Boot Docker](https://spring.io/guides/gs/spring-boot-docker/)
- [Nginx Configuration Guide](https://nginx.org/en/docs/http/ngx_http_core_module.html)
- [React Router](https://reactrouter.com/)
- [Jenkins Pipeline Documentation](https://www.jenkins.io/doc/book/pipeline/)

---

## Support

For issues or questions:

1. Check [Troubleshooting](#troubleshooting) section
2. Review Docker logs: `docker-compose logs`
3. Check Jenkins pipeline output
4. Contact the development team

---

**Document Version**: 1.0  
**Last Updated**: 2026-08-06  
**Maintained By**: Portfolio Manager Team
