package vn.kms.ngaythobet.web.dto;

import vn.kms.ngaythobet.domain.betting.BettingMatch;
import vn.kms.ngaythobet.domain.validation.EntityExist;
import vn.kms.ngaythobet.domain.validation.ModeratorAccess;

@ModeratorAccess
public class ActiveBettingMatchInfo {

    @EntityExist(type = BettingMatch.class, message = "{exception.betting.match.id.in.valid}")
    private Long bettingMatchId;

    public Long getBettingMatchId() {
        return bettingMatchId;
    }

    public void setBettingMatchId(Long bettingMatchId) {
        this.bettingMatchId = bettingMatchId;
    }

}
