package vn.kms.ngaythobet.web.dto;

import org.hibernate.validator.constraints.NotEmpty;

import vn.kms.ngaythobet.domain.betting.BettingPlayer;
import vn.kms.ngaythobet.domain.validation.EntityExist;

public class updatePlayerBettingMatchInfo {
    @NotEmpty
    @EntityExist(type = BettingPlayer.class)
    private Long bettingPlayerId;

    @NotEmpty
    private Long CompetitorId;

    public Long getBettingPlayerId() {
        return bettingPlayerId;
    }

    public void setBettingPlayerId(Long bettingPlayerId) {
        this.bettingPlayerId = bettingPlayerId;
    }

    public Long getCompetitorId() {
        return CompetitorId;
    }

    public void setCompetitorId(Long competitorId) {
        CompetitorId = competitorId;
    }

}
