# #ch05-anorm-schema
# --- !Ups //#A

CREATE TABLE products ( //#B
    id long,
    ean long,
    name varchar,
    description varchar);

CREATE TABLE warehouses ( //#B
    id long,
    name varchar);

CREATE TABLE stock_items ( //#B
    id long,
    product_id long,
    warehouse_id long,
    quantity long);

# --- !Downs //#C

DROP TABLE IF EXISTS products; //#D

DROP TABLE IF EXISTS warehouses; //#D

DROP TABLE IF EXISTS stock_items; //#D
# #ch05-anorm-schema
