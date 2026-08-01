# 106-ByteBreakers-PortfolioManagement

## Database Setup

1. Create a MySQL database:

```sql
CREATE DATABASE portfolio_manager;
USE portfolio_manager
```

2. Start the Spring Boot application using the **dev** profile.

On the first run, Spring Boot will automatically create the required tables and initialize the database (if initialization scripts are present).

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
