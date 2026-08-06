#  ByteBreakers Portfolio Management

> A modern, full-stack portfolio management web application built with Spring Boot, React, and TypeScript.

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.0-brightgreen.svg)
![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)
![React](https://img.shields.io/badge/React-19.1.1-61DAFB?logo=react&logoColor=black)
![TypeScript](https://img.shields.io/badge/TypeScript-6.0-3178C6?logo=typescript&logoColor=white)
![Vite](https://img.shields.io/badge/Vite-8.2-646CFF?logo=vite&logoColor=white)
![Mantine](https://img.shields.io/badge/Mantine-9.5-339AF0?logo=mantine&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0+-4479A1?logo=mysql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.8+-C71A36?logo=apachemaven&logoColor=white)

---

##  Table of Contents

- [Project Overview](#-project-overview)
- [Features](#-features)
- [Technology Stack](#%EF%B8%8F-technology-stack)
- [Project Structure](#-project-structure)
- [System Architecture](#-system-architecture)
- [Prerequisites](#-prerequisites)
- [Installation & Setup](#-installation--setup)
- [Running the Application](#-running-the-application)
- [API Documentation](#-api-documentation)
- [Development Guidelines](#-development-guidelines)
- [Git Workflow](#-git-workflow)

---

##  Project Overview

**ByteBreakers Portfolio Management** is a comprehensive web application designed to help users efficiently manage, track, and analyze their investment portfolios. The application provides an intuitive interface for monitoring investment performance, receiving diversification recommendations, and executing trades seamlessly.

Built with modern technologies, it demonstrates best practices in full-stack development with a robust backend API and a responsive, interactive frontend.

---

##  Features

 **Portfolio Tracking & Management**
- Real-time portfolio overview and performance metrics
- Detailed investment tracking with historical data
- Portfolio rebalancing recommendations

 **Dashboards**
- Comprehensive dashboard with key performance indicators
- Visual representation of portfolio composition

 **Investment Management**
- Browse available investment options
- View detailed investment information
- Execute trades with transaction history
- Monitor current holdings with real-time updates

 **Diversification Analysis**
- Algorithm based diversification recommendations
- Strategic allocation suggestions

 **Modern User Experience**
- Responsive and accessible UI built with Mantine
- Real-time data updates via REST API
- Smooth navigation with React Router
- Professional data visualization with Chart.js

 **Enterprise-Grade Backend**
- RESTful API with comprehensive error handling
- Swagger/OpenAPI documentation
- Database transaction support

---

##  Technology Stack

### **Backend**

| Technology | Version | Purpose |
|-----------|---------|---------|
| **Spring Boot** | 4.1.0 | Web framework & application container |
| **Java** | 17 | Programming language |
| **Spring WebMVC** | 4.1.0 | REST controller & MVC architecture |
| **Spring Data JDBC** | 4.1.0 | Database access & ORM |
| **MySQL Connector/J** | Latest | MySQL database driver |
| **Springdoc OpenAPI** | 2.8.9 | API documentation & Swagger UI |
| **Maven** | 3.8+ | Build & dependency management |

### **Frontend**

| Technology | Version | Purpose                      |
|-----------|---------|------------------------------|
| **React** | 19.1.1 | UI framework                 |
| **TypeScript** | 6.0 | Type-safe JavaScript         |
| **Vite** | 8.2.0 | Fast build tool & dev server |
| **React Router** | 7.18.2 | Client-side routing          |
| **Mantine UI** | 9.5.1 | Component library & styling  |
| **Axios** | 1.11.0 | HTTP client for API calls    |
| **Chart.js** | 4.5.1 | Data visualization           |
| **Lucide React** | 1.28.0 | Icons                        |

### **Database**

| Technology | Version | Purpose |
|-----------|---------|---------|
| **MySQL** | 8.0+ | Relational database |

---

##  Project Structure

```
106-ByteBreakers-PortfolioManagement/
│
├── Backend/                           # Spring Boot Backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/neueda/portfolio/
│   │   │   │   ├── controller/        # REST API endpoints
│   │   │   │   ├── service/           # Business logic layer
│   │   │   │   ├── repository/        # Data access layer
│   │   │   │   ├── entity/            # JPA/JDBC entities
│   │   │   │   ├── dto/               # Data Transfer Objects
│   │   │   │   ├── diversify/         # Diversification logic
│   │   │   │   └── config/            # Configuration classes
│   │   │   └── resources/             # App properties & SQL scripts
│   │   └── test/
│   │       └── java/com/neueda/portfolio/   # Unit & integration tests
│
├── frontend/                          # React + TypeScript Frontend
│   ├── src/
│   │   ├── components/                # Reusable UI components
│   │   │   ├── common/                # Shared components (Header, Sidebar, etc.)
│   │   │   ├── investments/           # Investment-specific components
│   │   │   └── layout/                # Layout components
│   │   ├── pages/                     # Page components (routed views)
│   │   ├── hooks/                     # Custom React hooks
│   │   ├── constants/                 # App-wide constants & route definitions
│   │   ├── theme/                     # Mantine theme & colour palette
│   │   ├── util/                      # Utility & helper functions
│   │   └── services/                  # Axios API service layer
│   └── public/                        # Static assets
│
└── README.md                          # Project documentation

```

---

##  System Architecture

```
┌─────────────────────────────────────────────────────────────────────────┐
│                          USER BROWSER                                    │
│                       (http://localhost:5173)                           │
└──────────────────────┬──────────────────────────────────────────────────┘
                       │
                       ▼
┌──────────────────────────────────────────────────────────────────────────┐
│                     REACT FRONTEND (Vite + TypeScript)                   │
│  ┌────────────────┐  ┌──────────────┐  ┌──────────────┐  ┌────────────┐ │
│  │ React Router   │  │ Mantine UI   │  │  Chart.js    │  │   Axios    │ │
│  │ (Navigation)   │  │   (Layout)   │  │ (Analytics)  │  │   (HTTP)   │ │
│  └────────────────┘  └──────────────┘  └──────────────┘  └────────────┘ │
│        │                    │                    │               │       │
│        │     Pages & Components                 │               │       │
│        │     Dashboard, Investments, Trades     │               │       │
└────────┼────────────────────────────────────────┼───────────────┼───────┘
         │                                        │               │
         └────────────────────────────┬───────────┴───────────────┘
                                      ▼
                    API CALLS (REST/JSON over HTTPS)
                   (http://localhost:8081/api/*)
                                      │
                                      ▼
┌──────────────────────────────────────────────────────────────────────────┐
│             SPRING BOOT BACKEND (Java 17 + Spring WebMVC)                │
│                                                                           │
│  ┌────────────────────────────────────────────────────────────────────┐  │
│  │ REST Controllers (API Layer)                                       │  │
│  │ ├─ APIendpointController - Main API endpoints                     │  │
│  │ ├─ InvestmentOptionController - Investment options API            │  │
│  │ ├─ CurrentHoldingController - User holdings API                   │  │
│  │ └─ ... (Other controllers)                                        │  │
│  └────────────────────────────────────────────────────────────────────┘  │
│                                     │                                      │
│                                     ▼                                      │
│  ┌────────────────────────────────────────────────────────────────────┐  │
│  │ Service Layer (Business Logic)                                     │  │
│  │ ├─ InvestmentOptionService - Investment management                │  │
│  │ ├─ CurrentHoldingService - Portfolio management                   │  │
│  │ ├─ TransactionService - Trade execution                           │  │
│  │ ├─ DiversifyService - Diversification recommendations             │  │
│  │ └─ ... (Other services)                                           │  │
│  └────────────────────────────────────────────────────────────────────┘  │
│                                     │                                      │
│                                     ▼                                      │
│  ┌────────────────────────────────────────────────────────────────────┐  │
│  │ Repository Layer (Data Access - Spring Data JDBC)                 │  │
│  │ ├─ InvestmentOptionRepository - Query investments                 │  │
│  │ ├─ CurrentHoldingRepository - Query holdings                      │  │
│  │ ├─ TransactionHistoryRepository - Query transactions              │  │
│  │ └─ ... (Other repositories)                                       │  │
│  └────────────────────────────────────────────────────────────────────┘  │
│                                     │                                      │
│                                     ▼                                      │
│  ┌────────────────────────────────────────────────────────────────────┐  │
│  │ Entity Models (Domain Objects)                                     │  │
│  │ ├─ InvestmentOption - Investment data                             │  │
│  │ ├─ CurrentHolding - User's current position                       │  │
│  │ ├─ TransactionHistory - Trade records                             │  │
│  │ └─ ... (Other entities)                                           │  │
│  └────────────────────────────────────────────────────────────────────┘  │
│                                     │                                      │
│                                     ▼                                      │
│  ┌────────────────────────────────────────────────────────────────────┐  │
│  │ Spring Data JDBC (ORM Layer)                                       │  │
│  │ MySQL Connector/J                                                  │  │
│  └────────────────────────────────────────────────────────────────────┘  │
└──────────────────────┬──────────────────────────────────────────────────┘
                       │
                       ▼
┌──────────────────────────────────────────────────────────────────────────┐
│                         MYSQL DATABASE                                    │
│                   (portfolio_manager / portfolio_manager_test)           │
│                                                                           │
│  ┌──────────────────┐  ┌─────────────────┐  ┌──────────────────────┐   │
│  │ investment_option│  │ current_holding │  │transaction_history   │   │
│  ├──────────────────┤  ├─────────────────┤  ├──────────────────────┤   │
│  │ id               │  │ id              │  │ id                   │   │
│  │ name             │  │ investment_id   │  │ holding_id           │   │
│  │ symbol           │  │ user_id         │  │ transaction_type     │   │
│  │ price            │  │ quantity        │  │ quantity             │   │
│  │ category         │  │ purchase_price  │  │ price                │   │
│  │ ... (more)       │  │ ... (more)      │  │ transaction_date     │   │
│  └──────────────────┘  └─────────────────┘  │ ... (more)           │   │
│                                              └──────────────────────┘   │
└──────────────────────────────────────────────────────────────────────────┘
```

### Architecture Breakdown

1. **Frontend Layer**: React application with TypeScript, providing an interactive UI
2. **API Layer**: Spring Boot REST controllers exposing RESTful endpoints
3. **Business Logic**: Service layer handling core application logic
4. **Data Access**: Repository layer using Spring Data JDBC for database operations
5. **Database**: MySQL storing all application data

---

##  Prerequisites

Before you begin, ensure you have the following installed on your system:

### Required
- **Java Development Kit (JDK)** 17 or higher
  - Download from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://adoptopenjdk.net/)
- **Maven** 3.8.0 or higher
  - Included with the project (`mvnw.cmd` for Windows, `mvnw` for Linux/macOS)
- **Node.js** 16.x or higher with npm
  - Download from [nodejs.org](https://nodejs.org/)
- **MySQL** 8.0 or higher
  - Download from [mysql.com](https://www.mysql.com/downloads/) or use Docker

### Optional but Recommended
- **Git** for version control
- **IDE**: IntelliJ IDEA (backend) or VS Code (frontend)
- **Docker** for containerized MySQL database
- **Postman** or **Thunder Client** for API testing

---

##  Installation & Setup

### 1. Clone the Repository

```bash
git clone https://github.com/ByteBreakers/106-ByteBreakers-PortfolioManagement.git
cd 106-ByteBreakers-PortfolioManagement
```

### 2. Database Setup

#### Option A: Manual MySQL Setup

1. **Create development database:**
   ```sql
   CREATE DATABASE portfolio_manager;
   USE portfolio_manager;
   ```

2. **Create test database:**
   ```sql
   CREATE DATABASE portfolio_manager_test;
   USE portfolio_manager_test;
   ```

3. **Initialize schema and data:**
   - The Spring Boot application will automatically create tables on first run using `schema.sql` and `data.sql` from `Backend/src/main/resources/`

#### Option B: Docker Setup (Recommended)

```bash
# Pull MySQL image
docker pull mysql:8.0

# Run MySQL container
docker run --name portfolio-mysql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=portfolio_manager -p 3306:3306 -d mysql:8.0

# For test database
docker exec portfolio-mysql mysql -uroot -proot -e "CREATE DATABASE portfolio_manager_test;"
```

### 3. Backend Configuration

1. **Navigate to backend folder:**
   ```bash
   cd Backend
   ```

2. **Update database credentials** in `src/main/resources/application-dev.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/portfolio_manager?useTimezone=true&serverTimezone=UTC
   spring.datasource.username=your_mysql_username
   spring.datasource.password=your_mysql_password
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   
   spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
   spring.sql.init.mode=always
   spring.sql.init.data-locations=classpath:data.sql
   ```

   For test environment, update `application-test.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/portfolio_manager_test?useTimezone=true&serverTimezone=UTC
   spring.datasource.username=your_mysql_username
   spring.datasource.password=your_mysql_password
   ```

3. **Verify Maven dependencies:**
   ```bash
   # Windows
   .\mvnw.cmd verify
   
   # Linux/macOS
   ./mvnw verify
   ```

### 4. Frontend Setup

1. **Navigate to frontend folder:**
   ```bash
   cd frontend
   ```

2. **Install dependencies:**
   ```bash
   npm install
   ```

3. **Verify installation:**
   ```bash
   npm run build
   ```

---

##  Running the Application

### Starting the Backend

```bash
cd Backend

# Windows PowerShell
.\mvnw.cmd spring-boot:run

# Linux/macOS
./mvnw spring-boot:run
```

**Expected output:**
```
Started PortfolioManagerApplication in X.XXX seconds
```

**Backend will be available at:** `http://localhost:8081`

### Starting the Frontend

Open a new terminal/command prompt:

```bash
cd frontend

# Development mode
npm run dev

# Or production build
npm run build
npm run preview
```

**Expected output:**
```
  VITE v8.2.0  ready in XXX ms

    Local:   http://localhost:5173/
    press h to show help
```

**Frontend will be available at:** `http://localhost:5173`

### Verify Both Services

1. Open your browser to `http://localhost:5173`
2. Navigate to any dashboard or investment page
3. You should see data loading from the backend API
4. Check browser console for any errors

---

##  API Documentation

### Swagger UI Access

Once the backend is running, access the interactive API documentation:

**URL:** `http://localhost:8081/swagger-ui.html`

Or OpenAPI JSON specification:

**URL:** `http://localhost:8081/v3/api-docs`

### Main API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| **GET** | `/api/userInvestments` | Get user's investment holdings |
| **GET** | `/api/investmentOptions` | Get all available investment options |
| **GET** | `/api/investmentOptions/{id}` | Get specific investment details |
| **POST** | `/api/trades` | Execute a trade (buy/sell) |
| **GET** | `/api/trades/history` | Get transaction history |
| **GET** | `/api/portfolio/summary` | Get portfolio summary & metrics |
| **GET** | `/api/diversify/recommendations` | Get diversification recommendations |

### Example API Call using cURL

```bash
# Get all investment options
curl -X GET http://localhost:8081/api/investmentOptions \
  -H "Content-Type: application/json"

# Get user investments
curl -X GET http://localhost:8081/api/userInvestments \
  -H "Content-Type: application/json"
```

### Example API Call using JavaScript/Axios

```typescript
import axios from 'axios';

// Get investments
axios.get('http://localhost:8081/api/investmentOptions')
  .then(response => console.log(response.data))
  .catch(error => console.error(error));
```

---

##  Development Guidelines

### Code Structure & Conventions

#### Backend (Java)

- **Controllers**: REST endpoints, request validation, response formatting
  - Location: `src/main/java/com/neueda/portfolio/controller/`
  - Naming: `*Controller.java`
  - Example: `InvestmentOptionController.java`

- **Services**: Business logic, calculations, data processing
  - Location: `src/main/java/com/neueda/portfolio/service/`
  - Naming: `*Service.java`
  - Example: `InvestmentOptionService.java`

- **Repositories**: Database queries, CRUD operations
  - Location: `src/main/java/com/neueda/portfolio/repository/`
  - Naming: `*Repository.java`
  - Example: `InvestmentOptionRepository.java`

- **Entities**: Database model classes
  - Location: `src/main/java/com/neueda/portfolio/entity/`
  - Naming: `*.java` (CamelCase)
  - Example: `InvestmentOption.java`, `CurrentHolding.java`

- **DTOs**: Data Transfer Objects for API requests/responses
  - Location: `src/main/java/com/neueda/portfolio/dto/`
  - Naming: `*DTO.java` or `*Request.java` / `*Response.java`

#### Frontend (React/TypeScript)

- **Components**: Reusable UI components
  - Location: `src/components/`
  - Naming: `ComponentName.tsx`
  - Example: `InvestmentCard.tsx`, `PortfolioChart.tsx`

- **Pages**: Full page components (routed)
  - Location: `src/pages/`
  - Naming: `PageName.tsx`
  - Example: `Dashboard.tsx`, `MyInvestments.tsx`

- **Hooks**: Custom React hooks for logic
  - Location: `src/hooks/`
  - Naming: `useHookName.ts`
  - Example: `useInvestments.ts`, `usePortfolioSummary.ts`

- **Utilities**: Helper functions
  - Location: `src/util/`
  - Naming: `filename.ts`
  - Example: `currency.ts`, `formatting.ts`

- **Constants**: Application constants
  - Location: `src/constants/`
  - Naming: `filename.ts`
  - Example: `routes.ts`, `navigation.ts`

- **Theme**: Styling and theming
  - Location: `src/theme/`
  - Naming: `filename.ts`
  - Example: `theme.ts`, `colours.ts`


### Code Quality

- **Backend**: Follow Java conventions (camelCase for methods/variables, PascalCase for classes)
- **Frontend**: Use TypeScript strict mode, follow React best practices
- **Commit Messages**: Use descriptive, conventional commit messages (feat:, fix:, docs:, etc.)

---

##  Git Workflow

### Branch Structure

```
main (production)
  │
  └── dev (development/integration)
        │
        ├── feature/auth
        ├── feature/dashboard
        ├── feature/backend-api
        ├── bugfix/login-issue
        └── ... (other feature branches)
```

### Branching Strategy

| Branch Type | Purpose | Source | Target |
|------------|---------|--------|--------|
| `main` | Production-ready code | `dev` via PR | - |
| `dev` | Integration branch | Feature branches via PR | `main` via PR |
| `feature/*` | New features | `dev` | `dev` via PR |
| `fix/*` | Bug fixes | `dev` | `dev` via PR |

### Development Workflow

#### 1. **Set up your local environment**
```bash
git clone https://github.com/ByteBreakers/106-ByteBreakers-PortfolioManagement.git
cd 106-ByteBreakers-PortfolioManagement
git checkout dev
git pull origin dev
```

#### 2. **Create a feature branch**
```bash
# Create branch from dev
git checkout -b feature/your-feature-name

# Naming conventions:
# feature/add-dashboard-charts
# feature/implement-user-auth
# bugfix/fix-portfolio-calculation
# docs/update-readme
```

#### 3. **Make your changes**
```bash
# Stage changes
git add .

# Commit with descriptive message
git commit -m "feat: add portfolio diversification recommendations

- Implement diversification algorithm
- Add recommendation UI component
- Update API endpoints

Closes #123"
```

#### 4. **Keep your branch updated**
```bash
# Fetch latest changes
git fetch origin

# Rebase on dev (to keep history clean)
git rebase origin/dev

# If conflicts arise, resolve them and continue
git rebase --continue
```

#### 5. **Push your changes**
```bash
git push origin feature/your-feature-name
```

#### 6. **Open a Pull Request**
- Go to GitHub repository
- Click "New Pull Request"
- Set base to `dev`, compare to your feature branch
- Write descriptive PR title and description
- Link any related issues (`Closes #123`)
- Request review from team members

#### 7. **Code Review & Approval**
- Address review comments
- Push additional commits if needed
- Wait for at least 1 approval from team members

#### 8. **Merge to dev**
```bash
# Once approved, merge via GitHub (use "Squash and merge" for clean history)
# Or merge locally:
git checkout dev
git pull origin dev
git merge --no-ff feature/your-feature-name
git push origin dev
```

#### 9. **Deploy from dev to main**
Once all features are tested and stable on `dev`:
```bash
# Open PR from dev to main
# Follow same review process
# Merge to main only after thorough testing
```

### Important Rules

 **DO:**
- Always create feature branches from `dev`
- Always merge feature branches into `dev` (via PR)
- Keep `dev` and `main` in sync
- Write clear commit messages
- Test your changes before pushing
- Review other team members' code

 **DON'T:**
- Commit directly to `main` or `dev`
- Push large, untested changes
- Ignore merge conflicts
- Force push to shared branches
- Create PRs directly from feature branches to `main`

---


##  Building for Production

### Backend

```bash
cd Backend

# Build JAR file
.\mvnw.cmd clean package

# JAR will be in: target/portfolioManager-0.0.1-SNAPSHOT.jar

# Run JAR
java -jar target/portfolioManager-0.0.1-SNAPSHOT.jar
```

### Frontend

```bash
cd frontend

# Build optimized bundle
npm run build

# Output in: dist/

# Preview production build
npm run preview
```

---
