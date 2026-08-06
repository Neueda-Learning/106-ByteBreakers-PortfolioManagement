// ============================================
// Portfolio Manager - Declarative Jenkins Pipeline
// ============================================
// This pipeline automates building, testing, containerizing, and deploying
// the full-stack Portfolio Manager application
//
// Trigger: Automatic when commits are pushed to 'feature/deployment' branch
// Later, this can be changed to 'dev' or 'main' branch
//
// Stages:
// 1. Checkout - Clone repository code
// 2. Build Backend - Maven compile and package
// 3. Run Backend Tests - Execute unit tests
// 4. Build Frontend - Install dependencies and build production bundle
// 5. Build Docker Images - Create Docker images for backend and frontend
// 6. Stop Existing Containers - Gracefully shutdown current deployment
// 7. Deploy Using Docker Compose - Start new containers
// 8. Health Check - Verify all services are healthy
// 9. Cleanup - Remove build artifacts and dangling images

pipeline {
    
    // ============================================
    // Pipeline Configuration
    // ============================================
    agent any
    
    // Global options for the pipeline
    options {
        // Keep last 10 builds
        buildDiscarder(logRotator(numToKeepStr: '10'))
        
        // Prevent concurrent builds (ensure sequential deployment)
        disableConcurrentBuilds()
        
        // Add timestamps to console output
        timestamps()
        
        // Timeout after 1 hour
        timeout(time: 1, unit: 'HOURS')
    }
    
    // Environment variables available to all stages
    environment {
        // Docker image names
        BACKEND_IMAGE = "portfolio-backend:latest"
        FRONTEND_IMAGE = "portfolio-frontend:latest"
        
        // Docker Compose file location
        COMPOSE_FILE = "docker-compose.yml"
        
        // Build output directories
        BACKEND_TARGET = "Backend/target"
        FRONTEND_DIST = "frontend/dist"
        
        // AWS Region (for future ECR integration)
        AWS_REGION = "us-east-1"
    }
    
    // ============================================
    // Build Stages
    // ============================================
    stages {
        
        // ============================================
        // Stage 1: Checkout
        // ============================================
        stage('Checkout') {
            steps {
                script {
                    echo "═══════════════════════════════════════════════════════"
                    echo "STAGE: Checkout"
                    echo "═══════════════════════════════════════════════════════"
                    echo "Cloning repository from feature/deployment branch..."
                }
                
                // Clone the repository with current branch
                checkout scm
                
                script {
                    echo "✓ Code checked out successfully"
                    echo "Current commit: ${GIT_COMMIT}"
                    echo "Branch: ${GIT_BRANCH}"
                }
            }
        }
        
        // ============================================
        // Stage 2: Build Backend
        // ============================================
        stage('Build Backend') {
            steps {
                script {
                    echo "═══════════════════════════════════════════════════════"
                    echo "STAGE: Build Backend"
                    echo "═══════════════════════════════════════════════════════"
                    echo "Building Spring Boot application with Maven..."
                }
                
                dir('Backend') {
                    // Use Maven wrapper if available, else use installed Maven
                    sh '''
                        echo "Running: mvn clean package -DskipTests"
                        if [ -f "mvnw" ]; then
                            chmod +x ./mvnw
                            ./mvnw clean package -DskipTests
                        else
                            mvn clean package -DskipTests
                        fi
                    '''
                }
                
                script {
                    echo "✓ Backend build completed successfully"
                    echo "JAR artifact: Backend/target/portfolioManager-0.0.1-SNAPSHOT.jar"
                }
            }
        }
        
        // ============================================
        // Stage 3: Run Backend Tests
        // ============================================
        // stage('Run Backend Tests') {
        //     steps {
        //         script {
        //             echo "═══════════════════════════════════════════════════════"
        //             echo "STAGE: Run Backend Tests"
        //             echo "═══════════════════════════════════════════════════════"
        //             echo "Executing unit tests..."
        //         }
                
        //         dir('Backend') {
        //             sh '''
        //                 echo "Running: mvn test"
        //                 if [ -f "mvnw" ]; then
        //                     chmod +x ./mvnw
        //                     ./mvnw test
        //                 else
        //                     mvn test
        //                 fi
        //             '''
        //         }
                
        //         script {
        //             echo "✓ Backend tests completed successfully"
        //         }
        //     }
            
        //     post {
        //         always {
        //             // Publish JUnit test results
        //             junit 'Backend/target/surefire-reports/**/*.xml'
                    
        //             script {
        //                 echo "Test results published to Jenkins"
        //             }
        //         }
        //     }
        // }
        
        // ============================================
        // Stage 4: Build Frontend
        // ============================================
        stage('Build Frontend') {
            steps {
                script {
                    echo "═══════════════════════════════════════════════════════"
                    echo "STAGE: Build Frontend"
                    echo "═══════════════════════════════════════════════════════"
                    echo "Building React/Vite production bundle..."
                }
                
                dir('frontend') {
                    sh '''
                        echo "Installing dependencies..."
                        npm ci
                        
                        echo "Building production bundle..."
                        npm run build
                        
                        echo "Verifying dist/ directory..."
                        if [ -d "dist" ]; then
                            echo "✓ dist/ directory created successfully"
                            ls -la dist/ | head -20
                        else
                            echo "✗ ERROR: dist/ directory not found"
                            exit 1
                        fi
                    '''
                }
                
                script {
                    echo "✓ Frontend build completed successfully"
                    echo "Build output: frontend/dist/"
                }
            }
        }
        
        // ============================================
        // Stage 5: Build Docker Images
        // ============================================
        stage('Build Docker Images') {
            steps {
                script {
                    echo "═══════════════════════════════════════════════════════"
                    echo "STAGE: Build Docker Images"
                    echo "═══════════════════════════════════════════════════════"
                    echo "Building Docker images for backend and frontend..."
                }
                
                sh '''
                    echo "Building backend image..."
                    docker build -t ${BACKEND_IMAGE} ./Backend
                    
                    echo "Building frontend image..."
                    docker build -t ${FRONTEND_IMAGE} ./frontend
                    
                    echo "Docker images built successfully:"
                    docker images | grep portfolio
                '''
                
                script {
                    echo "✓ Docker images built successfully"
                }
            }
        }
        
        // ============================================
        // Stage 6: Stop Existing Containers
        // ============================================
        stage('Stop Existing Containers') {
            steps {
                script {
                    echo "═══════════════════════════════════════════════════════"
                    echo "STAGE: Stop Existing Containers"
                    echo "═══════════════════════════════════════════════════════"
                    echo "Stopping and removing current deployment..."
                }
                
                sh '''
                    echo "Running: docker-compose down"
                    
                    # Stop and remove containers (ignore errors if containers don't exist)
                    docker-compose -f ${COMPOSE_FILE} down || true
                    
                    echo "Waiting for graceful shutdown..."
                    sleep 5
                    
                    echo "✓ Existing containers stopped"
                '''
            }
        }
        
        // ============================================
        // Stage 7: Deploy Using Docker Compose
        // ============================================
        stage('Deploy Using Docker Compose') {
            steps {
                script {
                    echo "═══════════════════════════════════════════════════════"
                    echo "STAGE: Deploy Using Docker Compose"
                    echo "═══════════════════════════════════════════════════════"
                    echo "Starting new deployment..."
                }
                
                sh '''
                    echo "Running: docker-compose up -d"
                    docker-compose -f ${COMPOSE_FILE} up -d
                    
                    echo "Waiting for services to start..."
                    sleep 10
                    
                    echo "Service status:"
                    docker-compose -f ${COMPOSE_FILE} ps
                '''
                
                script {
                    echo "✓ Docker Compose deployment completed"
                }
            }
        }
        
        // ============================================
        // Stage 8: Health Check
        // ============================================
        stage('Health Check') {
            steps {
                script {
                    echo "═══════════════════════════════════════════════════════"
                    echo "STAGE: Health Check"
                    echo "═══════════════════════════════════════════════════════"
                    echo "Verifying all services are healthy..."
                }
                
                sh '''
                    # Function to retry with timeout
                    retry_check() {
                        local url=$1
                        local max_attempts=$2
                        local attempt=1
                        
                        while [ $attempt -le $max_attempts ]; do
                            echo "Attempt $attempt: Checking $url..."
                            if curl -f -s "$url" > /dev/null 2>&1; then
                                echo "✓ $url is healthy"
                                return 0
                            fi
                            attempt=$((attempt + 1))
                            sleep 5
                        done
                        
                        echo "✗ $url failed health check after $max_attempts attempts"
                        return 1
                    }
                    
                    echo "Checking MySQL health..."
                    retry_check "http://localhost:8081/actuator/health" 10
                    
                    echo "Checking Backend health..."
                    retry_check "http://localhost:8081/actuator/health" 10
                    
                    echo "Checking Frontend health..."
                    retry_check "http://localhost:8082/index.html" 10
                    
                    echo ""
                    echo "✓ All health checks passed"
                '''
            }
        }
        
        // ============================================
        // Stage 9: Cleanup
        // ============================================
        stage('Cleanup') {
            steps {
                script {
                    echo "═══════════════════════════════════════════════════════"
                    echo "STAGE: Cleanup"
                    echo "═══════════════════════════════════════════════════════"
                    echo "Cleaning up build artifacts..."
                }
                
                sh '''
                    echo "Removing dangling Docker images..."
                    docker image prune -f || true
                    
                    echo "Removing dangling volumes..."
                    docker volume prune -f || true
                    
                    echo "✓ Cleanup completed"
                '''
                
                script {
                    echo "Clearing Jenkins workspace..."
                    cleanWs(
                        deleteDirs: true,
                        patterns: [
                            [pattern: 'Backend/target/**', type: 'INCLUDE'],
                            [pattern: 'frontend/dist/**', type: 'INCLUDE'],
                            [pattern: 'frontend/node_modules/**', type: 'INCLUDE']
                        ]
                    )
                }
            }
        }
    }
    
    // ============================================
    // Post Build Actions
    // ============================================
    post {
        always {
            script {
                echo "═══════════════════════════════════════════════════════"
                echo "PIPELINE SUMMARY"
                echo "═══════════════════════════════════════════════════════"
            }
            
            // Archive logs for troubleshooting
            sh '''
                echo "Archiving Docker logs..."
                mkdir -p logs
                
                docker-compose -f ${COMPOSE_FILE} logs > logs/docker-compose.log 2>&1 || true
                docker-compose -f ${COMPOSE_FILE} logs mysql > logs/mysql.log 2>&1 || true
                docker-compose -f ${COMPOSE_FILE} logs backend > logs/backend.log 2>&1 || true
                docker-compose -f ${COMPOSE_FILE} logs frontend > logs/frontend.log 2>&1 || true
            '''
            
            // Archive logs to Jenkins artifacts
            archiveArtifacts artifacts: 'logs/**/*.log', allowEmptyArchive: true
        }
        
        success {
            script {
                echo "✓✓✓ PIPELINE SUCCEEDED ✓✓✓"
                echo ""
                echo "Deployment completed successfully!"
                echo ""
                echo "Access the application at:"
                echo "  Frontend: http://10.9.64.59:8082"
                echo "  Backend API: http://10.9.64.59:8081"
                echo "  Jenkins: http://10.9.64.59:8080"
                echo ""
                echo "View logs with:"
                echo "  docker-compose logs -f [service-name]"
            }
        }
        
        failure {
            script {
                echo "✗✗✗ PIPELINE FAILED ✗✗✗"
                echo ""
                echo "The deployment encountered an error. Troubleshooting steps:"
                echo "  1. Check logs in the 'Logs' section above"
                echo "  2. Run: docker-compose logs -f"
                echo "  3. Check Docker containers: docker ps -a"
                echo "  4. Review individual service logs"
            }
        }
        
        unstable {
            script {
                echo "⚠ PIPELINE UNSTABLE ⚠"
                echo "Some stages completed but tests or checks failed."
            }
        }
    }
}
