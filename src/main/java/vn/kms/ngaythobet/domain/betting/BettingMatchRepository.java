// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.betting;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.kms.ngaythobet.domain.tournament.Round;

public interface BettingMatchRepository extends JpaRepository<BettingMatch, Long>{
}
