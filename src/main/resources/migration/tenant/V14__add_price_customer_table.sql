create table customer_price_lists
(
    customer_id   integer references customers (id) primary key,
    price_list_id integer references price_lists (id),
    created_at    timestamp,
    updated_at    timestamp,
    created_by    text,
    updated_by    text
);