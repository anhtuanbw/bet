// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.tournament;

import java.util.List;

public interface TournamentRepository {
    List<Tournament> findAllOrderByCreatedAtDesc();
    Tournament getOne(long tournamentId);
}
