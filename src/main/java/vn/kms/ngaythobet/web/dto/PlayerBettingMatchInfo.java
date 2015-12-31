package vn.kms.ngaythobet.web.dto;

import javax.validation.constraints.NotNull;

import vn.kms.ngaythobet.domain.betting.BettingMatch;
import vn.kms.ngaythobet.domain.tournament.Competitor;
import vn.kms.ngaythobet.domain.validation.EntityExist;

public class PlayerBettingMatchInfo {
    @NotNull
    @EntityExist(type = BettingMatch.class)
    private Long bettingMatchId;

    @NotNull
    @EntityExist(type = Competitor.class)
    private Long competitorId;

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

    public Long getCompetitorId() {
        return competitorId;
    }

    public void setCompetitorId(Long competitorId) {
        this.competitorId = competitorId;
    }

}
