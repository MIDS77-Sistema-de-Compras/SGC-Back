-- PostgreSQL manual script for existing databases.
-- Goal: replace the composite primary key (delivery_id, user_id) with a generated id
-- while preserving all delivery_receiver rows and foreign keys.
--
-- Safe execution order:
-- 1. Run this script in a maintenance window or inside an explicit transaction.
-- 2. Confirm the duplicate check returns no rows.
-- 3. Let the script add/populate id, replace the primary key, and add the unique constraint.
-- 4. Validate the application with spring.jpa.hibernate.ddl-auto=validate.

BEGIN;

-- Abort if duplicated receiver links already exist.
DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM delivery_receiver
        GROUP BY delivery_id, user_id
        HAVING COUNT(*) > 1
    ) THEN
        RAISE EXCEPTION 'delivery_receiver has duplicated (delivery_id, user_id) rows. Resolve duplicates before continuing.';
    END IF;
END $$;

-- Add the surrogate id column without dropping existing data.
ALTER TABLE delivery_receiver
    ADD COLUMN IF NOT EXISTS id BIGINT;

-- Create the sequence used by the new primary key.
CREATE SEQUENCE IF NOT EXISTS delivery_receiver_id_seq;

-- Populate ids for existing rows.
UPDATE delivery_receiver
SET id = nextval('delivery_receiver_id_seq')
WHERE id IS NULL;

-- Keep the sequence ahead of the current maximum id.
SELECT setval(
    'delivery_receiver_id_seq',
    COALESCE((SELECT MAX(id) FROM delivery_receiver), 0) + 1,
    false
);

-- Make id generated for future inserts.
ALTER TABLE delivery_receiver
    ALTER COLUMN id SET DEFAULT nextval('delivery_receiver_id_seq'),
    ALTER COLUMN id SET NOT NULL;

-- Attach sequence ownership to the new id column.
ALTER SEQUENCE delivery_receiver_id_seq
    OWNED BY delivery_receiver.id;

-- Drop the current primary key regardless of its generated name.
DO $$
DECLARE
    primary_key_name TEXT;
BEGIN
    SELECT constraint_name
    INTO primary_key_name
    FROM information_schema.table_constraints
    WHERE table_schema = current_schema()
      AND table_name = 'delivery_receiver'
      AND constraint_type = 'PRIMARY KEY';

    IF primary_key_name IS NOT NULL THEN
        EXECUTE format('ALTER TABLE delivery_receiver DROP CONSTRAINT %I', primary_key_name);
    END IF;
END $$;

-- Create the new primary key on id.
ALTER TABLE delivery_receiver
    ADD CONSTRAINT delivery_receiver_pkey PRIMARY KEY (id);

-- Preserve the business rule previously represented by the composite key.
ALTER TABLE delivery_receiver
    DROP CONSTRAINT IF EXISTS uk_delivery_receiver_delivery_user;

ALTER TABLE delivery_receiver
    ADD CONSTRAINT uk_delivery_receiver_delivery_user UNIQUE (delivery_id, user_id);

COMMIT;
