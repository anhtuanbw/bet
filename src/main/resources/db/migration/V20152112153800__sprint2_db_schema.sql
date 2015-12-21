create table tournaments(
    id bigserial not null primary key,
    number_of_competitors integer,
    activated bool
);

create table rounds(
    id bigserial not null primary key,
    name varchar(64),
    tournament_id BIGINT not null,
    CONSTRAINT FK_TOURNAMENT FOREIGN KEY (tournament_id) REFERENCES tournaments (id)
);

create table competitors(
    id bigserial not null primary key,
    name varchar(64),
    tournament_id BIGINT not null,
    CONSTRAINT FK_TOURNAMENT FOREIGN KEY (tournament_id) REFERENCES tournaments (id)
);

create table round_competitor(
    competitor_id BIGINT not null,
    round_id BIGINT not null,
    PRIMARY KEY (competitor_id, round_id),
    CONSTRAINT FK_ROUND FOREIGN KEY (round_id) REFERENCES rounds (id),
    CONSTRAINT FK_COMPETITOR FOREIGN KEY (competitor_id) REFERENCES competitors (id)
);

create table groups(
    id bigserial not null primary key,
    name varchar(64),
    moderator bigserial not null
);

create table group_user(
    group_id BIGINT not null,
    user_id BIGINT not null,
    PRIMARY KEY (group_id, user_id),
    CONSTRAINT FK_GROUP FOREIGN KEY (group_id) REFERENCES groups (id),
    CONSTRAINT FK_USER FOREIGN KEY (user_id) REFERENCES users (id)
);

create table group_tournament(
    group_id BIGINT not null,
    tournament_id BIGINT not null,
    PRIMARY KEY (group_id, tournament_id),
    CONSTRAINT FK_GROUP FOREIGN KEY (group_id) REFERENCES groups (id),
    CONSTRAINT FK_TOURNAMENT FOREIGN KEY (tournament_id) REFERENCES tournaments (id)
);

create table matches(
    id bigserial not null primary key,
    competitor_id_1 BIGINT not null,
    competitor_id_2 BIGINT not null,
    round_id BIGINT not null,
    score_1 integer,
    score_2 integer,
    match_time timestamp,
    location varchar(128),
    CONSTRAINT FK_ROUND FOREIGN KEY (round_id) REFERENCES rounds (id)
);
