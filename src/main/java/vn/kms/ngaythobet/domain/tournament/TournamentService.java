// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.tournament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TournamentService {
    private final TournamentRepository tournamentRepo;

    @Autowired
    public TournamentService(TournamentRepository tournamentRepo) {
        this.tournamentRepo = tournamentRepo;
    }

    @Transactional(readOnly = true)
    public Tournament findTournamentById(Long id) {
        return tournamentRepo.findOne(id);
    }
}
