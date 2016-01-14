// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.tournament;

import static vn.kms.ngaythobet.domain.util.Constants.DEFAULT_ROUND_NAME;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.kms.ngaythobet.domain.betting.BettingMatch;
import vn.kms.ngaythobet.domain.betting.BettingMatchRepository;
import vn.kms.ngaythobet.domain.util.Constants;
import vn.kms.ngaythobet.domain.util.DataInvalidException;
import vn.kms.ngaythobet.web.dto.CreateMatchInfo;
import vn.kms.ngaythobet.web.dto.MatchNotCreateBetInfo;
import vn.kms.ngaythobet.web.dto.UpdateScoreInfo;

@Service
@Transactional
public class MatchService {

    private final CompetitorRepository competitorRepo;

    private final RoundRepository roundRepo;

    private final MatchRepository matchRepo;

    private final BettingMatchRepository bettingMatchRepo;

    private final GroupRepository groupRepo;

    @Autowired
    public MatchService(CompetitorRepository competitorRepo, RoundRepository roundRepo, MatchRepository matchRepo,
            BettingMatchRepository bettingMatchRepo, GroupRepository groupRepo) {
        this.competitorRepo = competitorRepo;
        this.roundRepo = roundRepo;
        this.matchRepo = matchRepo;
        this.bettingMatchRepo = bettingMatchRepo;
        this.groupRepo = groupRepo;
    }

    @Transactional
    public void createMatch(CreateMatchInfo createMatchInfo) {
        Long roundId = createMatchInfo.getRound();

        Competitor competitor1 = competitorRepo.findOne(createMatchInfo.getCompetitor1());
        Competitor competitor2 = competitorRepo.findOne(createMatchInfo.getCompetitor2());

        Tournament tournament = null;
        Round round = null;

        if (competitor1.getTournament().getId().equals(competitor2.getTournament().getId())) {
            tournament = competitor1.getTournament();
        } else {
            throw new DataInvalidException("exception.competitor.not-exist-tournament");
        }

        if (roundId == null) {
            round = roundRepo.findByNameAndTournament(DEFAULT_ROUND_NAME, tournament);
            if (round == null) {
                round = new Round();
                round.setName(DEFAULT_ROUND_NAME);
                round.setTournament(tournament);

                round = roundRepo.save(round);
            }

            matchRepo.save(createMatchWithInfo(competitor1, competitor2, createMatchInfo.getTime(),
                    createMatchInfo.getLocation(), createMatchInfo.getComment(), round));
        } else {
            round = roundRepo.findOne(roundId);
            if (round == null) {
                throw new DataInvalidException("exception.round.not-existed.message");
            }

            tournament = round.getTournament();

            if (!tournament.getId().equals(competitor1.getTournament().getId())
                    || !tournament.getId().equals(competitor2.getTournament().getId())) {
                throw new DataInvalidException("exception.competitor.not-exist-tournament");
            }

            List<Competitor> competitors = competitorRepo.findByRounds(round);
            int countBelongRound = 0;
            for (Competitor competitor : competitors) {
                if (competitor.getId().equals(competitor1.getId()) || competitor.getId().equals(competitor2.getId())) {
                    countBelongRound++;
                }

                if (countBelongRound == 2) {
                    break;
                }
            }

            if (countBelongRound != 2) {
                throw new DataInvalidException("exception.matchService.createMatch.competitor-not-belong-round");
            }

            if (competitor1.getTournament().getId().equals(tournament.getId())
                    && competitor2.getTournament().getId().equals(tournament.getId())) {

                matchRepo.save(createMatchWithInfo(competitor1, competitor2, createMatchInfo.getTime(),
                        createMatchInfo.getLocation(), createMatchInfo.getComment(), round));
            } else {
                throw new DataInvalidException("exception.matchService.createMatch.not-exist-tournament");
            }
        }
    }

    private Match createMatchWithInfo(Competitor competitor1, Competitor competitor2, LocalDateTime time,
            String location, String comment, Round round) {
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
    public boolean checkRounds(Long tournamentId) {
        List<Round> rounds = roundRepo.findByTournamentId(tournamentId);
        boolean checked = true;

        if (rounds == null || rounds.size() == 0) {
            checked = false;
        } else if (rounds != null && rounds.size() == 1) {
            if (rounds.get(0).getName().equals(Constants.DEFAULT_ROUND_NAME)) {
                checked = false;
            }
        }
        return checked;
    }

    @Transactional(readOnly = true)
    public boolean checkIfCreateRound(Long tournamentId) {
        List<Round> rounds = roundRepo.findByTournamentId(tournamentId);
        boolean checked = true;

        if (rounds != null && rounds.size() == 1) {
            if (rounds.get(0).getName().equals(Constants.DEFAULT_ROUND_NAME)) {
                checked = false;
            }
        }
        return checked;
    }

    @Transactional(readOnly = true)
    public Match getMatch(Long matchId) {
        return matchRepo.findOne(matchId);
    }

    @Transactional
    public void updateScore(UpdateScoreInfo updateScoreInfo) {
        Match match = matchRepo.findOne(updateScoreInfo.getMatchId());
        if (LocalDateTime.now().isBefore(match.getMatchTime())) {
            throw new DataInvalidException("validation.matchService.updateScore.message");
        }
        match.setScore1(updateScoreInfo.getCompetitor1Score());
        match.setScore2(updateScoreInfo.getCompetitor2Score());
        matchRepo.save(match);
    }

    public List<MatchNotCreateBetInfo> getMatchNotCreatedBettingMatch(Long tournamentId, Long groupId) {
        List<Match> matches = matchRepo.findByTournament(tournamentId);
        List<MatchNotCreateBetInfo> matchesNotbet = new ArrayList<MatchNotCreateBetInfo>();
        Group group = groupRepo.getOne(groupId);
        List<BettingMatch> bettingMatches = bettingMatchRepo.findByGroup(group);
        int current = -1;
        for (Match match : matches) {
            boolean isExist = false;
            for (BettingMatch bettingMatch : bettingMatches) {
                if (match.getId().equals(bettingMatch.getMatch().getId())) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                if (current == -1 || !matchesNotbet.get(current).getRoundId().equals(match.getRound().getId())) {
                    current++;
                    matchesNotbet.add(new MatchNotCreateBetInfo());
                    matchesNotbet.get(current).setRoundId(match.getRound().getId());
                    matchesNotbet.get(current).setRoundName(match.getRound().getName());
                }
                matchesNotbet.get(current).getMatches().add(match);
            }
        }
        return matchesNotbet;
    }
}
