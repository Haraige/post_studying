CREATE TABLE IF NOT EXISTS roles (
    id bigint auto_increment PRIMARY KEY,
    name VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS users (
    id bigint auto_increment PRIMARY KEY,
    login VARCHAR(50) unique not null,
    password varchar(255) not null,
    email varchar(50) unique not null,
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    birthday date not null,
    role_id BIGINT NOT NULL,
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

INSERT IGNORE INTO roles(id, name) VALUES (1, 'Admin');
INSERT IGNORE INTO roles(id, name) VALUES (2, 'User');
INSERT IGNORE INTO roles(id, name) VALUES (3, 'SuperAdmin');

INSERT IGNORE INTO users(
	id, login, password, email, first_name, last_name, birthday, role_id)
	VALUES (1, 'admin', '{bcrypt}$2a$10$B3y971B1Mhqx2zZIXFv3i.ZrM8QtSNpSPwwE5YUs/c2L5gMAGcwqC', 'peter@mail.com', 'Peter', 'Debkin', '2001-05-13', 1);

INSERT IGNORE INTO users(
	id, login, password, email, first_name, last_name, birthday, role_id)
	VALUES (2, 'user', '{bcrypt}$2a$12$Iz2qNs0RjjD.6b0igcAOSurSqQex.tvp5iPf/Q8L5oQ2uyOKkVfjq', 'alice@mail.com', 'Alice', 'Golovko', '1998-03-09', 2);

INSERT IGNORE INTO users(
	id, login, password, email, first_name, last_name, birthday, role_id)
	VALUES (3, 'super', '{bcrypt}$2a$12$zWzzKuhB2dQYgnD7btIP9Ogkb5WL5xdFEdVWukoidkGJ8OrKGRFuu', 'super@mail.com', 'Andrew', 'Point', '1998-03-09', 3);