--TABELE

CREATE TABLE department (
    department_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name          VARCHAR(100) NOT NULL UNIQUE,
    description   VARCHAR(255)
);

CREATE TABLE users (
    user_id       BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    department_id BIGINT       NOT NULL REFERENCES department (department_id) ON DELETE RESTRICT,
    full_name     VARCHAR(150) NOT NULL,
    email         VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role          VARCHAR(20)  NOT NULL,
    active        BOOLEAN      NOT NULL DEFAULT TRUE,
    CONSTRAINT chk_users_role CHECK (role IN ('ADMIN', 'MECHANIC', 'OPERATOR'))
);

CREATE TABLE equipment_category (
    equipment_category_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name                  VARCHAR(100) NOT NULL UNIQUE,
    description           VARCHAR(255)
);

CREATE TABLE location (
    location_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    type        VARCHAR(20)  NOT NULL,
    description VARCHAR(255),
    CONSTRAINT chk_location_type CHECK (type IN ('HANGAR', 'WORKSHOP', 'FIELD', 'WAREHOUSE'))
);

CREATE TABLE equipment (
    equipment_id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    equipment_category_id BIGINT        NOT NULL REFERENCES equipment_category (equipment_category_id) ON DELETE RESTRICT,
    location_id           BIGINT        NOT NULL REFERENCES location (location_id) ON DELETE RESTRICT,
    name                  VARCHAR(150)  NOT NULL,
    serial_number         VARCHAR(100)  NOT NULL UNIQUE,
    manufacturer          VARCHAR(100),
    model                 VARCHAR(100),
    status                VARCHAR(20)   NOT NULL DEFAULT 'AVAILABLE',
    acquisition_date      DATE,
    price                 NUMERIC(12,2),
    engine_hours          NUMERIC(10,1) NOT NULL DEFAULT 0,
    version               BIGINT        NOT NULL DEFAULT 0,
    created_at            TIMESTAMPTZ   NOT NULL DEFAULT now(),
    updated_at            TIMESTAMPTZ   NOT NULL DEFAULT now(),
    CONSTRAINT chk_equipment_status
        CHECK (status IN ('AVAILABLE', 'ASSIGNED', 'IN_MAINTENANCE', 'IN_REPAIR', 'RETIRED')),
    CONSTRAINT chk_equipment_price CHECK (price IS NULL OR price >= 0),
    CONSTRAINT chk_equipment_hours CHECK (engine_hours >= 0)
);

CREATE TABLE assignment (
    assignment_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    equipment_id  BIGINT      NOT NULL REFERENCES equipment (equipment_id) ON DELETE RESTRICT,
    user_id       BIGINT      NOT NULL REFERENCES users (user_id) ON DELETE RESTRICT,
    start_date    TIMESTAMPTZ NOT NULL,
    end_date      TIMESTAMPTZ,
    notes         VARCHAR(255),
    CONSTRAINT chk_assignment_dates CHECK (end_date IS NULL OR end_date > start_date)
);

CREATE TABLE repair_request (
    repair_request_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    equipment_id      BIGINT        NOT NULL REFERENCES equipment (equipment_id) ON DELETE RESTRICT,
    reported_by       BIGINT        NOT NULL REFERENCES users (user_id) ON DELETE RESTRICT,
    technician_id     BIGINT        REFERENCES users (user_id) ON DELETE RESTRICT,
    description       TEXT          NOT NULL,
    priority          VARCHAR(20)   NOT NULL DEFAULT 'MEDIUM',
    status            VARCHAR(20)   NOT NULL DEFAULT 'OPEN',
    reported_at       TIMESTAMPTZ   NOT NULL DEFAULT now(),
    resolved_at       TIMESTAMPTZ,
    cost              NUMERIC(10,2),
    CONSTRAINT chk_repair_priority CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL')),
    CONSTRAINT chk_repair_status   CHECK (status IN ('OPEN', 'IN_PROGRESS', 'RESOLVED', 'CLOSED')),
    CONSTRAINT chk_repair_cost     CHECK (cost IS NULL OR cost >= 0),
    CONSTRAINT chk_repair_dates    CHECK (resolved_at IS NULL OR resolved_at >= reported_at)
);

CREATE TABLE maintenance_record (
    maintenance_id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    equipment_id            BIGINT        NOT NULL REFERENCES equipment (equipment_id) ON DELETE RESTRICT,
    technician_id           BIGINT        NOT NULL REFERENCES users (user_id) ON DELETE RESTRICT,
    type                    VARCHAR(20)   NOT NULL,
    performed_at            DATE          NOT NULL,
    engine_hours_at_service NUMERIC(10,1),
    description             TEXT,
    cost                    NUMERIC(10,2),
    CONSTRAINT chk_maintenance_type CHECK (type IN ('ROUTINE', 'PREVENTIVE', 'INSPECTION')),
    CONSTRAINT chk_maintenance_cost CHECK (cost IS NULL OR cost >= 0)
);

-- INDEXURI

-- pe cheile straine
CREATE INDEX idx_users_department          ON users (department_id);
CREATE INDEX idx_equipment_category        ON equipment (equipment_category_id);
CREATE INDEX idx_equipment_location        ON equipment (location_id);
CREATE INDEX idx_assignment_equipment      ON assignment (equipment_id);
CREATE INDEX idx_assignment_user           ON assignment (user_id);
CREATE INDEX idx_repair_equipment          ON repair_request (equipment_id);
CREATE INDEX idx_repair_reported_by        ON repair_request (reported_by);
CREATE INDEX idx_repair_technician         ON repair_request (technician_id);
CREATE INDEX idx_maintenance_equipment     ON maintenance_record (equipment_id);
CREATE INDEX idx_maintenance_technician    ON maintenance_record (technician_id);

-- pe coloane des filtrate
CREATE INDEX idx_equipment_status          ON equipment (status);
CREATE INDEX idx_repair_status             ON repair_request (status);

CREATE UNIQUE INDEX uq_assignment_active_equipment
    ON assignment (equipment_id)
    WHERE end_date IS NULL;