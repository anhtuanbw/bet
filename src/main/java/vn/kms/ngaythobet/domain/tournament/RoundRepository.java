// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.tournament;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoundRepository extends JpaRepository<Round, Long> {
    Round findByName(String name);
    List<Round> findAllByOrderByCreatedAtDesc();
    List<Round> findByTournamentId(Long tournament_id);
    Round findByNameAndTournament(String name, Tournament tournament);
    @Query("select r from BettingMatch bm "
            + "inner join bm.match m "
            + "inner join m.round r "
            + "where bm.id = :bettingMatchId")
    Round findByBettingMatchId(@Param("bettingMatchId") Long bettingMatchId);
}
