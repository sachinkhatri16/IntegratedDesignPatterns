# Setup for Windows - cmd.exe AND PowerShell

## ⚠️ IMPORTANT: Open PowerShell, NOT cmd.exe

**Right-click Start menu → Windows PowerShell → Run as Administrator**

---

## Step 1: Verify PostgreSQL is Running

In PowerShell (as admin):
```powershell
Get-Service -Name *postgres*
```

If status shows "Running", you're good. If "Stopped", start it:
```powershell
Start-Service -Name postgresql-x64-14
```
(Replace `postgresql-x64-14` with your actual PostgreSQL service name if different)

---

## Step 2: Fix PostgreSQL Password

Since you got "password authentication failed", let's reset the `postgres` user password.

**Option A: Using psql with no password (if postgres user has no password set)**

```powershell
psql -U postgres -h localhost -p 5432 -w -c "ALTER USER postgres WITH PASSWORD '1928374650@Asd';"
```

**Option B: If that fails, you may need to edit PostgreSQL config**

Check PostgreSQL pg_hba.conf and temporarily allow trust auth:

1. Find pg_hba.conf location:
   ```powershell
   psql -U postgres -h localhost -p 5432 -c "SHOW hba_file;" -w
   ```

2. Edit the file (usually `C:\Program Files\PostgreSQL\15\data\pg_hba.conf`)
   - Find lines with `host    all             all` 
   - Change method from `md5` or `scram-sha-256` to `trust`
   - Restart PostgreSQL:
     ```powershell
     Restart-Service -Name postgresql-x64-14
     ```

3. Now set the password:
   ```powershell
   psql -U postgres -h localhost -p 5432 -c "ALTER USER postgres WITH PASSWORD '1928374650@Asd';"
   ```

4. Change pg_hba.conf back to `scram-sha-256` and restart again.

---

## Step 3: Create the Database

```powershell
psql -U postgres -h localhost -p 5432 -c "CREATE DATABASE food_ordering;"
```

When prompted for password, enter: `1928374650@Asd`

---

## Step 4: Set Environment Variables & Test

In the SAME PowerShell window:

```powershell
$env:DB_URL = 'jdbc:postgresql://localhost:5432/food_ordering'
$env:DB_USER = 'postgres'
$env:DB_PASSWORD = '1928374650@Asd'
```

Navigate to your project:
```powershell
cd "C:\Users\Sachin Khatri\IdeaProjects\IntegratedDesignPatterns"
```

Build and test:
```powershell
mvn -DskipTests package
mvn exec:java -Dexec.mainClass="com.foodordering.util.DbConnectionTest"
```

---

## Step 5: Run the Application

If the test shows `✓ SUCCESS`:

```powershell
mvn exec:java
```

---

## If you MUST use cmd.exe:

Set env vars differently:
```cmd
set DB_URL=jdbc:postgresql://localhost:5432/food_ordering
set DB_USER=postgres
set DB_PASSWORD=1928374650@Asd
cd C:\Users\Sachin Khatri\IdeaProjects\IntegratedDesignPatterns
mvn -DskipTests package
mvn exec:java -Dexec.mainClass="com.foodordering.util.DbConnectionTest"
```

But PowerShell is recommended and easier.

---

## Troubleshooting

**Still getting "password authentication failed"?**

The postgres user doesn't have password `1928374650@Asd` yet. You need to set it. Follow Option A or B above.

**Can't connect to psql?**

```powershell
netstat -ano | findstr 5432
```

If no output, PostgreSQL isn't listening. Restart the service.

**psql command not found?**

Add PostgreSQL to PATH. Open PowerShell as admin:
```powershell
[Environment]::SetEnvironmentVariable("PATH", "$env:PATH;C:\Program Files\PostgreSQL\15\bin", [EnvironmentVariableTarget]::User)
```

Then close and reopen PowerShell.

