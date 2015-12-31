package vn.kms.ngaythobet.web.dto;

import javax.validation.constraints.NotNull;

import vn.kms.ngaythobet.domain.core.User;
import vn.kms.ngaythobet.domain.tournament.Group;
import vn.kms.ngaythobet.domain.validation.EntityExist;

/**
 * 
 * @author thangpham
 *
 */
public class CheckModeratorInfo {
    @NotNull
    @EntityExist(type = Group.class)
    private Long groupId;

    @NotNull
    @EntityExist(type = User.class)
    private Long userId;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
