package vn.kms.ngaythobet.web.dto;

import static vn.kms.ngaythobet.domain.util.Constants.WHITE_SPACE_REGEX;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import vn.kms.ngaythobet.domain.betting.BettingMatch;
import vn.kms.ngaythobet.domain.validation.EntityExist;

public class AddCommentInfo {
    @NotNull
    @EntityExist(type = BettingMatch.class)
    private Long bettingMatchId;

    @NotNull
    @Pattern(regexp = WHITE_SPACE_REGEX, message = "{validation.pattern.blankspace}")
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
