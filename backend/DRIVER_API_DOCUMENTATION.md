# 🚛 Driver API Documentation

## Overview
This document describes the complete Driver (Conducteur) functionality for the DeliveryMatch platform. Drivers can publish trip announcements, manage shipment requests, and track their delivery history.

## 🔐 Authentication
All driver endpoints require JWT authentication. Include the token in the Authorization header:
```
Authorization: Bearer <your_jwt_token>
```

## 📋 API Endpoints

### 1. Trip Management

#### Create Trip Announcement
**POST** `/api/driver/trips`

Creates a new trip announcement with departure, destination, capacity, and pricing information.

**Request Body:**
```json
{
  "departureLocation": "Paris, France",
  "destinationLocation": "Lyon, France",
  "intermediateStops": ["Dijon, France"],
  "departureTime": "2024-01-15T08:00:00",
  "estimatedArrivalTime": "2024-01-15T14:00:00",
  "maxLength": 200.0,
  "maxWidth": 150.0,
  "maxHeight": 100.0,
  "maxWeight": 500.0,
  "availableCapacity": 1000.0,
  "acceptedCargoTypes": ["Electronics", "Clothing", "Books"],
  "description": "Regular trip from Paris to Lyon with stop in Dijon",
  "price": 2.50
}
```

**Response:**
```json
{
  "id": 1,
  "driverId": 1,
  "driverName": "Jean Dupont",
  "departureLocation": "Paris, France",
  "destinationLocation": "Lyon, France",
  "intermediateStops": ["Dijon, France"],
  "departureTime": "2024-01-15T08:00:00",
  "estimatedArrivalTime": "2024-01-15T14:00:00",
  "maxLength": 200.0,
  "maxWidth": 150.0,
  "maxHeight": 100.0,
  "maxWeight": 500.0,
  "availableCapacity": 1000.0,
  "acceptedCargoTypes": ["Electronics", "Clothing", "Books"],
  "description": "Regular trip from Paris to Lyon with stop in Dijon",
  "status": "ACTIVE",
  "price": 2.50,
  "createdAt": "2024-01-10T10:00:00",
  "updatedAt": "2024-01-10T10:00:00",
  "pendingRequestsCount": 0,
  "acceptedRequestsCount": 0
}
```

#### Get Driver Trips
**GET** `/api/driver/trips?page=0&size=10`

Retrieves paginated list of all trips for the authenticated driver.

**Query Parameters:**
- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 10)

#### Get Active Trips
**GET** `/api/driver/trips/active`

Retrieves all active trips for the driver.

#### Get Trip by ID
**GET** `/api/driver/trips/{tripId}`

Retrieves a specific trip by ID.

#### Update Trip Status
**PUT** `/api/driver/trips/{tripId}/status?status={status}`

Updates the status of a trip.

**Query Parameters:**
- `status`: One of `ACTIVE`, `IN_PROGRESS`, `COMPLETED`, `CANCELLED`

#### Delete Trip
**DELETE** `/api/driver/trips/{tripId}`

Deletes a trip (only if no accepted requests exist).

### 2. Shipment Request Management

#### Get Pending Requests
**GET** `/api/driver/requests?page=0&size=10`

Retrieves all pending shipment requests for the driver's trips.

#### Get Accepted Requests
**GET** `/api/driver/requests/accepted?page=0&size=10`

Retrieves all accepted shipment requests for the driver's trips.

#### Get Requests for Specific Trip
**GET** `/api/driver/trips/{tripId}/requests?page=0&size=10`

Retrieves all shipment requests for a specific trip.

#### Get Request by ID
**GET** `/api/driver/requests/{requestId}`

Retrieves a specific shipment request by ID.

#### Accept/Reject Request
**PUT** `/api/driver/requests/{requestId}/status`

Updates the status of a shipment request.

**Request Body:**
```json
{
  "status": "ACCEPTED"
}
```

Or for rejection:
```json
{
  "status": "REJECTED",
  "rejectionReason": "Capacity exceeded"
}
```

**Response:**
```json
{
  "id": 1,
  "shipperId": 2,
  "shipperName": "Marie Martin",
  "shipperEmail": "marie.martin@example.com",
  "tripId": 1,
  "pickupLocation": "Paris, France",
  "deliveryLocation": "Lyon, France",
  "pickupTime": "2024-01-15T09:00:00",
  "deliveryDeadline": "2024-01-15T16:00:00",
  "length": 50.0,
  "width": 30.0,
  "height": 20.0,
  "weight": 25.0,
  "cargoType": "Electronics",
  "description": "Laptop and accessories",
  "offeredPrice": 75.0,
  "status": "ACCEPTED",
  "rejectionReason": null,
  "createdAt": "2024-01-10T11:00:00",
  "updatedAt": "2024-01-10T12:00:00"
}
```

### 3. Trip History

#### Get Completed Trips
**GET** `/api/driver/trips/completed`

Retrieves all completed trips for the driver.

#### Get Completed Requests
**GET** `/api/driver/requests/completed`

Retrieves all completed shipment requests for the driver.

## 📊 Data Models

### Trip Status
- `ACTIVE`: Trip is available for requests
- `IN_PROGRESS`: Trip has started
- `COMPLETED`: Trip finished
- `CANCELLED`: Trip cancelled

### Request Status
- `PENDING`: Waiting for driver response
- `ACCEPTED`: Driver accepted the request
- `REJECTED`: Driver rejected the request
- `CANCELLED`: Shipper cancelled the request
- `COMPLETED`: Shipment completed

## 🔧 Business Rules

### Trip Management
1. Only the trip driver can update or delete their trips
2. Trips cannot be deleted if they have accepted shipment requests
3. Trip status can be updated to track progress
4. Available capacity is tracked in real-time

### Shipment Requests
1. Only the trip driver can accept/reject requests for their trips
2. Rejection requires a reason
3. Request status changes are tracked with timestamps
4. Drivers can view all requests for their trips

### Security
1. All endpoints require JWT authentication
2. Drivers can only access their own trips and requests
3. Proper authorization checks are enforced

## 🧪 Testing

Use the provided Postman collection `Driver_API_Tests.postman_collection.json` to test all endpoints.

### Test Flow:
1. Register a driver account
2. Login to get JWT token
3. Create a trip announcement
4. View trip details and requests
5. Accept/reject shipment requests
6. Update trip status
7. View trip history

## 📝 Error Handling

The API returns appropriate HTTP status codes:
- `200`: Success
- `201`: Created
- `400`: Bad Request
- `401`: Unauthorized
- `403`: Forbidden
- `404`: Not Found
- `500`: Internal Server Error

Error responses include detailed messages:
```json
{
  "timestamp": "2024-01-10T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Rejection reason is required when rejecting a request"
}
```

## 🚀 Getting Started

1. Ensure the backend is running on `http://localhost:8080`
2. Import the Postman collection
3. Set the `baseUrl` variable to `http://localhost:8080`
4. Register a driver account
5. Login and copy the JWT token to the `driverToken` variable
6. Start testing the endpoints!

## 📞 Support

For any issues or questions about the Driver API, please refer to the main project documentation or contact the development team. 