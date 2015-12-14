// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.tournament;

import vn.kms.ngaythobet.domain.core.AuditableEntity;

import java.time.LocalDateTime;

public class Match extends AuditableEntity {
    private Tournament tournament;
    private Competitor competitor1;
    private int competitor1Score;
    private Competitor competitor2;
    private int competitor2Score;
    private LocalDateTime time;
    private String location;
    private Round round;

    public Match() {
    }

    public Match(Tournament tournament, Competitor competitor1, Competitor competitor2, LocalDateTime time,
                 String location, Round round) {
        this.tournament = tournament;
        this.competitor1 = competitor1;
        this.competitor2 = competitor2;
        this.time = time;
        this.location = location;
        this.round = round;
    }
}
