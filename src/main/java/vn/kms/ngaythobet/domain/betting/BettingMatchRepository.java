// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.betting;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.kms.ngaythobet.domain.tournament.Group;
import vn.kms.ngaythobet.domain.tournament.Match;

public interface BettingMatchRepository extends JpaRepository<BettingMatch, Long> {
    List<BettingMatch> findAllByOrderByCreatedAtDesc();

    List<BettingMatch> findByMatch(Match match);

    List<BettingMatch> findByGroup(Group group);

    List<BettingMatch> findByGroupId(Long groupId);

    BettingMatch findByGroupAndMatch(Group group, Match match);

    Optional<BettingMatch> findByIdAndActivated(Long id, boolean activated);

    @Query(value = "select bMatch from BettingMatch bMatch inner join bMatch.group gr inner join gr.members u where gr.id = :groupId and u.username = :userName ")
    List<BettingMatch> findByGroupIdAndUsername(@Param("groupId") Long groupId, @Param("userName") String userName);
}
