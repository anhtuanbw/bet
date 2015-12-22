// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.tournament;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.kms.ngaythobet.web.dto.CreateTournamentInfo;

@Service
@Transactional(readOnly = true)
public class TournamentService {
    private final TournamentRepository tournamentRepo;

    private final CompetitorRepository competitorRepo;

    @Autowired
    public TournamentService(TournamentRepository tournamentRepo, CompetitorRepository competitorRepo) {
        this.tournamentRepo = tournamentRepo;
        this.competitorRepo = competitorRepo;
    }

    @Transactional
    public void createTournament(CreateTournamentInfo tournamentInfo) {
        Tournament tournament = new Tournament();
        tournament.setName(tournamentInfo.getName());
        tournament.setNumOfCompetitor((long) tournamentInfo.getCompetitors().size());
        tournament.setActivated(tournamentInfo.isActive());
        tournamentRepo.save(tournament);

        tournamentInfo.getCompetitors().forEach(competitorName -> {
            Competitor competitor = new Competitor(tournament, competitorName);
            competitorRepo.save(competitor);
        });
    }

    @Transactional
    public List<Tournament> findAllTournament() {
        return tournamentRepo.findAllByOrderByCreatedAtDesc();
    }

    @Transactional
    public Tournament findById(Long id) {
        return tournamentRepo.findOneById(id);
    }

    @Transactional
    public void activateTournament(long tournamentId) {
        Tournament tournament = tournamentRepo.findOneById(tournamentId);
        tournament.setActivated(true);
        tournamentRepo.save(tournament);
    }
}
