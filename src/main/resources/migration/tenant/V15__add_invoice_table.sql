create table ncf_sequences
(
    ncf_type         text not null,
    ncf_series       text not null,
    date             date not null,
    initial_sequence integer,
    final_sequence   integer,
    current_sequence integer,
    active           boolean,
    version          integer not null,
    created_at       timestamp,
    updated_at       timestamp,
    created_by       text,
    updated_by       text,
    primary key (ncf_type, date)
);

create table invoices
(
    document    text           not null,
    sequence    integer        not null,
    customer_id integer references customers (id),
    total       numeric(10, 2) not null,
    version     integer        not null,
    ncf         text           not null,
    created_at  timestamp,
    updated_at  timestamp,
    created_by  text,
    updated_by  text,
    primary key (document, sequence)
);