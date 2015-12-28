package vn.kms.ngaythobet.web.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import vn.kms.ngaythobet.domain.tournament.Group;
import vn.kms.ngaythobet.domain.validation.EntityExist;
import vn.kms.ngaythobet.domain.validation.ListUnique;

public class AddNewMemberInfo {
    @NotNull
    @EntityExist(type = Group.class)
    private Long groupId;

    @ListUnique(message = "{validation.member.unique.message}")
    private List<Long> memberIds;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public List<Long> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<Long> memberIds) {
        this.memberIds = memberIds;
    }

}
