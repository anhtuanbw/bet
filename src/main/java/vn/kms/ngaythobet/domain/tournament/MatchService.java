// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.tournament;

import vn.kms.ngaythobet.domain.util.DataInvalidException;
import vn.kms.ngaythobet.web.dto.CreateMatchInfo;
import vn.kms.ngaythobet.web.dto.UpdateScoreInfo;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static vn.kms.ngaythobet.domain.util.Constants.*;

@Service
@Transactional
public class MatchService {

    private final CompetitorRepository competitorRepo;

    private final RoundRepository roundRepo;

    private final MatchRepository matchRepo;

    @Autowired
    public MatchService(CompetitorRepository competitorRepo,
            RoundRepository roundRepo, MatchRepository matchRepo) {
        this.competitorRepo = competitorRepo;
        this.roundRepo = roundRepo;
        this.matchRepo = matchRepo;
    }

    @Transactional
    public void createMatch(CreateMatchInfo createMatchInfo) {
        Long roundId = createMatchInfo.getRound();

        Competitor competitor1 = competitorRepo.findOne(createMatchInfo
                .getCompetitor1());
        Competitor competitor2 = competitorRepo.findOne(createMatchInfo
                .getCompetitor2());

        Tournament tournament = null;
        Round round = null;

        if (competitor1.getTournament().getId()
                .equals(competitor2.getTournament().getId())) {
            tournament = competitor1.getTournament();
        } else {
            throw new DataInvalidException(
                    "exception.competitor.not-exist-tournament");
        }

        if (roundId == null || roundId.equals(0)) {
            round = roundRepo.findByNameAndTournament(DEFAULT_ROUND_NAME,
                    tournament);
            if (round == null) {
                round = new Round();
                round.setName(DEFAULT_ROUND_NAME);
                round.setTournament(tournament);

                round = roundRepo.save(round);
            }

            matchRepo.save(createMatchWithInfo(competitor1, competitor2,
                    createMatchInfo.getTime(), createMatchInfo.getLocation(),
                    createMatchInfo.getComment(), round));
        } else {
            round = roundRepo.findOne(roundId);
            if (round == null) {
                throw new DataInvalidException(
                        "exception.round.not-existed.message");
            }

            tournament = round.getTournament();

            if (!tournament.getId().equals(competitor1.getTournament().getId())
                    || !tournament.getId().equals(
                            competitor2.getTournament().getId())) {
                throw new DataInvalidException(
                        "exception.competitor.not-exist-tournament");
            }

            List<Competitor> competitors = competitorRepo.findByRounds(round);
            int countBelongRound = 0;
            for (Competitor competitor : competitors) {
                if (competitor.getId().equals(competitor1.getId())
                        || competitor.getId().equals(competitor2.getId())) {
                    countBelongRound++;
                }

                if (countBelongRound == 2) {
                    break;
                }
            }

            if (countBelongRound != 2) {
                throw new DataInvalidException(
                        "exception.matchService.createMatch.competitor-not-belong-round");
            }

            if (competitor1.getTournament().getId().equals(tournament.getId())
                    && competitor2.getTournament().getId()
                            .equals(tournament.getId())) {

                matchRepo.save(createMatchWithInfo(competitor1, competitor2,
                        createMatchInfo.getTime(),
                        createMatchInfo.getLocation(),
                        createMatchInfo.getComment(), round));
            } else {
                throw new DataInvalidException(
                        "exception.matchService.createMatch.not-exist-tournament");
            }
        }
    }

    private Match createMatchWithInfo(Competitor competitor1,
            Competitor competitor2, LocalDateTime time, String location,
            String comment, Round round) {
        Match match = new Match();
        match.setCompetitor1(competitor1);
        match.setCompetitor2(competitor2);
        match.setMatchTime(time);
        match.setLocation(location);
        match.setComment(comment);
        match.setRound(round);
        return match;
    }

    @Transactional(readOnly = true)
    public List<Competitor> getCompetitors(Long roundId) {
        Round round = roundRepo.getOne(roundId);
        return competitorRepo.findByRounds(round);
    }

    @Transactional(readOnly = true)
    public List<Round> getRounds(Long tournamentId) {
        return roundRepo.findByTournamentId(tournamentId);
    }

    @Transactional(readOnly = true)
    public Match getMatch(Long matchId) {
        return matchRepo.findOne(matchId);
    }

    @Transactional
    public void updateMatch(long matchId, long competitor1Id,
            long competitor2Id, LocalDateTime time, String location) {
        // TODO: Consider to only update competitor1Id/competitor2Id if it is
        // NULL
    }

    @Transactional
    public void updateScore(UpdateScoreInfo updateScoreInfo) {
        Match match = matchRepo.findOne(updateScoreInfo.getMatchId());
        if (LocalDateTime.now().isBefore(match.getMatchTime())) {
            throw new DataInvalidException(
                    "validation.matchService.updateScore.message");
        }
        match.setScore1(updateScoreInfo.getCompetitor1Score());
        match.setScore2(updateScoreInfo.getCompetitor2Score());
        matchRepo.save(match);
    }

    @Transactional(readOnly = true)
    public Map<String, List<Match>> getFixtures(long tournamentId) {
        Map<String, List<Match>> fixtures = new LinkedHashMap<>();

        // TODO: return the list of all matches in a tournament group by
        // round.name and order by round.index, time

        return fixtures;
    }
}
