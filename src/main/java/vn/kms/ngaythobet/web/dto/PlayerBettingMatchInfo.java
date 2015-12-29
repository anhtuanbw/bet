package vn.kms.ngaythobet.web.dto;

import org.hibernate.validator.constraints.NotEmpty;

import vn.kms.ngaythobet.domain.tournament.Competitor;
import vn.kms.ngaythobet.domain.tournament.Match;
import vn.kms.ngaythobet.domain.validation.EntityExist;

public class PlayerBettingMatchInfo {
    @NotEmpty
    @EntityExist(type = Match.class)
    private Long matchId;
    
    @NotEmpty
    @EntityExist(type = Competitor.class)
    private Long CompetitorId;
    
    private String comment;

    public Long getMatchId() {
        return matchId;
    }

    public void setMathchId(Long matchId) {
        this.matchId = matchId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getCompetitorId() {
        return CompetitorId;
    }

    public void setCompetitorId(Long competitorId) {
        CompetitorId = competitorId;
    }

}
