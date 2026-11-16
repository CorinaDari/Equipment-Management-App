ALTER TABLE repair_request
RENAME TO repair_ticket;

ALTER TABLE repair_ticket
ADD COLUMN version BIGINT NOT NULL DEFAULT 0;

CREATE UNIQUE INDEX uq_repair_active_equipment
    ON repair_ticket (equipment_id)
    WHERE status IN ('OPEN', 'IN_PROGRESS');