@echo off
echo Setting up Maven Wrapper...

REM Download Maven Wrapper files
powershell -Command "Invoke-WebRequest -Uri 'https://raw.githubusercontent.com/takari/maven-wrapper/master/mvnw.cmd' -OutFile 'mvnw.cmd'"
powershell -Command "Invoke-WebRequest -Uri 'https://raw.githubusercontent.com/takari/maven-wrapper/master/mvnw' -OutFile 'mvnw'"

REM Create .mvn/wrapper directory if it doesn't exist
if not exist ".mvn\wrapper" mkdir ".mvn\wrapper"

REM Download Maven Wrapper JAR and properties
powershell -Command "Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar' -OutFile '.mvn/wrapper/maven-wrapper.jar'"
powershell -Command "Invoke-WebRequest -Uri 'https://raw.githubusercontent.com/takari/maven-wrapper/master/mvnw.properties' -OutFile '.mvn/wrapper/maven-wrapper.properties'"

REM Make the wrapper scripts executable
icacls mvnw.cmd /grant Everyone:RX
icacls mvnw /grant Everyone:RX

echo Maven Wrapper setup complete!
echo You can now run '.\mvnw.cmd spring-boot:run' to start the application. 