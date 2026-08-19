param(
    [string]$DbUrl = $(if ($env:DB_URL) { $env:DB_URL } else { "jdbc:postgresql://localhost:5432/food_ordering" }),
    [string]$DbUser = $(if ($env:DB_USER) { $env:DB_USER } else { "postgres" }),
    [string]$DbPassword = $env:DB_PASSWORD,
    [switch]$SkipPackage,
    [switch]$SkipConnectionTest
)

$ErrorActionPreference = "Stop"

function ConvertFrom-SecureStringToPlainText {
    param([securestring]$SecureValue)

    if ($null -eq $SecureValue -or $SecureValue.Length -eq 0) {
        return ""
    }

    $bstr = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($SecureValue)
    try {
        return [Runtime.InteropServices.Marshal]::PtrToStringBSTR($bstr)
    } finally {
        [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($bstr)
    }
}

if ([string]::IsNullOrWhiteSpace($DbPassword)) {
    $securePassword = Read-Host "PostgreSQL password for user '$DbUser'" -AsSecureString
    $DbPassword = ConvertFrom-SecureStringToPlainText $securePassword
}

$env:DB_URL = $DbUrl
$env:DB_USER = $DbUser
$env:DB_PASSWORD = $DbPassword

Write-Host "Using database URL: $DbUrl"
Write-Host "Using database user: $DbUser"

if (-not $SkipPackage) {
    mvn "-Dmaven.test.skip=true" package
    if ($LASTEXITCODE -ne 0) {
        exit $LASTEXITCODE
    }
}

if (-not $SkipConnectionTest) {
    mvn exec:java "-Dexec.mainClass=com.foodordering.util.DbConnectionTest"
    if ($LASTEXITCODE -ne 0) {
        exit $LASTEXITCODE
    }
}

mvn exec:java
exit $LASTEXITCODE
