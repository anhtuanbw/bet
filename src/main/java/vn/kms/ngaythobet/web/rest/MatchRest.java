// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.web.rest;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.kms.ngaythobet.domain.tournament.Competitor;
import vn.kms.ngaythobet.domain.tournament.MatchService;
import vn.kms.ngaythobet.domain.tournament.Round;
import vn.kms.ngaythobet.web.dto.CreateMatchInfo;



@RestController
@RequestMapping("/api/matches")
public class MatchRest {
    private final MatchService matchService;


    @Autowired
    public MatchRest(MatchService matchService) {
        this.matchService = matchService;
    }

    @RequestMapping(value = "/create-match", method = POST)
    public void createMatch(@Valid @RequestBody CreateMatchInfo createMatchInfo) {
        matchService.createMatch(createMatchInfo);
    }
    
    @RequestMapping(value = "/competitors", method = GET)
    public List<Competitor> getCompetitorsByRound(@RequestParam Long roundId) {
        return matchService.getCompetitors(roundId);
    }
    @RequestMapping(value = "/rounds", method = GET)
    public List<Round> getRoundsByTournament(@RequestParam Long tournamentId) {
        return matchService.getRounds(tournamentId);
    }
}
