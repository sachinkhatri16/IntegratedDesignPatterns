# Integrated Design Patterns - Food Ordering System

## Database

This project now uses PostgreSQL instead of SQLite.

### Default connection

`DatabaseManager` uses these defaults:

- URL: `jdbc:postgresql://localhost:5432/food_ordering`
- User: `postgres`
- Password: empty

The shared IntelliJ run configuration may set `DB_PASSWORD` to the sample value `1928374650@Asd`. If your local PostgreSQL password is different, update the run configuration or set `DB_PASSWORD` before launching.

### Optional environment variables

You can override the defaults with:

- `DB_URL`
- `DB_USER`
- `DB_PASSWORD`

Example:

```powershell
$env:DB_URL = "jdbc:postgresql://localhost:5432/food_ordering"
$env:DB_USER = "postgres"
$env:DB_PASSWORD = "your_actual_postgres_password"
mvn test
```

### Notes

- The database tables are created automatically on first connection.
- Default menu items are seeded using PostgreSQL-compatible `ON CONFLICT DO NOTHING` inserts.
- The old `food_ordering.db` file is no longer used by the application.

## Quick Start (Local PostgreSQL)

### Step 1: Start PostgreSQL Service
Open PowerShell and ensure PostgreSQL service is running:
```powershell
Get-Service -Name *postgres*
Start-Service -Name postgresql-x64-14
```
(Replace `postgresql-x64-14` with your actual PostgreSQL service name)

### Step 2: Create Database & User
Run these psql commands in PowerShell:
```powershell
psql -U postgres -h localhost -p 5432 -c "CREATE DATABASE food_ordering;"
psql -U postgres -h localhost -p 5432 -c "ALTER USER postgres WITH PASSWORD '1928374650@Asd';"
```

### Step 3: Set Environment Variables & Test Connection
```powershell
$env:DB_URL = 'jdbc:postgresql://localhost:5432/food_ordering'
$env:DB_USER = 'postgres'
$env:DB_PASSWORD = 'your_actual_postgres_password'
mvn -Dmaven.test.skip=true package
mvn exec:java -Dexec.mainClass="com.foodordering.util.DbConnectionTest"
```

If you see ✓ SUCCESS, the database is connected and ready!

### Step 4: Run the App
```powershell
mvn exec:java
```

Or using the shaded jar:
```powershell
java -jar target/food-ordering-system-1.0.0-shaded.jar
```

Or run the PowerShell helper, which prompts for the PostgreSQL password if `DB_PASSWORD` is not set:

```powershell
.\start-app.ps1
```

