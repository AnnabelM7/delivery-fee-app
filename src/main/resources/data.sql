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


MERGE INTO extra_fees (id, fee_type, vehicle_type, min_value, max_value, phenomenon, fee, forbidden)
    KEY(fee_type, vehicle_type, min_value, max_value, phenomenon)
    VALUES
    -- ATEF: Scooter
    (random_uuid(), 'ATEF', 'SCOOTER', null, -10.0, null, 1.0, false),
    (random_uuid(), 'ATEF', 'SCOOTER', -10.0, 0.0,  null, 0.5, false),
    -- ATEF: Bike
    (random_uuid(), 'ATEF', 'BIKE',    null, -10.0, null, 1.0, false),
    (random_uuid(), 'ATEF', 'BIKE',    -10.0, 0.0,  null, 0.5, false),
    -- WSEF: Bike
    (random_uuid(), 'WSEF', 'BIKE',    10.0, 20.0,  null, 0.5,  false),
    (random_uuid(), 'WSEF', 'BIKE',    20.0, null,  null, null, true),
    -- WPEF: Scooter
    (random_uuid(), 'WPEF', 'SCOOTER', null, null, 'snow',    1.0,  false),
    (random_uuid(), 'WPEF', 'SCOOTER', null, null, 'sleet',   1.0,  false),
    (random_uuid(), 'WPEF', 'SCOOTER', null, null, 'rain',    0.5,  false),
    (random_uuid(), 'WPEF', 'SCOOTER', null, null, 'glaze',   null, true),
    (random_uuid(), 'WPEF', 'SCOOTER', null, null, 'hail',    null, true),
    (random_uuid(), 'WPEF', 'SCOOTER', null, null, 'thunder', null, true),
    -- WPEF: Bike
    (random_uuid(), 'WPEF', 'BIKE',    null, null, 'snow',    1.0,  false),
    (random_uuid(), 'WPEF', 'BIKE',    null, null, 'sleet',   1.0,  false),
    (random_uuid(), 'WPEF', 'BIKE',    null, null, 'rain',    0.5,  false),
    (random_uuid(), 'WPEF', 'BIKE',    null, null, 'glaze',   null, true),
    (random_uuid(), 'WPEF', 'BIKE',    null, null, 'hail',    null, true),
    (random_uuid(), 'WPEF', 'BIKE',    null, null, 'thunder', null, true);