package vn.kms.ngaythobet.web.dto;

import org.hibernate.validator.constraints.NotEmpty;

import vn.kms.ngaythobet.domain.betting.BettingPlayer;
import vn.kms.ngaythobet.domain.validation.EntityExist;

public class UpdatePlayerBettingMatchInfo {
    @NotEmpty
    @EntityExist(type = BettingPlayer.class)
    private Long bettingPlayerId;

    @NotEmpty
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
