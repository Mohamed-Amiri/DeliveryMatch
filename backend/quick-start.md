# Quick Start Guide - DeliveryMatch Backend

## 🚨 Current Issue
The application is having trouble starting due to Maven wrapper and JAVA_HOME configuration issues.

## 🔧 Solutions (Try in Order)

### Option 1: Install Maven Globally
1. Download Maven from: https://maven.apache.org/download.cgi
2. Extract to `C:\Program Files\Apache\maven`
3. Add to PATH: `C:\Program Files\Apache\maven\bin`
4. Run: `mvn spring-boot:run`

### Option 2: Use IDE (Recommended)
1. Open the project in IntelliJ IDEA or Eclipse
2. Import as Maven project
3. Run `DeliveryMatchApplication.java` directly

### Option 3: Set JAVA_HOME Manually
```cmd
set JAVA_HOME=C:\Program Files\Java\jdk-17
set PATH=%JAVA_HOME%\bin;%PATH%
mvnw.cmd spring-boot:run
```

### Option 4: Use Docker (If Available)
```bash
docker run -p 8080:8080 deliverymatch-backend
```

## 🧪 Testing Once Running

### 1. Health Check
```bash
curl http://localhost:8080/users/test
```
Expected: `"User controller is working!"`

### 2. Register User
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Test",
    "lastName": "User",
    "email": "test123@example.com",
    "password": "password123",
    "phoneNumber": "1234567890",
    "address": "123 Test St",
    "city": "Test City",
    "postalCode": "12345",
    "country": "Test Country"
  }'
```

### 3. Login
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test123@example.com",
    "password": "password123"
  }'
```

### 4. Get Profile (with token)
```bash
curl -X GET http://localhost:8080/users/profile \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

## 🔍 Troubleshooting

### JAVA_HOME Issues
- Check: `echo %JAVA_HOME%`
- Set: `set JAVA_HOME=C:\Program Files\Java\jdk-17`

### Port Issues
- Check if port 8080 is free: `netstat -an | findstr 8080`
- Kill process: `taskkill /f /im java.exe`

### Database Issues
- Ensure MySQL is running
- Check credentials in `application.yml`

## 📞 Need Help?
1. Try Option 2 (IDE) - most reliable
2. Check application logs for specific errors
3. Ensure all prerequisites are installed 