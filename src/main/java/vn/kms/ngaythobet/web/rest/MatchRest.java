// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.web.rest;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.kms.ngaythobet.domain.tournament.Competitor;
import vn.kms.ngaythobet.domain.tournament.Match;
import vn.kms.ngaythobet.domain.tournament.MatchService;
import vn.kms.ngaythobet.domain.tournament.Round;
import vn.kms.ngaythobet.web.dto.CreateMatchInfo;
import vn.kms.ngaythobet.web.dto.MatchNotCreateBetInfo;
import vn.kms.ngaythobet.web.dto.UpdateScoreInfo;

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

    @RequestMapping(value = "/competitors/{roundId}", method = GET)
    public List<Competitor> getCompetitorsByRound(@PathVariable Long roundId) {
        return matchService.getCompetitors(roundId);
    }

    @RequestMapping(value = "/rounds/{tournamentId}", method = GET)
    public List<Round> getRoundsByTournament(@PathVariable Long tournamentId) {
        return matchService.getRounds(tournamentId);
    }

    @RequestMapping(value = "/check-rounds/{tournamentId}", method = GET)
    public boolean checkRoundsByTournament(@PathVariable Long tournamentId) {
        return matchService.checkRounds(tournamentId);
    }

    @RequestMapping(value = "/check-round/{tournamentId}", method = GET)
    public boolean checkRoundsIfCreateMatch(@PathVariable Long tournamentId) {
        return matchService.checkIfCreateRound(tournamentId);
    }

    @RequestMapping(value = "/update-score", method = POST)
    public void updateScore(@Valid @RequestBody UpdateScoreInfo updateScoreInfo) {
        matchService.updateScore(updateScoreInfo);
    }

    @RequestMapping(value = "/getMatch/{matchId}", method = GET)
    public Match getMatch(@PathVariable Long matchId) {
        return matchService.getMatch(matchId);
    }

    @RequestMapping(value = "/getMatchNotCreateBettingMatch/{tournamentId}/{groupId}", method = GET)
    public List<MatchNotCreateBetInfo> getMatchNotCreateBettingMatch(@PathVariable Long tournamentId,
            @PathVariable Long groupId) {
        return matchService.getMatchNotCreatedBettingMatch(tournamentId, groupId);
    }
}
