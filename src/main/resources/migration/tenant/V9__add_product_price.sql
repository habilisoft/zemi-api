create table product_prices (
    product_id integer references products (id),
    date       timestamp      not null,
    price      numeric(10, 2) not null,
    is_current boolean not null,
    created_at timestamp,
    updated_at timestamp,
    created_by text,
    updated_by text,
    primary key (product_id, date)
);