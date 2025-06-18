Write-Host "Starting Spring Boot Application..." -ForegroundColor Green
Write-Host ""

# Check if Java is installed
try {
    $javaVersion = java -version 2>&1
    Write-Host "Java found:" -ForegroundColor Yellow
    Write-Host $javaVersion[0] -ForegroundColor Cyan
} catch {
    Write-Host "Error: Java is not installed or not in PATH" -ForegroundColor Red
    Write-Host "Please install Java 17 or later" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

# Check if target directory exists
if (-not (Test-Path "target")) {
    Write-Host "Building the application first..." -ForegroundColor Yellow
    Write-Host "This might take a few minutes..." -ForegroundColor Yellow
    Write-Host ""
    
    # Try to use Maven if available
    try {
        mvn clean compile -q
        Write-Host "Build successful!" -ForegroundColor Green
    } catch {
        Write-Host "Maven not found, trying to compile with Java directly..." -ForegroundColor Yellow
        Write-Host "Please make sure you have Maven installed or use an IDE" -ForegroundColor Red
        Read-Host "Press Enter to exit"
        exit 1
    }
}

# Run the application
Write-Host "Starting the application..." -ForegroundColor Green
Write-Host "Application will be available at: http://localhost:8080" -ForegroundColor Cyan
Write-Host "Press Ctrl+C to stop the application" -ForegroundColor Yellow
Write-Host ""

java -jar target/deliverymatch-0.0.1-SNAPSHOT.jar 