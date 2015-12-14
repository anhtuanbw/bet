create table users(
    id bigserial not null primary key,
    created_at timestamp,
    created_by varchar(32),
    modified_at timestamp,
    modified_by varchar(32),

    username varchar(32) not null unique,
    password varchar(60),
    email varchar(64) not null unique,
    name varchar(64),
    language_tag varchar(5),
    role varchar(12),
    activated bool,
    activation_key varchar(32),
    reset_key varchar(32),
    reset_time timestamp
);
