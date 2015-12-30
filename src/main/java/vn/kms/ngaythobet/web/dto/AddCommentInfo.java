package vn.kms.ngaythobet.web.dto;

import static vn.kms.ngaythobet.domain.util.Constants.WHITE_SPACE_REGEX;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import vn.kms.ngaythobet.domain.betting.BettingPlayer;
import vn.kms.ngaythobet.domain.validation.EntityExist;

public class AddCommentInfo {
    @NotEmpty
    @EntityExist(type = BettingPlayer.class)
    private Long bettingMatchId;

    @NotEmpty
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
