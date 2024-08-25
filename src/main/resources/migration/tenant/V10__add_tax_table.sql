create table taxes
(
    id         serial primary key,
    name       text,
    rate       numeric(10, 2),
    created_at timestamp,
    updated_at timestamp,
    created_by text,
    updated_by text
);