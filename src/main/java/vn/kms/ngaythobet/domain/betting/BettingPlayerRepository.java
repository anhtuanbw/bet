// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.betting;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.kms.ngaythobet.domain.core.User;

public interface BettingPlayerRepository extends JpaRepository<BettingPlayer, Long> {
    BettingPlayer findByPlayerAndBettingMatch(User player, BettingMatch bettingMatch);

    List<BettingPlayer> findByBettingMatchIdAndBetCompetitorId(Long bettingMatchId, Long betCompetitorId);
}
