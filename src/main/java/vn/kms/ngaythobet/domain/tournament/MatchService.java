// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.tournament;

import vn.kms.ngaythobet.domain.util.DataInvalidException;
import vn.kms.ngaythobet.web.dto.CreateMatchInfo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
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
        long roundId = Long.parseLong(createMatchInfo.getRound().trim());
        
        Long competitor1Id = Long.valueOf(createMatchInfo.getCompetitor1().trim());
        Long competitor2Id = Long.valueOf(createMatchInfo.getCompetitor2().trim());
        
        String location = createMatchInfo.getLocation().trim();
        String comment = createMatchInfo.getComment().trim();
        
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalDateTime time = LocalDateTime.parse(createMatchInfo.getTime().trim(),
                formatter);
        
        Round round = roundRepo.findById(roundId); 
        if(round == null){
            throw new DataInvalidException("exception.data-not-found");
        }
        Tournament tournament = round.getTournament();
        if (tournament == null) {
            throw new DataInvalidException("exception.data-not-found");
        }
        
        if(competitor1Id == null || competitor2Id == null){
            throw new DataInvalidException("exception.data-not-found");
        }
        
        
        Match match = new Match();
        match.setCompetitor1(competitorRepo.findById(competitor1Id));
        match.setCompetitor2(competitorRepo.findById(competitor2Id));
        match.setScore1(0);
        match.setScore2(0);
        match.setMatchTime(time);
        match.setLocation(location);
        match.setComment(comment);
        match.setRound(round);
        
        matchRepo.save(match);
    }
    
    @Transactional
    public void updateMatch(long matchId, long competitor1Id, long competitor2Id, LocalDateTime time, String location) {
        // TODO: Consider to only update competitor1Id/competitor2Id if it is NULL
    }
    
    @Transactional
    public void updateScore(long matchId, int competitor1Score, int competitor2Score) {

    }

    @Transactional
    public Map<String, List<Match>> getFixtures(long tournamentId) {
        Map<String, List<Match>> fixtures = new LinkedHashMap<>();

        // TODO: return the list of all matches in a tournament group by round.name and order by round.index, time

        return fixtures;
    }
}
