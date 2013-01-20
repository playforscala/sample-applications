# --- !Ups

create table if not exists "products" (
  "id" BIGINT NOT NULL PRIMARY KEY,
  "ean" BIGINT NOT NULL,
  "name" VARCHAR(254) NOT NULL,
  "description" VARCHAR(254) NOT NULL
);

# --- !Downs

drop table products;


