// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.betting;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BettingMatchRepository extends
        JpaRepository<BettingMatch, Long> {
    List<BettingMatch> findAllByOrderByCreatedAtDesc();

    @Query(value = "select bMatch from BettingMatch bMatch inner join bMatch.group gr inner join gr.members u where gr.id = :groupId and u.username = :username")
    List<BettingMatch> findByGroupIdAndUsername(@Param("groupId") Long groupId,
            @Param("username") String username);
}
