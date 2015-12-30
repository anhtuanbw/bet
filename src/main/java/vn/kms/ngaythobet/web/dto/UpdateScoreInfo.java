package vn.kms.ngaythobet.web.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import vn.kms.ngaythobet.domain.tournament.Match;
import vn.kms.ngaythobet.domain.validation.EntityExist;

public class UpdateScoreInfo {
    @NotNull
    @EntityExist(type = Match.class, message = "{validation.existMatchEntity.message}")
    private Long matchId;

    @NotNull
    @Min(value = 0, message = "{validation.score.message}")
    private Long competitor1Score;

    @NotNull
    @Min(value = 0, message = "{validation.score.message}")
    private Long competitor2Score;

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public Long getCompetitor1Score() {
        return competitor1Score;
    }

    public void setCompetitor1Score(Long competitor1Score) {
        this.competitor1Score = competitor1Score;
    }

    public Long getCompetitor2Score() {
        return competitor2Score;
    }

    public void setCompetitor2Score(Long competitor2Score) {
        this.competitor2Score = competitor2Score;
    }
}
