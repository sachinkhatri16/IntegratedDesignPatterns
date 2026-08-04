# Database Connection Setup - Simple Steps

## ⚠️ OPEN POWERSHELL, NOT cmd.exe!

**Right-click Start menu → Windows PowerShell (or Terminal) → Run as Administrator**

---

## ONE COMMAND TO RUN EVERYTHING

If `DB_PASSWORD` is already set to your real PostgreSQL password, just run:

```powershell
cd "C:\Users\Sachin Khatri\IdeaProjects\IntegratedDesignPatterns"
.\start-app.ps1
```

If `DB_PASSWORD` is not set, the script prompts for the PostgreSQL password for `postgres`.

This script will:
1. ✓ Set environment variables for this run
2. ✓ Build the project
3. ✓ Test the database connection
4. ✓ Run the app

---

## Manual Steps (if you prefer)

### Step 1: Create Database
```powershell
psql -U postgres -h localhost -p 5432 -c "CREATE DATABASE food_ordering;"
```

### Step 2: Set Environment Variables & Run
```powershell
$env:DB_URL = 'jdbc:postgresql://localhost:5432/food_ordering'
$env:DB_USER = 'postgres'
$env:DB_PASSWORD = 'your_actual_postgres_password'
mvn -Dmaven.test.skip=true package
mvn exec:java
```

## Troubleshooting

**psql command not found?**
- Add PostgreSQL bin folder to PATH, or use full path:
  ```powershell
  & "C:\Program Files\PostgreSQL\15\bin\psql.exe" -U postgres -h localhost -p 5432 -c "CREATE DATABASE food_ordering;"
  ```

**PostgreSQL service not running?**
```powershell
Get-Service -Name *postgres*
Start-Service -Name postgresql-x64-14  # use actual service name
```

**Connection test shows FAILED?**
- Verify service is running: `Get-Service -Name *postgres* | Select Status`
- Verify port 5432 is listening: `netstat -ano | findstr 5432`
- Check database was created: `psql -U postgres -l`
- Check password is correct by logging in: `psql -U postgres -h localhost -p 5432`
- In IntelliJ, open **Run > Edit Configurations > FoodOrderingSystemDemo** and set `DB_PASSWORD` to that same password.

**Can't run scripts?**
```powershell
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
```



