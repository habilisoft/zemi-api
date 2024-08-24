alter table products
    add column created_at timestamp,
    add column updated_at timestamp,
    add column created_by text,
    add column updated_by text;

alter table categories
    add column created_at timestamp,
    add column updated_at timestamp,
    add column created_by text,
    add column updated_by text;