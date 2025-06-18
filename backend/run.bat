@echo off
echo Starting Spring Boot Application...
echo.

REM Check if Java is installed
java -version >nul 2>&1
if errorlevel 1 (
    echo Error: Java is not installed or not in PATH
    echo Please install Java 17 or later
    pause
    exit /b 1
)

REM Check if target directory exists
if not exist "target" (
    echo Building the application first...
    echo This might take a few minutes...
    echo.
    
    REM Try to use Maven if available
    mvn clean compile -q
    if errorlevel 1 (
        echo Maven not found, trying to compile with Java directly...
        echo Please make sure you have Maven installed or use an IDE
        pause
        exit /b 1
    )
)

REM Run the application
echo Starting the application...
echo.
java -jar target/deliverymatch-0.0.1-SNAPSHOT.jar

pause 