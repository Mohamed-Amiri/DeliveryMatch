<<<<<<< HEAD
<<<<<<< HEAD
# DeliveryMatch
DeliveryMatch est une application web mettant en relation des conducteurs avec des expéditeurs de colis dans une logique de co-transport collaboratif. L’application vise à optimiser les trajets, réduire les coûts de transport et limiter l’impact environnemental.
=======
=======
>>>>>>> 24d585f5a812e39afc66507076feff13ebe4dd43
# DeliveryMatch Platform

A platform connecting drivers and shippers for efficient delivery services.

## Technology Stack

### Backend
- Spring Boot 3.x
- Spring Security
- Spring Data JPA
- MySQL/PostgreSQL
- Maven
- Java 17+

### Frontend
- Angular 16+
- Angular Material
- Bootstrap
- Chart.js
- TypeScript
- RxJS

## Project Structure

```
deliverymatch/
├── backend/                 # Spring Boot Backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/deliverymatch/
│   │   │   │       ├── config/         # Configuration classes
│   │   │   │       ├── controller/     # REST controllers
│   │   │   │       ├── dto/            # Data Transfer Objects
│   │   │   │       ├── entity/         # JPA entities
│   │   │   │       ├── repository/     # JPA repositories
│   │   │   │       ├── service/        # Business logic
│   │   │   │       ├── security/       # Security configuration
│   │   │   │       └── exception/      # Custom exceptions
│   │   │   └── resources/
│   │   │       └── application.yml     # Application properties
│   │   └── test/                       # Test classes
│   └── pom.xml                         # Maven configuration
│
├── frontend/               # Angular Frontend
│   ├── src/
│   │   ├── app/
│   │   │   ├── core/                   # Core module
│   │   │   ├── shared/                 # Shared module
│   │   │   ├── features/               # Feature modules
│   │   │   │   ├── auth/              # Authentication
│   │   │   │   ├── driver/            # Driver features
│   │   │   │   ├── shipper/           # Shipper features
│   │   │   │   └── admin/             # Admin features
│   │   │   ├── services/              # Angular services
│   │   │   └── models/                # TypeScript interfaces
│   │   ├── assets/                    # Static assets
│   │   └── environments/              # Environment configurations
│   ├── angular.json                   # Angular configuration
│   └── package.json                   # NPM dependencies
│
└── README.md
```

## Features

### User Management
- User registration and authentication
- Profile management
- Role-based access control (User, Driver, Shipper, Admin)

### Driver Features
- Create and manage delivery listings
- View and respond to shipping requests
- Track delivery history
- Manage vehicle capacity and dimensions

### Shipper Features
- Search for available deliveries
- Create shipping requests
- Track shipment status
- View shipping history

### Admin Features
- User management
- Content moderation
- Analytics dashboard
- Platform statistics

## Getting Started

### Prerequisites
- Java 17 or higher
- Node.js 16 or higher
- Angular CLI
- Maven
- MySQL/PostgreSQL

### Backend Setup
1. Navigate to the backend directory
2. Configure database connection in `application.yml`
3. Run `mvn spring-boot:run`

### Frontend Setup
1. Navigate to the frontend directory
2. Run `npm install`
3. Run `ng serve`

## Security
- JWT-based authentication
- Password encryption
- Role-based authorization
- Input validation
- CORS configuration

## API Documentation
- RESTful API endpoints
- Swagger/OpenAPI documentation
- API versioning

## Testing
- Unit tests
- Integration tests
- E2E tests
- API tests

## Deployment
- Docker support
- CI/CD pipeline configuration
<<<<<<< HEAD
- Environment-specific configurations 
>>>>>>> 24d585f (Crée authentification)
=======
- Environment-specific configurations 
>>>>>>> 24d585f5a812e39afc66507076feff13ebe4dd43
