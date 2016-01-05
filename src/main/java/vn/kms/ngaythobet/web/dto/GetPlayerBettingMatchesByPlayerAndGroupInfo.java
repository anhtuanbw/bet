package vn.kms.ngaythobet.web.dto;

import javax.validation.constraints.NotNull;

import vn.kms.ngaythobet.domain.tournament.Group;
import vn.kms.ngaythobet.domain.validation.EntityExist;

public class GetPlayerBettingMatchesByPlayerAndGroupInfo {

    @EntityExist(type = Group.class)
    @NotNull
    private Long groupId;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}
