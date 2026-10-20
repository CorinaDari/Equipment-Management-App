
ALTER TABLE users
    DROP CONSTRAINT chk_users_role;

ALTER TABLE users
    ADD CONSTRAINT chk_users_role
    CHECK (role IN ('ADMIN', 'MANAGER', 'MECHANIC', 'OPERATOR'));


INSERT INTO users
    (department_id, full_name, email, password_hash, role, active)
VALUES
    (
        (SELECT department_id FROM department WHERE name = 'Mechanization'),
        'James Wilson',
        'james.wilson@example.com',
        '$2a$12$bOOyo9Bx1zhdNx4qQha2iOkGC/272/TPnCHBvTFYNSWT7YYyszaJq',
        'ADMIN',
        TRUE
    ),
    (
        (SELECT department_id FROM department WHERE name = 'Mechanization'),
        'Michael Brown',
        'michael.brown@example.com',
        '$2a$12$b1MO8qVZrad3w1uLF0ZztO6zEERTLIfjmroYwJMw80Aawnr0S1/kS',
        'MECHANIC',
        TRUE
    ),
    (
        (SELECT department_id FROM department WHERE name = 'Agriculture'),
        'Emily Davis',
        'emily.davis@example.com',
        '$2a$12$pg2o4/esP8IhESXBjHmOw.eD4beQdjeiB/PdGClAZZrDedu7Tv.iu',
        'OPERATOR',
        TRUE
    ),
    (
        (SELECT department_id FROM department WHERE name = 'Agriculture'),
        'Sarah Miller',
        'sarah.miller@example.com',
        '$2a$12$FBVN3t/AA1PE1W085RBgF.I4quueagU9kxCIKPvEnLeWSIJozJ6.y',
        'MANAGER',
        TRUE
    ),
    (
        (SELECT department_id FROM department WHERE name = 'Mechanization'),
        'David Anderson',
        'david.anderson@example.com',
        '$2a$12$bgyNkjQRsMMXro003J2RjeXvmV1Ss2cg./pNhNkQlyhtEVatwNzLe',
        'ADMIN',
        TRUE
    ),
    (
        (SELECT department_id FROM department WHERE name = 'Agriculture'),
        'Thomas Moore',
        'thomas.moore@example.com',
        '$2a$12$EhFoNXf5X1b9EudXHxhcR.b68nL5iCxP60h35Yi0AEXfzD3BxzVdK',
        'MANAGER',
        TRUE
    ),
    (
        (SELECT department_id FROM department WHERE name = 'Mechanization'),
        'Olivia Martin',
        'olivia.martin@example.com',
        '$2a$12$5hSyL8rJCUt2VriNxXzjEOZMTRQDL7jMPqNy2au.v68Is2NrEUYw2',
        'MANAGER',
        TRUE
    ),
    (
        (SELECT department_id FROM department WHERE name = 'Agriculture'),
        'Daniel Taylor',
        'daniel.taylor@example.com',
        '$2a$12$Bi7EAmSgswKg.nd80npUnuVXksM95ixc89XDBndz.ylgYjPbXtJs2',
        'MECHANIC',
        FALSE
    ),
    (
        (SELECT department_id FROM department WHERE name = 'Mechanization'),
        'Henry White',
        'henry.white@example.com',
        '$2a$12$RqUrdjuXgtD.DlENTtNm7OnvAaID6q1TX45ZW7YNt2vXIAgb89qI.',
        'MECHANIC',
        TRUE
    ),
    (
        (SELECT department_id FROM department WHERE name = 'Agriculture'),
        'Peter Harris',
        'peter.harris@example.com',
        '$2a$12$mqEBPTVbvoK9JRQ6iOtJX.v3IFpcDbE294U4wj2ingIJFD3KX6Nui',
        'MECHANIC',
        TRUE
    ),
    (
        (SELECT department_id FROM department WHERE name = 'Agriculture'),
        'Robert Clark',
        'robert.clark@example.com',
        '$2a$12$M3KAzvnKJ0AosJbhfQ2ywuyxx1Jtc5vr.ZX10vU5XazG5aSnuCc6C',
        'OPERATOR',
        TRUE
    ),
    (
        (SELECT department_id FROM department WHERE name = 'Mechanization'),
        'Emma Lewis',
        'emma.lewis@example.com',
        '$2a$12$WS32a7TQPGN.ZaHNmEJmD.ERPRWG3QpXlcXlaugEzP2bXMWGUdgou',
        'OPERATOR',
        TRUE
    ),
    (
        (SELECT department_id FROM department WHERE name = 'Agriculture'),
        'Noah Walker',
        'noah.walker@example.com',
        '$2a$12$El.f9ztuWv2gS8n0X0H/9OkbMxBn.w4WuG8U3yMzVu40xZGnmoo/2',
        'OPERATOR',
        TRUE
    ),
    (
        (SELECT department_id FROM department WHERE name = 'Mechanization'),
        'Sophia Hall',
        'sophia.hall@example.com',
        '$2a$12$lKxyxlSk8L.5EEictl6QaeT5WCb7BzWOMeT/sD9tmgrty76CUw66y',
        'OPERATOR',
        TRUE
    ),
    (
        (SELECT department_id FROM department WHERE name = 'Agriculture'),
        'Liam Allen',
        'liam.allen@example.com',
        '$2a$12$PKEX5.JIsBt.7IXf3WaYRurBW0NTR1WMpX8KAb7R5YHdnwvPo4Igy',
        'OPERATOR',
        TRUE
    ),
    (
        (SELECT department_id FROM department WHERE name = 'Mechanization'),
        'Isabella Young',
        'isabella.young@example.com',
        '$2a$12$IdOXEt70Yws.by/Sw0x40..olYqRYsY8O25yfULE4y.RbQHe0vId6',
        'OPERATOR',
        TRUE
    );
INSERT INTO equipment (
    equipment_category_id,
    location_id,
    name,
    serial_number,
    manufacturer,
    model,
    status,
    acquisition_date,
    price,
    engine_hours
)
VALUES
    (
        (SELECT equipment_category_id FROM equipment_category WHERE name = 'Tractors'),
        (SELECT location_id FROM location WHERE name = 'North Hangar'),
        'Agricultural Tractor',
        'TR-1001',
        'John Deere',
        '6120M',
        'ASSIGNED',
        '2023-03-15',
        85000.00,
        1240.5
    ),
    (
        (SELECT equipment_category_id FROM equipment_category WHERE name = 'Harvesters'),
        (SELECT location_id FROM location WHERE name = 'Field 12'),
        'Combine Harvester',
        'HV-2001',
        'CLAAS',
        'Lexion 6700',
        'AVAILABLE',
        '2022-08-10',
        245000.00,
        890.0
    ),
    (
        (SELECT equipment_category_id FROM equipment_category WHERE name = 'Irrigation'),
        (SELECT location_id FROM location WHERE name = 'Central Workshop'),
        'Irrigation Pump',
        'IR-3001',
        'Grundfos',
        'CR 64',
        'IN_MAINTENANCE',
        '2024-02-20',
        12500.00,
        320.0
    ),
    (
        (SELECT equipment_category_id FROM equipment_category WHERE name = 'Tractors'),
        (SELECT location_id FROM location WHERE name = 'Central Workshop'),
        'Utility Tractor',
        'TR-1002',
        'New Holland',
        'T5.110',
        'IN_REPAIR',
        '2021-06-05',
        62000.00,
        2310.5
    ),
    (
        (SELECT equipment_category_id FROM equipment_category WHERE name = 'Tractors'),
        (SELECT location_id FROM location WHERE name = 'North Hangar'),
        'Old Farm Tractor',
        'TR-1003',
        'Massey Ferguson',
        '5711',
        'RETIRED',
        '2015-04-12',
        38000.00,
        6840.0
    );

INSERT INTO assignment (
    equipment_id,
    user_id,
    start_date,
    end_date,
    notes
)
VALUES
    (
        (SELECT equipment_id FROM equipment WHERE serial_number = 'TR-1001'),
        (SELECT user_id FROM users WHERE email = 'emily.davis@example.com'),
        '2026-09-01 08:00:00+00',
        NULL,
        'Assigned for daily field operations'
    ),
    (
        (SELECT equipment_id FROM equipment WHERE serial_number = 'HV-2001'),
        (SELECT user_id FROM users WHERE email = 'emily.davis@example.com'),
        '2026-07-01 07:00:00+00',
        '2026-08-15 16:00:00+00',
        'Harvesting season assignment'
    ),
    (
        (SELECT equipment_id FROM equipment WHERE serial_number = 'TR-1003'),
        (SELECT user_id FROM users WHERE email = 'james.wilson@example.com'),
        '2025-01-10 09:00:00+00',
        '2025-06-30 17:00:00+00',
        'Temporary assignment before retirement'
    );
INSERT INTO repair_request (
    equipment_id,
    reported_by,
    technician_id,
    description,
    priority,
    status,
    reported_at,
    resolved_at,
    cost
)
VALUES
    (
        (SELECT equipment_id FROM equipment WHERE serial_number = 'TR-1002'),
        (SELECT user_id FROM users WHERE email = 'emily.davis@example.com'),
        (SELECT user_id FROM users WHERE email = 'michael.brown@example.com'),
        'Engine overheating during field operations',
        'HIGH',
        'IN_PROGRESS',
        '2026-10-05 09:00:00+00',
        NULL,
        NULL
    ),
    (
        (SELECT equipment_id FROM equipment WHERE serial_number = 'IR-3001'),
        (SELECT user_id FROM users WHERE email = 'emily.davis@example.com'),
        (SELECT user_id FROM users WHERE email = 'michael.brown@example.com'),
        'Water pressure below the required level',
        'MEDIUM',
        'OPEN',
        '2026-10-08 10:30:00+00',
        NULL,
        NULL
    ),
    (
        (SELECT equipment_id FROM equipment WHERE serial_number = 'HV-2001'),
        (SELECT user_id FROM users WHERE email = 'james.wilson@example.com'),
        (SELECT user_id FROM users WHERE email = 'michael.brown@example.com'),
        'Damaged cutting blade',
        'CRITICAL',
        'RESOLVED',
        '2026-09-10 08:00:00+00',
        '2026-09-12 14:00:00+00',
        1850.00
    ),
    (
        (SELECT equipment_id FROM equipment WHERE serial_number = 'TR-1001'),
        (SELECT user_id FROM users WHERE email = 'emily.davis@example.com'),
        NULL,
        'Minor oil leak detected during inspection',
        'LOW',
        'CLOSED',
        '2026-08-20 11:00:00+00',
        '2026-08-22 15:00:00+00',
        250.00
    );
INSERT INTO maintenance_record (
    equipment_id,
    technician_id,
    type,
    performed_at,
    engine_hours_at_service,
    description,
    cost
)
VALUES
    (
        (SELECT equipment_id FROM equipment WHERE serial_number = 'TR-1001'),
        (SELECT user_id FROM users WHERE email = 'michael.brown@example.com'),
        'ROUTINE',
        '2026-06-15',
        1100.0,
        'Engine oil and filter replacement',
        320.00
    ),
    (
        (SELECT equipment_id FROM equipment WHERE serial_number = 'HV-2001'),
        (SELECT user_id FROM users WHERE email = 'michael.brown@example.com'),
        'PREVENTIVE',
        '2026-07-20',
        850.0,
        'Pre-season inspection and belt replacement',
        1250.00
    ),
    (
        (SELECT equipment_id FROM equipment WHERE serial_number = 'IR-3001'),
        (SELECT user_id FROM users WHERE email = 'michael.brown@example.com'),
        'INSPECTION',
        '2026-09-25',
        300.0,
        'Pump pressure and seal inspection',
        180.00
    );