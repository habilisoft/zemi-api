create table use_case_executions
(
    id              integer primary key generated always as identity,
    command         jsonb,
    result          jsonb,
    execution_time  bigint,
    name            text,
    status          text,
    user_name       text,
    date            timestamp,
    error_message   text,
    idempotency_key text
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

CREATE TABLE users
(
    username                      TEXT NOT NULL,
    name                          TEXT,
    password                      TEXT,
    change_password_at_next_login BOOLEAN,
    deleted                       BOOLEAN DEFAULT FALSE,
    created_by                    TEXT,
    updated_by                    TEXT,
    created_at                    TIMESTAMP WITHOUT TIME ZONE,
    updated_at                    TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_users PRIMARY KEY (username)
);

CREATE TABLE roles
(
    name        TEXT PRIMARY KEY NOT NULL,
    description TEXT,
    created_at  TIMESTAMP WITHOUT TIME ZONE,
    created_by  TEXT,
    updated_at  TIMESTAMP WITHOUT TIME ZONE,
    updated_by  TEXT,
    deleted     BOOLEAN default false
);

CREATE TABLE permissions
(
    name        TEXT PRIMARY KEY NOT NULL,
    description TEXT,
    module      TEXT
);

CREATE TABLE role_permissions
(
    permission_name TEXT NOT NULL REFERENCES permissions (name),
    role_name       TEXT NOT NULL REFERENCES roles (name),
    PRIMARY KEY (permission_name, role_name)
);

CREATE TABLE user_roles
(
    role_name TEXT NOT NULL REFERENCES roles (name),
    username  TEXT NOT NULL REFERENCES users (username),
    PRIMARY KEY (role_name, username)
);


INSERT INTO roles (name, description, created_at, created_by)
VALUES ('admin', 'Rol de administrador. Tiene todos los permisos en el sistema', CURRENT_TIMESTAMP, 'system');

-- Inserting data
INSERT INTO permissions (name, description, module)
VALUES ('auth:role:create', 'Crear rol', 'AUTH'),
       ('auth:role:read', 'Ver rol', 'AUTH'),
       ('auth:role:update', 'Editar rol', 'AUTH'),
       ('auth:role:delete', 'Eliminar rol', 'AUTH'),
       ('auth:user:create', 'Crear usuario', 'AUTH'),
       ('auth:user:read', 'Ver usuario', 'AUTH'),
       ('auth:user:update', 'Editar usuario', 'AUTH'),
       ('auth:user:delete', 'Eliminar usuario', 'AUTH'),
       ('auth:user:reset-password', 'Restablecer contraseña', 'AUTH');

create table documents
(
    document varchar(6) not null,
    sequence bigint     not null,
    primary key (document),
    check ( document ~ '^[A-Z]{1,6}$' )
);
create or replace function document_next_sequence(doc varchar(6)) returns bigint
    language plpgsql as
$$
declare
    sequence      bigint;
    next_sequence bigint;
begin
    -- Start a transaction with SERIALIZABLE isolation level
    perform pg_advisory_xact_lock(hashtext(doc));

    -- Get the next sequence value
    select documents.sequence
    into sequence
    from documents
    where document = doc;

    next_sequence := coalesce(sequence, 0) + 1;

    -- Get the next sequence value
    insert
    into documents (document, sequence)
    values (doc, next_sequence)
    on conflict (document) do update
        set sequence = excluded.sequence;

    return next_sequence;
end;
$$;
create table categories
(
    id   serial primary key,
    name text not null
);
create table products
(
    id          serial primary key,
    category_id integer references categories (id),
    name        text not null
);
