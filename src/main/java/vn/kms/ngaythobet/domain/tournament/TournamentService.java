// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.tournament;

import org.springframework.transaction.annotation.Transactional;
import vn.kms.ngaythobet.domain.betting.BettingGroup;
import vn.kms.ngaythobet.domain.betting.BettingGroupRepository;
import vn.kms.ngaythobet.domain.util.DataInvalidException;
import vn.kms.ngaythobet.domain.util.SecurityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class TournamentService {
    private final TournamentRepository tournamentRepo;

    private final CompetitorRepository competitorRepo;

    private final RoundRepository roundRepo;

    private final BettingGroupRepository bettingGroupRepo;

    public TournamentService(TournamentRepository tournamentRepo, CompetitorRepository competitorRepo,
                             RoundRepository roundRepo, BettingGroupRepository bettingGroupRepo) {
        this.tournamentRepo = tournamentRepo;
        this.competitorRepo = competitorRepo;
        this.roundRepo = roundRepo;
        this.bettingGroupRepo = bettingGroupRepo;
    }

    @Transactional
    public void createTournament(String name, Set<String> competitorNames, List<String> rounds) {
        Tournament tournament = new Tournament(name);
        //tournamentRepo.save(tournament);

        competitorNames
            .forEach(competitorName -> {
                Competitor competitor = new Competitor(tournament, competitorName);
                //competitorRepo.save(competitor);
            });

        IntStream.range(0, rounds.size())
            .forEach(index -> {
                String roundName = rounds.get(index);
                Round round = new Round(tournament, roundName, index);
                //roundRepo.save(round);
            });
    }

    @Transactional
    public void activateTournament(long tournamentId) {
        Tournament tournament = null; //tournamentRepo.getOne(tournamentId);
        if (tournament == null) {
            throw new DataInvalidException("exception.data-not-found");
        }

        // TODO: A tournament should be activated only once

        tournament.setActive(true);
        //tournamentRepo.save(tournament);
    }

    public List<TournamentGroups> getTournamentGroups() {
        List<TournamentGroups> tournamentGroupsList = new ArrayList<>();

        String loginUser = SecurityUtil.getCurrentLogin();

        List<Tournament> tournaments = tournamentRepo.findAllOrderByCreatedAtDesc();
        List<BettingGroup> groups = null; //bettingGroupRepo.findAllOrderByTournamentId();

        // Note: we consider number of tournaments and groups aren't too large then we do filer in-memory
        // TODO: Filter by loginUser role (ADMIN/USER) and Moderator right
        // - Admin can see all tournaments and groups
        // - User only see tournaments and groups that he joined to play
        // - User with Moderator right can see all groups that he is moderator on that

        return tournamentGroupsList;
    }
}
