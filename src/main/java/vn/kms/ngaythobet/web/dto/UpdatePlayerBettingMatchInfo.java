package vn.kms.ngaythobet.web.dto;

import javax.validation.constraints.NotNull;

import vn.kms.ngaythobet.domain.betting.BettingPlayer;
import vn.kms.ngaythobet.domain.tournament.Competitor;
import vn.kms.ngaythobet.domain.validation.EntityExist;

public class UpdatePlayerBettingMatchInfo {
    @NotNull
    @EntityExist(type = BettingPlayer.class)
    private Long bettingPlayerId;

    @NotNull
    @EntityExist(type = Competitor.class)
    private Long competitorId;

    public Long getBettingPlayerId() {
        return bettingPlayerId;
    }

    public void setBettingPlayerId(Long bettingPlayerId) {
        this.bettingPlayerId = bettingPlayerId;
    }

    public Long getCompetitorId() {
        return competitorId;
    }

    public void setCompetitorId(Long competitorId) {
        this.competitorId = competitorId;
    }

}
