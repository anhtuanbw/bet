package vn.kms.ngaythobet.web.dto;

import javax.validation.constraints.NotNull;

import vn.kms.ngaythobet.domain.tournament.Group;
import vn.kms.ngaythobet.domain.tournament.Round;
import vn.kms.ngaythobet.domain.validation.EntityExist;

public class GetBettingMatchesByRoundAndGroupIdInfo {

    @NotNull
    @EntityExist(type = Round.class)
    private Long roundId;

    @NotNull
    @EntityExist(type = Group.class)
    private Long groupId;

    public Long getRoundId() {
        return roundId;
    }

    public void setRoundId(Long roundId) {
        this.roundId = roundId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}
