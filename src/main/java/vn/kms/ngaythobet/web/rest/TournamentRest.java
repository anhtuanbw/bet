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

import vn.kms.ngaythobet.domain.tournament.Tournament;
import vn.kms.ngaythobet.domain.tournament.TournamentService;
import vn.kms.ngaythobet.web.dto.CreateTournamentInfo;

@RestController
@RequestMapping("/api/tournaments")
public class TournamentRest {
    private final TournamentService tournamentService;

    @Autowired
    public TournamentRest(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @RequestMapping(value = "/create", method = POST)
    public void createTournament(@Valid @RequestBody CreateTournamentInfo tournamentInfo) {
        tournamentService.createTournament(tournamentInfo);
    }

    @RequestMapping(value = "/findAll", method = GET)
    public List<Tournament> getAllTournament() {
        return tournamentService.findAllTournament();
    }

    @RequestMapping(value = "/active", method = GET)
    public void activeTournament(@RequestParam Long tournamentId) {
        tournamentService.activateTournament(tournamentId);
    }

    @RequestMapping(value = "/getById", method = GET)
    public Tournament getById(@RequestParam Long tournamentId) {
        return tournamentService.findById(tournamentId);
    }

    @RequestMapping(value = "/findByRole", method = GET)
    public List<Tournament> getAllByRole() {
        return tournamentService.findAllTournamentOfUser();
    }
}
