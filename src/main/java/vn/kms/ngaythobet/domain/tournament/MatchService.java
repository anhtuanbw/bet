// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.tournament;

import vn.kms.ngaythobet.domain.util.DataInvalidException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MatchService {
    private final TournamentRepository tournamentRepo;

    private final CompetitorRepository competitorRepo;

    private final RoundRepository roundRepo;

    private final MatchRepository matchRepo;

    public MatchService(TournamentRepository tournamentRepo, CompetitorRepository competitorRepo,
                        RoundRepository roundRepo, MatchRepository matchRepo) {
        this.tournamentRepo = tournamentRepo;
        this.competitorRepo = competitorRepo;
        this.roundRepo = roundRepo;
        this.matchRepo = matchRepo;
    }

    public void createMatch(long tournamentId, Long competitor1Id, Long competitor2Id,
                            LocalDateTime time, String location, String roundName) {
        Tournament tournament = null; //tournamentRepo.getOne(tournamentId);
        if (tournament == null) {
            throw new DataInvalidException("exception.data-not-found");
        }

        // TODO: competitorId may be NULL since we may not identify the competitor for a future match

        // TODO: validate competitor1Id/competitor2Id is valid competitors of tournament

        Round round = roundRepo.findByName(roundName);
        // TODO: validate if round is valid

        // TODO: save
    }

    public void updateMatch(long matchId, long competitor1Id, long competitor2Id, LocalDateTime time, String location) {
        // TODO: Consider to only update competitor1Id/competitor2Id if it is NULL
    }

    public void updateScore(long matchId, int competitor1Score, int competitor2Score) {

    }

    public Map<String, List<Match>> getFixtures(long tournamentId) {
        Map<String, List<Match>> fixtures = new LinkedHashMap<>();

        // TODO: return the list of all matches in a tournament group by round.name and order by round.index, time

        return fixtures;
    }
}
