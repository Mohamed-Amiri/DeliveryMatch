@echo off
echo ========================================
echo Starting DeliveryMatch Backend
echo ========================================
echo.

REM Check if Java is installed
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 17 or later
    pause
    exit /b 1
)

echo Java found. Starting application...
echo.
echo Application will be available at:
echo - Main API: http://localhost:8080
echo - Swagger UI: http://localhost:8080/swagger-ui.html
echo.
echo Press Ctrl+C to stop the application
echo.

REM Try to start with Maven wrapper
if exist "mvnw.cmd" (
    echo Using Maven wrapper...
    call mvnw.cmd spring-boot:run
) else (
    echo Maven wrapper not found. Trying direct Maven...
    mvn spring-boot:run
)

pause 