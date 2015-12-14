// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.betting;

import vn.kms.ngaythobet.domain.core.AuditableEntity;
import vn.kms.ngaythobet.domain.tournament.Match;

import java.time.LocalDateTime;

public class BettingMatch extends AuditableEntity {
    private long groupId;
    private Match match;
    private double competitor1Balance;
    private double competitor2Balance;
    private LocalDateTime expiryTime;
    private long betAmount;
    private String comment;
    private boolean active;
}
