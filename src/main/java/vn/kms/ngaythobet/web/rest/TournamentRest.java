package vn.kms.ngaythobet.web.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.kms.ngaythobet.domain.tournament.Round;
import vn.kms.ngaythobet.domain.tournament.Tournament;
import vn.kms.ngaythobet.domain.tournament.TournamentService;

@RestController
@RequestMapping("/api/tournaments")
public class TournamentRest {
    private final TournamentService tournamentService;

    @Autowired
    public TournamentRest(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @RequestMapping(value = "/getRoundsInTournament/{tournament_id}", method = RequestMethod.POST)
    public List<Round> getRoundnTournament(@PathVariable("tournament_id") Long id) {
        Tournament tournament = tournamentService.findTournamentById(id);
        List<Round> rounds = tournament.getRounds();
        return rounds;
    }
}