// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.tournament;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MatchRepository extends JpaRepository<Match, Long>{
    List<Match> findByRound(Round round);

    @Query("select m from Tournament t "
            + "inner join t.rounds r "
            + "inner join r.matches m "
            + "where t.id = :tournamentId order by m.id")
    List<Match> findByTournament(@Param("tournamentId") Long tournamentId);
}