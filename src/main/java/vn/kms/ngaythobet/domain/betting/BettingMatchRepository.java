// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.betting;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.kms.ngaythobet.domain.tournament.Group;
import vn.kms.ngaythobet.domain.tournament.Match;

public interface BettingMatchRepository extends JpaRepository<BettingMatch, Long>{
    List<BettingMatch> findAllByOrderByCreatedAtDesc();
    List<BettingMatch> findByMatch(Match match);
    BettingMatch findByGroupAndMatch(Group group,Match match);
    Optional<BettingMatch> findByIdAndActivated(Long id, boolean activated);
}
