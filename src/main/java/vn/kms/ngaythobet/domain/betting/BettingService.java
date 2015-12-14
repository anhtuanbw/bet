// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.betting;

import java.time.LocalDateTime;
import java.util.Set;

public class BettingService {

    public void createBettingMatch(BettingMatch bettingMatch) {

    }

    public void updateMatchBetBalance(int matchBetId, double competitor1Balance, double competitor2Balance) {

    }

    public void updateMatchBetAmount(int matchBetId, long amount) {

    }

    public void updateMatchBetExpiryTime(int matchBetId, LocalDateTime expiryTime) {

    }

    public void placeBet(int matchBetId, int betCompetitorId, String comment) {

    }

    public long getBetAmountResult(int playerId, int matchBetId) {
        return 0;
    }
}
