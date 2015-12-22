// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.tournament;

import vn.kms.ngaythobet.domain.util.DataInvalidException;
import vn.kms.ngaythobet.web.dto.CreateMatchInfo;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        
        Round round = roundRepo.getOne(createMatchInfo.getRound());
        if(round == null){
            throw new DataInvalidException("exception.data-not-found");
        }
        Tournament tournament = round.getTournament();
        if (tournament == null) {
            throw new DataInvalidException("exception.data-not-found");
        }
        
        if(createMatchInfo.getCompetitor1() == null || createMatchInfo.getCompetitor2() == null){
            throw new DataInvalidException("exception.data-not-found");
        }
        
        Competitor competitor1 = competitorRepo.getOne(createMatchInfo.getCompetitor1());
        Competitor competitor2 = competitorRepo.getOne(createMatchInfo.getCompetitor2());
        
        if(competitor1 == null || competitor2 == null){
            throw new DataInvalidException("exception.data-not-found");
        }
        
        if(competitor1.getTournament().getId() == tournament.getId() && competitor2.getTournament().getId() == tournament.getId()){
            Match match = new Match();
            match.setCompetitor1(competitor1);
            match.setCompetitor2(competitor2);
            match.setMatchTime(createMatchInfo.getTime());
            match.setLocation(createMatchInfo.getLocation());
            match.setComment(createMatchInfo.getComment());
            match.setRound(round);
            matchRepo.save(match);
        }else{
            throw new DataInvalidException("exception.data-not-found");
        }
    }
    
    @Transactional
    public void updateMatch(long matchId, long competitor1Id, long competitor2Id, LocalDateTime time, String location) {
        // TODO: Consider to only update competitor1Id/competitor2Id if it is NULL
    }
    
    @Transactional
    public void updateScore(long matchId, int competitor1Score, int competitor2Score) {
        //TODO
    }

    @Transactional(readOnly = true)
    public Map<String, List<Match>> getFixtures(long tournamentId) {
        Map<String, List<Match>> fixtures = new LinkedHashMap<>();

        // TODO: return the list of all matches in a tournament group by round.name and order by round.index, time

        return fixtures;
    }
}
