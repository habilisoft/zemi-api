create table price_lists
(
    id         serial primary key,
    name       text,
    created_at timestamp,
    updated_at timestamp,
    created_by text,
    updated_by text
);
create table price_list_product
(
    price_list_id integer references price_lists (id) not null,
    product_id    integer references products (id) not null,
    date          timestamp      not null,
    price         numeric(10, 2) not null,
    is_current    boolean not null,
    created_at    timestamp,
    updated_at    timestamp,
    created_by    text,
    updated_by    text,
    primary key (price_list_id, product_id, date)
);