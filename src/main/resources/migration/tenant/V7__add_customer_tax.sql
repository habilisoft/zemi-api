create table customer_tax_settings
(
    customer_id integer references customers(id) not null,
    ncf_type text,
    created_at timestamp,
    updated_at timestamp,
    created_by text,
    updated_by text
);