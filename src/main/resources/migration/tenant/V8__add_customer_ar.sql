create table customer_ar_settings
(
    customer_id integer references customers(id) not null,
    credit_limit numeric(10, 2),
    created_at timestamp,
    updated_at timestamp,
    created_by text,
    updated_by text
);