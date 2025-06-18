# DeliveryMatch Backend

A Spring Boot application for the DeliveryMatch platform with JWT authentication and user management.

## 🚀 Quick Start

### Prerequisites
- Java 17 or later
- MySQL 8.0 or later
- Maven (optional - Maven wrapper included)

### Database Setup
1. Install MySQL if not already installed
2. Create a database (or let the application create it automatically)
3. Update database credentials in `src/main/resources/application.yml` if needed

### Starting the Application

#### Option 1: Using the provided script (Recommended)
```bash
# Windows
start-app.bat

# PowerShell
.\start-app.ps1
```

#### Option 2: Using Maven wrapper
```bash
# Windows
mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

#### Option 3: Using Maven directly
```bash
mvn spring-boot:run
```

### Application URLs
- **Main API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs

## 📋 API Endpoints

### Public Endpoints (No Authentication Required)
- `GET /users/test` - Health check
- `GET /users/debug-users` - Check user count
- `GET /users/profile-simple` - Simple profile test
- `POST /auth/register` - Register new user
- `POST /auth/login` - Login user

### Protected Endpoints (Authentication Required)
- `GET /users/profile` - Get user profile
- `PUT /users/profile` - Update user profile
- `POST /users/change-password` - Change password
- `POST /users/deactivate` - Deactivate account
- `POST /users/reactivate` - Reactivate account
- `GET /users/debug-auth` - Debug authentication

## 🧪 Testing with Postman

1. **Import the Postman Collection**
   - Open Postman
   - Import `DeliveryMatch_API_Tests.postman_collection.json`

2. **Test Sequence**
   ```
   1. Test Controller (GET /users/test)
   2. Register User (POST /auth/register)
   3. Login User (POST /auth/login)
   4. Copy JWT token from login response
   5. Set token in collection variable {{jwt_token}}
   6. Test protected endpoints
   ```

3. **Sample Requests**

#### Register User
```bash
POST http://localhost:8080/auth/register
Content-Type: application/json

{
    "firstName": "Test",
    "lastName": "User",
    "email": "test123@example.com",
    "password": "password123",
    "phoneNumber": "1234567890",
    "address": "123 Test St",
    "city": "Test City",
    "postalCode": "12345",
    "country": "Test Country"
}
```

#### Login
```bash
POST http://localhost:8080/auth/login
Content-Type: application/json

{
    "email": "test123@example.com",
    "password": "password123"
}
```

#### Get Profile (with token)
```bash
GET http://localhost:8080/users/profile
Authorization: Bearer YOUR_JWT_TOKEN_HERE
```

## 🔧 Configuration

### Database Configuration
Update `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/deliverymatch?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
    username: root
    password: admin
```

### JWT Configuration
```yaml
spring:
  security:
    jwt:
      secret: YOUR_JWT_SECRET_HERE
      expiration: 86400000 # 24 hours
```

## 🐛 Troubleshooting

### Common Issues

1. **Port 8080 already in use**
   - Change port in `application.yml`
   - Or kill the process using port 8080

2. **Database connection failed**
   - Check MySQL is running
   - Verify database credentials
   - Ensure database exists

3. **Maven wrapper not working**
   - Use `start-app.bat` instead
   - Or install Maven globally

4. **404 errors**
   - Ensure application is running
   - Check URL paths (no `/api` prefix needed)
   - Verify endpoint exists

### Logs
Check application logs for detailed error information. Logging is configured to show:
- SQL queries
- Security events
- HTTP requests
- Application debug info

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/deliverymatch/
│   │   ├── config/          # Configuration classes
│   │   ├── controller/      # REST controllers
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── entity/         # JPA entities
│   │   ├── exception/      # Exception handlers
│   │   ├── repository/     # Data repositories
│   │   ├── security/       # Security configuration
│   │   └── service/        # Business logic
│   └── resources/
│       └── application.yml # Application configuration
└── test/                   # Test files
```

## 🔒 Security Features

- JWT-based authentication
- Password encryption
- Role-based access control
- CORS configuration
- Stateless sessions

## 📝 API Documentation

Once the application is running, visit:
- http://localhost:8080/swagger-ui.html

For OpenAPI specification:
- http://localhost:8080/api-docs 