create table betting_matches(
    id bigserial not null primary key,
    created_at timestamp,
    created_by varchar(32),
    modified_at timestamp,
    modified_by varchar(32),
    
    balance1 decimal,
    balance2 decimal,
    expire_time timestamp,
    bet_amount decimal,
    comment varchar(512),
    activated bool,
    match_id BIGINT not null,
    group_id BIGINT not null,
    CONSTRAINT FK_BETTING_MATCH_MATCH FOREIGN KEY (match_id) REFERENCES matches (id),
    CONSTRAINT FK_BETTING_MATCH_GROUP FOREIGN KEY (group_id) REFERENCES groups (id)
);

create table betting_players(
	id bigserial not null primary key,
	created_at timestamp,
    created_by varchar(32),
    modified_at timestamp,
    modified_by varchar(32),
    
    competitor_id BIGINT not null,
    user_id BIGINT not null,
    comment varchar(512),
    CONSTRAINT FK_PLAYER_USER FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT FK_PLAYER_COMPETITOR FOREIGN KEY (competitor_id) REFERENCES competitors (id)
);

create table betting_match_details(
	betting_player_id BIGINT not null,
    betting_match_id BIGINT not null,
    PRIMARY KEY (betting_player_id, betting_match_id),
    CONSTRAINT FK_DETAIL_BETTING_MATCH FOREIGN KEY (betting_match_id) REFERENCES betting_matches (id),
    CONSTRAINT FK_DETAIL_BETTING_PLAYER FOREIGN KEY (betting_player_id) REFERENCES betting_players (id)
);