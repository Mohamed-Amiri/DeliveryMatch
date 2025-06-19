@echo off
set JAVA_HOME=C:\Program Files\Java\jdk-23
set PATH=%JAVA_HOME%\bin;%PATH%

echo Creating directories...
mkdir src\main\java\com\deliverymatch 2>nul
mkdir target\classes 2>nul

echo Compiling the application...
javac -d target/classes src/main/java/com/deliverymatch/SimpleServer.java

if %ERRORLEVEL% NEQ 0 (
    echo Compilation failed!
    exit /b %ERRORLEVEL%
)

echo Starting the server...
java -cp target/classes com.deliverymatch.SimpleServer

pause 