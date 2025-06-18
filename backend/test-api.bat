@echo off
echo ========================================
echo DeliveryMatch API Test Script
echo ========================================
echo.

echo Testing basic connectivity...
curl -s http://localhost:8080/users/test
echo.
echo.

echo Testing user count...
curl -s http://localhost:8080/users/debug-users
echo.
echo.

echo ========================================
echo Manual Testing Instructions:
echo ========================================
echo.
echo 1. Register a user:
echo    POST http://localhost:8080/auth/register
echo    Content-Type: application/json
echo    {
echo        "firstName": "Test",
echo        "lastName": "User", 
echo        "email": "test123@example.com",
echo        "password": "password123",
echo        "phoneNumber": "1234567890",
echo        "address": "123 Test St",
echo        "city": "Test City",
echo        "postalCode": "12345",
echo        "country": "Test Country"
echo    }
echo.
echo 2. Login:
echo    POST http://localhost:8080/auth/login
echo    Content-Type: application/json
echo    {
echo        "email": "test123@example.com",
echo        "password": "password123"
echo    }
echo.
echo 3. Copy the JWT token from login response
echo.
echo 4. Test profile (with token):
echo    GET http://localhost:8080/users/profile
echo    Authorization: Bearer YOUR_JWT_TOKEN_HERE
echo.
echo ========================================
echo Use Postman or curl to test these endpoints
echo ========================================
echo.

pause 