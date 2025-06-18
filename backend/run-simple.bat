@echo off
echo ========================================
echo Starting DeliveryMatch Backend (Simple)
echo ========================================
echo.

REM Set JAVA_HOME if not set
if "%JAVA_HOME%"=="" (
    set JAVA_HOME=C:\Program Files\Common Files\Oracle\Java\javapath
    echo Set JAVA_HOME to: %JAVA_HOME%
)

REM Check if target directory exists
if not exist "target\classes" (
    echo Building application...
    echo This might take a few minutes...
    
    REM Try to compile with javac if Maven is not available
    if exist "src\main\java" (
        echo Compiling with Java...
        javac -cp "lib\*" -d target\classes src\main\java\com\deliverymatch\*.java
        if errorlevel 1 (
            echo Compilation failed. Please use an IDE or install Maven.
            pause
            exit /b 1
        )
    ) else (
        echo Source files not found. Please build with Maven first.
        pause
        exit /b 1
    )
)

echo Starting application...
echo Application will be available at: http://localhost:8080
echo Press Ctrl+C to stop
echo.

REM Try to run the main class directly
java -cp "target\classes;lib\*" com.deliverymatch.DeliveryMatchApplication

pause 