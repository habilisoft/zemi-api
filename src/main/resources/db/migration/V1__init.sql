create table tenants
(
    name text primary key
);

create table if not exists event_publication
(
    id               uuid                     not null,
    listener_id      text                     not null,
    event_type       text                     not null,
    serialized_event text                     not null,
    publication_date timestamp with time zone not null,
    completion_date  timestamp with time zone,
    primary key (id)
);