MERGE INTO base_fees (id, city, vehicle_type, fee)
    KEY(city, vehicle_type)
    VALUES
    (random_uuid(),'TALLINN','CAR',4.0),
    (random_uuid(),'TALLINN','SCOOTER',3.5),
    (random_uuid(),'TALLINN','BIKE',3.0),
    (random_uuid(),'TARTU','CAR',3.5),
    (random_uuid(),'TARTU','SCOOTER',3.0),
    (random_uuid(),'TARTU','BIKE',2.5),
    (random_uuid(),'PARNU','CAR',3.0),
    (random_uuid(),'PARNU','SCOOTER',2.5),
    (random_uuid(),'PARNU','BIKE',2.0);