DROP TABLE IF EXISTS cart;

CREATE TABLE cart (
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    employeeid uuid NOT NULL,
    productids uuid[],
    productcounts int[]
) WITH (
    OIDS=FALSE
);