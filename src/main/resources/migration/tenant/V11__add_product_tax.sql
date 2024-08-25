create table product_taxes
(
    product_id integer references products (id) not null,
    tax_id     integer references taxes (id) not null,
    created_at timestamp,
    updated_at timestamp,
    created_by text,
    updated_by text,
    primary key (product_id, tax_id)
);