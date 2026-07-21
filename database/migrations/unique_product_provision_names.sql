-- Execute this script manually against PostgreSQL after reviewing every query result.
-- It only identifies duplicates and creates constraints; it never removes or merges records.
-- The CREATE statements must be run only when their corresponding duplicate query returns no rows.

-- 1. Products with names that normalize to the same value.
SELECT
    LOWER(REGEXP_REPLACE(BTRIM(name), '[[:space:]]+', ' ', 'g')) AS normalized_name,
    ARRAY_AGG(id ORDER BY id) AS product_ids,
    COUNT(*) AS occurrences
FROM product
GROUP BY LOWER(REGEXP_REPLACE(BTRIM(name), '[[:space:]]+', ' ', 'g'))
HAVING COUNT(*) > 1;

-- 2. Services with names that normalize to the same value.
SELECT
    LOWER(REGEXP_REPLACE(BTRIM(name), '[[:space:]]+', ' ', 'g')) AS normalized_name,
    ARRAY_AGG(id ORDER BY id) AS provision_ids,
    COUNT(*) AS occurrences
FROM provision
GROUP BY LOWER(REGEXP_REPLACE(BTRIM(name), '[[:space:]]+', ' ', 'g'))
HAVING COUNT(*) > 1;

-- 3. Existing duplicate product items within a request.
SELECT
    request_id,
    product_id,
    ARRAY_AGG(id ORDER BY id) AS item_ids,
    COUNT(*) AS occurrences
FROM item_request_product
GROUP BY request_id, product_id
HAVING COUNT(*) > 1;

-- 4. Existing duplicate service items within a request.
SELECT
    request_id,
    provision_id,
    ARRAY_AGG(id ORDER BY id) AS item_ids,
    COUNT(*) AS occurrences
FROM item_request_service
GROUP BY request_id, provision_id
HAVING COUNT(*) > 1;

-- Run the following statements only after all four checks above return no rows.
CREATE UNIQUE INDEX ux_product_normalized_name
    ON product (LOWER(REGEXP_REPLACE(BTRIM(name), '[[:space:]]+', ' ', 'g')));

CREATE UNIQUE INDEX ux_provision_normalized_name
    ON provision (LOWER(REGEXP_REPLACE(BTRIM(name), '[[:space:]]+', ' ', 'g')));

CREATE UNIQUE INDEX uq_item_request_product_request_product
    ON item_request_product (request_id, product_id);

CREATE UNIQUE INDEX uq_item_request_service_request_provision
    ON item_request_service (request_id, provision_id);
