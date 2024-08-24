create table business_entities
(
    id serial primary key
);
create table business_entity_phones
(
    business_entity_id integer references business_entities (id),
    phone text not null,
    primary key (business_entity_id, phone)
);
create table business_entity_addresses
(
    business_entity_id integer primary key references business_entities (id),
    street text not null,
    city text not null,
    zip_code text not null
);

create table customers
(
    id integer references business_entities (id),
    name text not null,
    type text,
    email text,
    created_at timestamp,
    updated_at timestamp,
    created_by text,
    updated_by text
);

-- add trigger to the customers table to insert a row in the business_entities table
create or replace function insert_business_entity()
    returns trigger
    language plpgsql
as
$$
begin
    insert into business_entities (id)
    values (new.id) on conflict do nothing;
    return new;
end;
$$;
create trigger insert_business_entity
    before insert on customers
    for each row
    execute function insert_business_entity();


