// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.betting;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.kms.ngaythobet.domain.core.User;

public interface BettingPlayerRepository extends JpaRepository<BettingPlayer, Long> {
    BettingPlayer findByPlayerAndBettingMatch(User player, BettingMatch bettingMatch);
    @Query(value = "select bp from BettingPlayer bp "
            + "join bp.bettingMatch bm " + "join bp.player player "
            + "where player.username = :username and bm.id = :bettingMatchId")
    BettingPlayer findByUsernameAndBettingMatchId(
            @Param("username") String username,
            @Param("bettingMatchId") Long bettingMatchId);

    BettingPlayer findByPlayer(User user);

    List<BettingPlayer> findByBettingMatchIdAndBetCompetitorId(Long bettingMatchId, Long betCompetitorId);
}