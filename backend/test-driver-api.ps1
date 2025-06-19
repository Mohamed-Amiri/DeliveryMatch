# Test script for Driver API functionality
Write-Host "========================================" -ForegroundColor Green
Write-Host "    Driver API Test Script" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""

# Configuration
$baseUrl = "http://localhost:8080"
$token = ""

function Test-Backend {
    try {
        $response = Invoke-WebRequest -Uri "$baseUrl/auth/test" -Method GET -ErrorAction Stop
        return $true
    }
    catch {
        return $false
    }
}

function Register-Driver {
    $body = @{
        firstName = "Jean"
        lastName = "Dupont"
        email = "jean.dupont@example.com"
        password = "password123"
        phone = "+33123456789"
        role = "DRIVER"
    } | ConvertTo-Json

    try {
        $response = Invoke-WebRequest -Uri "$baseUrl/auth/register" -Method POST -Body $body -ContentType "application/json"
        Write-Host "✅ Driver registration successful" -ForegroundColor Green
        return $true
    }
    catch {
        Write-Host "❌ Driver registration failed: $_" -ForegroundColor Red
        return $false
    }
}

function Login-Driver {
    $body = @{
        email = "jean.dupont@example.com"
        password = "password123"
    } | ConvertTo-Json

    try {
        $response = Invoke-WebRequest -Uri "$baseUrl/auth/login" -Method POST -Body $body -ContentType "application/json"
        $token = ($response.Content | ConvertFrom-Json).token
        Write-Host "✅ Driver login successful" -ForegroundColor Green
        return $token
    }
    catch {
        Write-Host "❌ Driver login failed: $_" -ForegroundColor Red
        return $null
    }
}

function Create-Trip {
    param($token)

    $body = @{
        departureLocation = "Paris, France"
        destinationLocation = "Lyon, France"
        intermediateStops = @("Dijon, France")
        departureTime = "2024-01-15T08:00:00"
        estimatedArrivalTime = "2024-01-15T14:00:00"
        maxLength = 200.0
        maxWidth = 150.0
        maxHeight = 100.0
        maxWeight = 500.0
        availableCapacity = 1000.0
        acceptedCargoTypes = @("Electronics", "Clothing", "Books")
        description = "Regular trip from Paris to Lyon with stop in Dijon"
        price = 2.50
    } | ConvertTo-Json

    try {
        $headers = @{
            "Authorization" = "Bearer $token"
            "Content-Type" = "application/json"
        }
        $response = Invoke-WebRequest -Uri "$baseUrl/api/driver/trips" -Method POST -Headers $headers -Body $body
        Write-Host "✅ Trip creation successful" -ForegroundColor Green
        return ($response.Content | ConvertFrom-Json).id
    }
    catch {
        Write-Host "❌ Trip creation failed: $_" -ForegroundColor Red
        return $null
    }
}

function Get-DriverTrips {
    param($token)

    try {
        $headers = @{
            "Authorization" = "Bearer $token"
        }
        $response = Invoke-WebRequest -Uri "$baseUrl/api/driver/trips" -Method GET -Headers $headers
        Write-Host "✅ Retrieved driver trips successfully" -ForegroundColor Green
        return $response.Content | ConvertFrom-Json
    }
    catch {
        Write-Host "❌ Failed to get driver trips: $_" -ForegroundColor Red
        return $null
    }
}

# Main test flow
Write-Host "🚀 Starting Driver API tests..." -ForegroundColor Cyan
Write-Host ""

# Check if backend is running
Write-Host "1️⃣ Checking if backend is running..."
if (-not (Test-Backend)) {
    Write-Host "❌ Backend is not running. Please start the backend first:" -ForegroundColor Red
    Write-Host "   1. Run 'setup-mvnw.bat' to set up Maven Wrapper" -ForegroundColor Yellow
    Write-Host "   2. Run '.\mvnw.cmd spring-boot:run' to start the backend" -ForegroundColor Yellow
    exit
}

# Register driver
Write-Host "`n2️⃣ Registering driver account..."
if (-not (Register-Driver)) {
    Write-Host "❌ Test sequence stopped due to registration failure" -ForegroundColor Red
    exit
}

# Login
Write-Host "`n3️⃣ Logging in driver..."
$token = Login-Driver
if (-not $token) {
    Write-Host "❌ Test sequence stopped due to login failure" -ForegroundColor Red
    exit
}

# Create trip
Write-Host "`n4️⃣ Creating a trip..."
$tripId = Create-Trip -token $token
if (-not $tripId) {
    Write-Host "❌ Test sequence stopped due to trip creation failure" -ForegroundColor Red
    exit
}

# Get trips
Write-Host "`n5️⃣ Getting driver trips..."
$trips = Get-DriverTrips -token $token

Write-Host "`n========================================" -ForegroundColor Green
Write-Host "    Test Sequence Complete!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host "`nℹ️ JWT Token for manual testing:" -ForegroundColor Cyan
Write-Host $token
Write-Host "`nℹ️ Created Trip ID: $tripId" -ForegroundColor Cyan
Write-Host "`nYou can now use these in Postman for further testing!" 