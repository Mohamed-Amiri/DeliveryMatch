@echo off
echo ========================================
echo    Driver API Quick Test Script
echo ========================================
echo.

echo Starting Driver API tests...
echo.

REM Test 1: Register a driver
echo [1/8] Registering driver account...
curl -X POST http://localhost:8080/auth/register ^
  -H "Content-Type: application/json" ^
  -d "{\"firstName\":\"Jean\",\"lastName\":\"Dupont\",\"email\":\"jean.dupont@example.com\",\"password\":\"password123\",\"phone\":\"+33123456789\",\"role\":\"DRIVER\"}" ^
  -s | echo.

echo.
echo [2/8] Logging in driver...
curl -X POST http://localhost:8080/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"jean.dupont@example.com\",\"password\":\"password123\"}" ^
  -s | echo.

echo.
echo [3/8] Creating a trip announcement...
curl -X POST http://localhost:8080/api/driver/trips ^
  -H "Content-Type: application/json" ^
  -H "Authorization: Bearer YOUR_TOKEN_HERE" ^
  -d "{\"departureLocation\":\"Paris, France\",\"destinationLocation\":\"Lyon, France\",\"intermediateStops\":[\"Dijon, France\"],\"departureTime\":\"2024-01-15T08:00:00\",\"estimatedArrivalTime\":\"2024-01-15T14:00:00\",\"maxLength\":200.0,\"maxWidth\":150.0,\"maxHeight\":100.0,\"maxWeight\":500.0,\"availableCapacity\":1000.0,\"acceptedCargoTypes\":[\"Electronics\",\"Clothing\",\"Books\"],\"description\":\"Regular trip from Paris to Lyon with stop in Dijon\",\"price\":2.50}" ^
  -s | echo.

echo.
echo [4/8] Getting driver trips...
curl -X GET http://localhost:8080/api/driver/trips ^
  -H "Authorization: Bearer YOUR_TOKEN_HERE" ^
  -s | echo.

echo.
echo [5/8] Getting active trips...
curl -X GET http://localhost:8080/api/driver/trips/active ^
  -H "Authorization: Bearer YOUR_TOKEN_HERE" ^
  -s | echo.

echo.
echo [6/8] Getting pending requests...
curl -X GET http://localhost:8080/api/driver/requests ^
  -H "Authorization: Bearer YOUR_TOKEN_HERE" ^
  -s | echo.

echo.
echo [7/8] Getting completed trips...
curl -X GET http://localhost:8080/api/driver/trips/completed ^
  -H "Authorization: Bearer YOUR_TOKEN_HERE" ^
  -s | echo.

echo.
echo [8/8] Getting completed requests...
curl -X GET http://localhost:8080/api/driver/requests/completed ^
  -H "Authorization: Bearer YOUR_TOKEN_HERE" ^
  -s | echo.

echo.
echo ========================================
echo    Driver API Tests Completed!
echo ========================================
echo.
echo Note: Replace 'YOUR_TOKEN_HERE' with the actual JWT token
echo       obtained from the login response.
echo.
echo For complete testing, use the Postman collection:
echo Driver_API_Tests.postman_collection.json
echo.
pause 