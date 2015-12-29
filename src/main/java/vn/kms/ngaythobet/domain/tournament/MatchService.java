// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.tournament;

import vn.kms.ngaythobet.domain.core.User;
import vn.kms.ngaythobet.domain.core.UserRepository;
import vn.kms.ngaythobet.domain.util.DataInvalidException;
import vn.kms.ngaythobet.web.dto.CreateMatchInfo;
import vn.kms.ngaythobet.web.dto.UpdateScoreInfo;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MatchService {

    private final TournamentRepository tournamentRepo;

    private final CompetitorRepository competitorRepo;

    private final RoundRepository roundRepo;

    private final MatchRepository matchRepo;

    private final GroupRepository groupRepo;

    private final UserRepository userRepo;

    @Autowired
    public MatchService(TournamentRepository tournamentRepo,
            CompetitorRepository competitorRepo, RoundRepository roundRepo,
            MatchRepository matchRepo, GroupRepository groupRepo,
            UserRepository userRepo) {
        this.tournamentRepo = tournamentRepo;
        this.competitorRepo = competitorRepo;
        this.roundRepo = roundRepo;
        this.matchRepo = matchRepo;
        this.groupRepo = groupRepo;
        this.userRepo = userRepo;
    }

    @Transactional
    public void createMatch(CreateMatchInfo createMatchInfo) {
        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        User user = userRepo.findOneByUsername(username).get();
        List<Group> groups = groupRepo.findByModerator(user);

        if (groups == null) {
            throw new DataInvalidException("exception.matchService.permission");
        }

        Round round = roundRepo.findOne(createMatchInfo.getRound());
        Tournament tournament = round.getTournament();

        boolean isModerator = false;
        if (groups.size() != 0) {
            for (Group group : groups) {
                if (group.getTournament().getId().equals(tournament.getId())) {
                    isModerator = true;
                    break;
                }
            }
        }

        if (!isModerator) {
            throw new DataInvalidException("exception.matchService.permission");
        }

        Competitor competitor1 = competitorRepo.findOne(createMatchInfo
                .getCompetitor1());
        Competitor competitor2 = competitorRepo.findOne(createMatchInfo
                .getCompetitor2());

        List<Competitor> competitors = competitorRepo.findByRounds(round);
        System.out.println("++++++++++++++++++++++++++ "+competitors.size());
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
                    "exception.matchService.competitor-belong-round");
        }

        List<Match> matches = matchRepo.findByRound(round);
        if (matches.size() != 0) {
            for (Match match : matches) {
                if ((match.getCompetitor1().getId().equals(competitor1.getId()))
                        || (match.getCompetitor2().getId().equals(competitor2
                                .getId()))) {
                    throw new DataInvalidException(
                            "exception.matchService.same-time-for-same-competitor");
                }
            }
        }

        if (competitor1.getTournament().getId().equals(tournament.getId())
                && competitor2.getTournament().getId()
                        .equals(tournament.getId())) {
            Match match = new Match();
            match.setCompetitor1(competitor1);
            match.setCompetitor2(competitor2);
            match.setMatchTime(createMatchInfo.getTime());
            match.setLocation(createMatchInfo.getLocation());
            match.setComment(createMatchInfo.getComment());
            match.setRound(round);
            matchRepo.save(match);
        } else {
            throw new DataInvalidException(
                    "exception.matchService.not-exist-tournament");
        }
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
            throw new DataInvalidException("validation.dateFormat.message");
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
