// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.betting;

import vn.kms.ngaythobet.domain.core.AuditableEntity;
import vn.kms.ngaythobet.domain.core.User;
import vn.kms.ngaythobet.domain.tournament.Competitor;

public class BettingPlayer extends AuditableEntity {
    private BettingMatch bettingMatch;
    private long playerId;
    private Long betCompetitorId;
    private String comment;
}
