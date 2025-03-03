create table person
(
    id          bigint not null primary key,
    email       varchar(255),
    first_name  varchar(255),
    last_name   varchar(255),
    middle_name varchar(255),
    phone       bigint
);

INSERT INTO person (id, email, first_name, last_name, middle_name, phone)
VALUES (0, 'karel.capek@gmail.com', 'Karel', 'Čapek', null, 607661548);


create table account
(
    id        bigint not null primary key,
    password  varchar(255),
    role      smallint
        constraint account_role_check
            check ((role >= 0) AND (role <= 4)),
    username  varchar(255),
    person_id bigint
        constraint uk_iajp1nugms7a5wl86ecnjamw2
            unique
        constraint fkd9dhia7smrg88vcbiykhofxee
            references person
);

INSERT INTO account (id, password, role, username, person_id)
VALUES (0, '$2a$10$OkMC9XdP50hoHQ.uTXAzZOeJ5L8miyBYAa14ilts.cX6ZZwISGhMO', 4, 'admin', 0);


