create table company_information(
    id integer generated always as identity primary key,
    name text not null,
    address text,
    phone text,
    email text,
    website text,
    document text,
    logo text
);