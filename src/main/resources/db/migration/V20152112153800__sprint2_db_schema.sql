create table tournaments(
    id bigserial not null primary key,
    created_at timestamp,
    created_by varchar(32),
    modified_at timestamp,
    modified_by varchar(32),
    
    number_of_competitors BIGINT,
    activated bool,
    name varchar(64)
);

create table rounds(
    id bigserial not null primary key,
    created_at timestamp,
    created_by varchar(32),
    modified_at timestamp,
    modified_by varchar(32),
    
    name varchar(64),
    tournament_id BIGINT not null,
    CONSTRAINT FK_ROUND_TOURNAMENT FOREIGN KEY (tournament_id) REFERENCES tournaments (id)
);

create table competitors(
    id bigserial not null primary key,
    created_at timestamp,
    created_by varchar(32),
    modified_at timestamp,
    modified_by varchar(32),
    
    name varchar(64),
    tournament_id BIGINT not null,
    CONSTRAINT FK_COMPETITOR_TOURNAMENT FOREIGN KEY (tournament_id) REFERENCES tournaments (id)
);

create table round_competitor(
    competitor_id BIGINT not null,
    round_id BIGINT not null,
    PRIMARY KEY (competitor_id, round_id),
    CONSTRAINT FK_ROUND_COMPETITOR_ROUND FOREIGN KEY (round_id) REFERENCES rounds (id),
    CONSTRAINT FK_ROUND_COMPETITOR_COMPETITOR FOREIGN KEY (competitor_id) REFERENCES competitors (id)
);

create table groups(
    id bigserial not null primary key,
    created_at timestamp,
    created_by varchar(32),
    modified_at timestamp,
    modified_by varchar(32),
    
    name varchar(64),
    moderator bigserial not null,
    CONSTRAINT FK_GROUP_MODDERATOR FOREIGN KEY (moderator) REFERENCES users (id)
);

create table group_user(
    group_id BIGINT not null,
    user_id BIGINT not null,
    PRIMARY KEY (group_id, user_id),
    CONSTRAINT FK_GROUP_USER_GROUP FOREIGN KEY (group_id) REFERENCES groups (id),
    CONSTRAINT FK_GROUP_USER_USER FOREIGN KEY (user_id) REFERENCES users (id)
);

create table group_tournament(
    group_id BIGINT not null,
    tournament_id BIGINT not null,
    PRIMARY KEY (group_id, tournament_id),
    CONSTRAINT FK_GROUP_TOURNAMENT_GROUP FOREIGN KEY (group_id) REFERENCES groups (id),
    CONSTRAINT FK_GROUP_TOURNAMENT_TOURNAMENT FOREIGN KEY (tournament_id) REFERENCES tournaments (id)
);

create table matches(
    id bigserial not null primary key,
    created_at timestamp,
    created_by varchar(32),
    modified_at timestamp,
    modified_by varchar(32),
    
    competitor_id_1 BIGINT not null,
    competitor_id_2 BIGINT not null,
    round_id BIGINT not null,
    score_1 BIGINT,
    score_2 BIGINT,
    match_time timestamp,
    location varchar(128),
    CONSTRAINT FK_MATCH_ROUND FOREIGN KEY (round_id) REFERENCES rounds (id),
    CONSTRAINT FK_MATCH_COMPETITOR1 FOREIGN KEY (competitor_id_1) REFERENCES competitors (id),
    CONSTRAINT FK_MATCH_COMPETITOR2 FOREIGN KEY (competitor_id_2) REFERENCES competitors (id)
)