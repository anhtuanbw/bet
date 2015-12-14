// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.betting;

import vn.kms.ngaythobet.domain.core.AuditableEntity;
import vn.kms.ngaythobet.domain.core.User;
import vn.kms.ngaythobet.domain.tournament.Tournament;

import java.util.List;

public class BettingGroup extends AuditableEntity {
    private long tournamentId;
    private String name;
    private List<User> players;
    private User moderator;
    private String rules;
    private boolean active;
}
