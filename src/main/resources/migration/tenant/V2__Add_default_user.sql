insert into users (username, password, change_password_at_next_login, deleted, created_by, created_at)
values ('admin', '$2a$10$sHtE7PVgHJwsx7JKqbCcauRZ3izpX7Iedl72CwGumopC9ULzw7l.e', true, false, 'system', now());

insert into user_roles (role_name, username)
values ('admin', 'admin');