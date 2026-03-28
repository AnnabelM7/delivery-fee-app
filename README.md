# delivery-fee-app

#### Parameters

| Parameter   | Type   | Required | Description                                      |
|-------------|--------|----------|--------------------------------------------------|
| city        | String | Yes      | City name: `TALLINN`, `TARTU`, `PARNU`           |
| vehicleType | String | Yes      | Vehicle type: `CAR`, `SCOOTER`, `BIKE`           |
| datetime    | String | No       | ISO 8601 format: `yyyy-MM-ddTHH:mm:ss`           |

#### Examples
```http
GET /api/delivery-fee?city=TARTU&vehicleType=BIKE
GET /api/delivery-fee?city=PARNU&vehicleType=BIKE&datetime=2026-03-28T12:09:03
```