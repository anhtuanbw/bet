package vn.kms.ngaythobet.web.dto;

import javax.validation.constraints.NotNull;

import vn.kms.ngaythobet.domain.betting.BettingMatch;
import vn.kms.ngaythobet.domain.tournament.Group;
import vn.kms.ngaythobet.domain.validation.EntityExist;
import vn.kms.ngaythobet.domain.validation.ModeratorAccess;

@ModeratorAccess
public class ActiveBettingMatchInfo {

    @NotNull
    @EntityExist(type = BettingMatch.class, message = "{validation.betting.match.id.in.valid}")
    private Long bettingMatchId;

    @NotNull
    @EntityExist(type = Group.class, message = "{validation.existGroup.message}")
    private Long groupId;

    public Long getBettingMatchId() {
        return bettingMatchId;
    }

    public void setBettingMatchId(Long bettingMatchId) {
        this.bettingMatchId = bettingMatchId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

}
