# 106-ByteBreakers-PortfolioManagement

## Database Setup

This project uses separate databases for `dev` and `test` profiles.

### Development (`dev`) Profile

1. Create the development database:

```sql
CREATE DATABASE portfolio_manager;
USE portfolio_manager;
```

2. Start the Spring Boot application using the `dev` profile.

On the first run, Spring Boot will automatically create the required tables and initialize the database (if initialization scripts are present).

### Testing (`test`) Profile

1. Create the test database:

```sql
CREATE DATABASE portfolio_manager_test;
USE portfolio_manager_test;
```

2. Run tests using the `test` profile so test data stays isolated from development data.

## Run Locally

Follow these steps to run both backend and frontend on your machine.

### 1) Start Backend (Spring Boot)

Before starting backend, update database credentials in `Backend/src/main/resources/application-dev.properties`:

- `spring.datasource.username` = your local MySQL username
- `spring.datasource.password` = your local MySQL password

Open a terminal in the `Backend` folder and run:

```bash
./mvnw spring-boot:run
```

On Windows PowerShell, you can also run:

```powershell
.\mvnw.cmd spring-boot:run
```

Backend will run on:

- http://localhost:8081

The investments endpoint used by frontend is:

- http://localhost:8081/userInvestments/

### 2) Start Frontend (Vite + React)

Open another terminal in the `frontend` folder and run:

```bash
npm install
npm run dev
```

Frontend will run on:

- http://localhost:5173

### 3) Verify Connection

With both servers running:

- Open http://localhost:5173
- Go to the Investments page
- Confirm rows are loaded from backend API

## Git Workflow

To keep the repository organized and ensure proper code reviews, **all development must go through the `dev` branch**.

### Branching Strategy

- `main` – Stable, production-ready branch.
- `dev` – Integration branch for ongoing development.
- `feature/<feature-name>` – Individual feature branches.

### Development Workflow

1. Pull the latest changes from `dev`.
2. Create a new feature branch from `dev`.
3. Implement your changes and commit them.
4. Push your feature branch to GitHub.
5. Open a Pull Request **from your feature branch to `dev`**.
6. Wait for at least **one team member's approval** before merging.
7. Once all planned features are completed and tested on `dev`, create a Pull Request **from `dev` to `main`**.

### Important Rules

- ❌ Do **not** commit or push directly to `main`.
- ❌ Do **not** create Pull Requests directly from feature branches to `main`.
- ✅ Always create feature branches from `dev`.
- ✅ Always merge feature branches into `dev` through a Pull Request.
- ✅ Keep your feature branch up to date with `dev` before opening a Pull Request.

### Workflow Diagram

```text
main
  ↑
PR: dev → main

dev
  ↑
PR: feature/auth → dev
PR: feature/dashboard → dev
PR: feature/backend → dev
```
