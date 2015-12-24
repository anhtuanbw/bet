// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.tournament;

import vn.kms.ngaythobet.domain.core.User;
import vn.kms.ngaythobet.domain.core.UserRepository;
import vn.kms.ngaythobet.domain.util.DataInvalidException;
import vn.kms.ngaythobet.web.dto.CreateMatchInfo;

import java.time.LocalDateTime;
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
        Group group = groupRepo.findByModerator(user);
        //TODO: check permission for Role MOD

        if (group != null) {
            Round round = roundRepo.findOne(createMatchInfo.getRound());
            Tournament tournament = round.getTournament();

            Competitor competitor1 = competitorRepo.getOne(createMatchInfo
                    .getCompetitor1());
            Competitor competitor2 = competitorRepo.getOne(createMatchInfo
                    .getCompetitor2());

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
        } else {
            throw new DataInvalidException("exception.matchService.permission");
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

    @Transactional
    public void updateMatch(long matchId, long competitor1Id,
            long competitor2Id, LocalDateTime time, String location) {
        // TODO: Consider to only update competitor1Id/competitor2Id if it is
        // NULL
    }

    @Transactional
    public void updateScore(long matchId, int competitor1Score,
            int competitor2Score) {
        // TODO
    }

    @Transactional(readOnly = true)
    public Map<String, List<Match>> getFixtures(long tournamentId) {
        Map<String, List<Match>> fixtures = new LinkedHashMap<>();

        // TODO: return the list of all matches in a tournament group by
        // round.name and order by round.index, time

        return fixtures;
    }
}
