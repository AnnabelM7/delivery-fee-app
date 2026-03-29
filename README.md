# Delivery Fee App
A Spring Boot application that calculates delivery fees for food couriers based on regional base fees, vehicle type, and weather conditions. Weather data is automatically imported from the Estonian Environment Agency.

## Technologies
- Java 21
- Spring Boot 4
- H2 Database (file-based)
- Docker

## How to Run the Application

### Running Locally
1. Clone the repository
```bash
   git clone https://github.com/AnnabelM7/delivery-fee-app.git
```
2. Open the project in an IDE (e.g. IntelliJ IDEA)
3. Run `DeliveryApplication.java`
4. The application will start at `http://localhost:8080`
5. Use Postman or a browser to test the API endpoints

### Running with Docker
1. Clone the repository
```bash
   git clone https://github.com/AnnabelM7/delivery-fee-app.git
```
2. Open a terminal in the project root
3. Run:
```bash
   docker-compose up -d
```
4. The application will start at `http://localhost:8080`
5. To stop:
```bash
   docker-compose down
```

## Project Structure
```
src/
├── main/
│   ├── java/com/example/delivery/
│   │   ├── controller/         # REST controllers
│   │   │   ├── BaseFeeController.java
│   │   │   ├── DeliveryFeeController.java
│   │   │   ├── ExtraFeeController.java
│   │   │   └── WeatherController.java
│   │   ├── entity/             # JPA entities
│   │   │   ├── BaseFee.java
│   │   │   ├── ExtraFee.java
│   │   │   └── Weather.java
│   │   ├── enums/              # Enums
│   │   │   ├── City.java
│   │   │   ├── FeeType.java  
│   │   │   └── VehicleType.java
│   │   ├── exception/          # Custom exceptions
│   │   │   ├── ForbiddenVehicleException.java
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   └── ResourceNotFoundException.java
│   │   ├── repository/         # JPA repositories
│   │   │   ├── BaseFeeRepository.java
│   │   │   ├── ExtraFeeRepository.java
│   │   │   └── WeatherRepository.java
│   │   └── service/            # Business logic
│   │       ├── BaseFeeService.java
│   │       ├── DeliveryFeeService.java
│   │       ├── ExtraFeeService.java
│   │       ├── WeatherImportService.java
│   │       └── WeatherService.java
│   └── resources/
│       ├── application.properties
│       └── data.sql            # Initial base fee data
└── test/                       # Unit tests
```

---

## API Endpoints

### Calculate Delivery Fee

**GET** `/api/delivery-fee`

| Parameter   | Type   | Required | Description                             |
|-------------|--------|----------|-----------------------------------------|
| city        | String | Yes      | `TALLINN`, `TARTU`, `PARNU`             |
| vehicleType | String | Yes      | `CAR`, `SCOOTER`, `BIKE`                |
| datetime    | String | No       | ISO 8601 format: `yyyy-MM-ddTHH:mm:ss`  |

**Examples:**
```http
GET /api/delivery-fee?city=TARTU&vehicleType=BIKE
GET /api/delivery-fee?city=PARNU&vehicleType=BIKE&datetime=2026-03-28T12:09:03
```

**Response 200 OK:**
```json
{
  "city": "TARTU",
  "vehicleType": "BIKE",
  "fee": 2.5
}
```

**Response 422 — vehicle type forbidden:**
```json
{
  "error": "Usage of selected vehicle type is forbidden",
  "timestamp": "2026-03-28T12:09:03"
}
```

**Response 404 — no weather data:**
```json
{
  "error": "No weather data found for station: Tartu-Tõravere",
  "timestamp": "2026-03-28T12:09:03"
}
```

---

### Base Fee CRUD

| Method | Endpoint              | Description           |
|--------|-----------------------|-----------------------|
| GET    | `/api/base-fees`      | Get all base fees     |
| GET    | `/api/base-fees/{id}` | Get base fee by ID    |
| POST   | `/api/base-fees`      | Create new base fee   |
| PUT    | `/api/base-fees/{id}` | Update base fee by ID |
| DELETE | `/api/base-fees/{id}` | Delete base fee       |

**POST/PUT request body:**
```json
{
  "city": "TARTU",
  "vehicleType": "BIKE",
  "fee": 2.5
}
```

---

### Weather

| Method | Endpoint            | Description                  |
|--------|---------------------|------------------------------|
| GET    | `/api/weather`      | Get all weather observations |
| GET    | `/api/weather/{id}` | Get observation by ID        |


### Extra Fee Rules CRUD

| Method | Endpoint               | Description                |
|--------|------------------------|----------------------------|
| GET    | `/api/extra-fees`      | Get all extra fee rules    |
| GET    | `/api/extra-fees/{id}` | Get extra fee rule by ID   |
| POST   | `/api/extra-fees`      | Create new extra fee rule  |
| PUT    | `/api/extra-fees/{id}` | Update extra fee rule      |
| DELETE | `/api/extra-fees/{id}` | Delete extra fee rule      |

**POST/PUT request body:**
```json
{
  "feeType": "WPEF",
  "vehicleType": "BIKE",
  "phenomenon": "rain",
  "fee": 0.5,
  "forbidden": false
}
```

---

## Weather Import
Weather data is automatically imported from the [Estonian Environment Agency](https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php) for three stations:
- Tallinn-Harku (Tallinn)
- Tartu-Tõravere (Tartu)
- Pärnu (Pärnu)

Import runs every hour at HH:15:00 and also on application startup. The schedule is configurable via `weather.import.cron` in `application.properties`.

---

## Fee Calculation Logic

Total fee = RBF + ATEF + WSEF + WPEF

| Fee  | Description                              | Applies to      |
|------|------------------------------------------|-----------------|
| RBF  | Regional base fee based on city + vehicle | All vehicles    |
| ATEF | Extra fee for low air temperature        | Scooter, Bike   |
| WSEF | Extra fee for high wind speed            | Bike only       |
| WPEF | Extra fee for weather phenomenon         | Scooter, Bike   |

### Regional Base Fee (RBF)

| City    | Car  | Scooter | Bike |
|---------|------|---------|------|
| Tallinn | 4.00 | 3.50    | 3.00 |
| Tartu   | 3.50 | 3.00    | 2.50 |
| Pärnu   | 3.00 | 2.50    | 2.00 |

### Air Temperature Extra Fee (ATEF) — Scooter, Bike
| Condition          | Fee   |
|--------------------|-------|
| temp < -10°C       | 1.00€ |
| -10°C ≤ temp ≤ 0°C | 0.50€ |

### Wind Speed Extra Fee (WSEF) — Bike only
| Condition           | Fee               |
|---------------------|-------------------|
| 10 m/s ≤ wind ≤ 20 m/s | 0.50€          |
| wind > 20 m/s       |  Forbidden        |

### Weather Phenomenon Extra Fee (WPEF) — Scooter, Bike
| Condition                      | Fee              |
|--------------------------------|------------------|
| Snow or sleet                  | 1.00€            |
| Rain                           | 0.50€            |
| Glaze, hail, or thunder        | Forbidden        |
## AI Usage
Claude (Anthropic) was used as a development assistant during this project — helping with code structure, debugging, and best practices. All code was reviewed and understood before use.