-- Manual PostgreSQL script for issue #326.
-- Run after the DeliveryReceiver primary key migration and before relying on delivery item emails.
-- Existing deliveries are backfilled as "all items from the request" because old records had no item-level link.

BEGIN;

ALTER TABLE notification
    ALTER COLUMN message TYPE varchar(4000);

CREATE TABLE IF NOT EXISTS delivery_item_request_product (
    delivery_id bigint NOT NULL,
    item_request_product_id bigint NOT NULL
);

CREATE TABLE IF NOT EXISTS delivery_item_request_service (
    delivery_id bigint NOT NULL,
    item_request_service_id bigint NOT NULL
);

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'pk_delivery_item_request_product') THEN
        ALTER TABLE delivery_item_request_product
            ADD CONSTRAINT pk_delivery_item_request_product
            PRIMARY KEY (delivery_id, item_request_product_id);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'pk_delivery_item_request_service') THEN
        ALTER TABLE delivery_item_request_service
            ADD CONSTRAINT pk_delivery_item_request_service
            PRIMARY KEY (delivery_id, item_request_service_id);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_delivery_item_request_product_delivery') THEN
        ALTER TABLE delivery_item_request_product
            ADD CONSTRAINT fk_delivery_item_request_product_delivery
            FOREIGN KEY (delivery_id) REFERENCES delivery (id) ON DELETE CASCADE;
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_delivery_item_request_product_item') THEN
        ALTER TABLE delivery_item_request_product
            ADD CONSTRAINT fk_delivery_item_request_product_item
            FOREIGN KEY (item_request_product_id) REFERENCES item_request_product (id) ON DELETE CASCADE;
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_delivery_item_request_service_delivery') THEN
        ALTER TABLE delivery_item_request_service
            ADD CONSTRAINT fk_delivery_item_request_service_delivery
            FOREIGN KEY (delivery_id) REFERENCES delivery (id) ON DELETE CASCADE;
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_delivery_item_request_service_item') THEN
        ALTER TABLE delivery_item_request_service
            ADD CONSTRAINT fk_delivery_item_request_service_item
            FOREIGN KEY (item_request_service_id) REFERENCES item_request_service (id) ON DELETE CASCADE;
    END IF;
END $$;

INSERT INTO delivery_item_request_product (delivery_id, item_request_product_id)
SELECT delivery.id, item.id
FROM delivery
JOIN item_request_product item ON item.request_id = delivery.request_id
ON CONFLICT DO NOTHING;

INSERT INTO delivery_item_request_service (delivery_id, item_request_service_id)
SELECT delivery.id, item.id
FROM delivery
JOIN item_request_service item ON item.request_id = delivery.request_id
ON CONFLICT DO NOTHING;

COMMIT;
