package vn.kms.ngaythobet.web.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import vn.kms.ngaythobet.domain.betting.BettingMatch;
import vn.kms.ngaythobet.domain.validation.EntityExist;

public class AddCommentInfo {
    @NotNull
    @EntityExist(type = BettingMatch.class)
    private Long bettingMatchId;

    @NotEmpty
    @Size(max = 512, message = "{validation.comment.long}")
    private String comment;

    public Long getBettingMatchId() {
        return bettingMatchId;
    }

    public void setBettingMatchId(Long bettingMatchId) {
        this.bettingMatchId = bettingMatchId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
