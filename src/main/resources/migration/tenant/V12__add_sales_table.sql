create table sales
(
    document    text           not null,
    sequence    integer        not null,
    date        timestamp      not null,
    customer_id integer references customers (id),
    total       numeric(10, 2) not null,
    created_at  timestamp,
    updated_at  timestamp,
    created_by  text,
    updated_by  text,
    primary key (document, sequence)
);

create table sale_products
(
    sale_document text           not null,
    sale_sequence integer        not null,
    product_id    integer        not null,
    quantity      integer        not null,
    price         numeric(10, 2) not null,
    tax_amount    numeric(10, 2),
    primary key (sale_document, sale_sequence, product_id),
    foreign key (sale_document, sale_sequence) references sales (document, sequence),
    foreign key (product_id) references products (id)
);

create table sale_product_taxes
(
    sale_document text           not null,
    sale_sequence integer        not null,
    product_id    integer        not null,
    tax_id        integer        not null,
    tax_rate      numeric(10, 2) not null,
    tax_amount    numeric(10, 2) not null,
    primary key (sale_document, sale_sequence, product_id, tax_id),
    foreign key (sale_document, sale_sequence, product_id) references sale_products (sale_document, sale_sequence, product_id),
    foreign key (tax_id) references taxes (id)
);