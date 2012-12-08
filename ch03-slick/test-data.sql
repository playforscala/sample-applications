-- Install PostgreSQL (OSX with Homebrew)…
-- brew install postgres

-- Create PostgreSQL user and database…
-- createuser -S -D -R -P slick
-- password: slick
-- createdb -E UNICODE -O slick slick 'Slick example database'

-- Create test data (run this script)…
-- psql --host localhost --username slick --dbname slick --file test-data.sql

DROP TABLE IF EXISTS products;

CREATE TABLE products (
  ean           numeric(13,0),
  name          varchar(40),
  description   varchar(40)
);

insert into products (ean, name, description) values (5010255079763, 'Paperclips Large', 'Large Plain Pack of 1000');
insert into products (ean, name, description) values (5018206244666, 'Giant Paperclips', 'Giant Plain 51mm 100 pack');
insert into products (ean, name, description) values (5018306332812, 'Paperclip Giant Plain', 'Giant Plain Pack of 10000');
insert into products (ean, name, description) values (5018306312913, 'No Tear Paper Clip', 'No Tear Extra Large Pack of 1000');
insert into products (ean, name, description) values (5018206244611, 'Zebra Paperclips', 'Zebra Length 28mm Assorted 150 Pack');
