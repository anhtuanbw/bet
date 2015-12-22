package vn.kms.ngaythobet.web.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.kms.ngaythobet.domain.tournament.Competitor;
import vn.kms.ngaythobet.domain.tournament.CompetitorService;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/api/competitors")
public class CompetitorRest {
    private final CompetitorService competitorService;

    @Autowired
    public CompetitorRest(CompetitorService competitorService) {
        this.competitorService = competitorService;
    }

    @RequestMapping(value = "/findByTournamentId", method = GET)
    public List<Competitor> findCompetitorByTournamentId(@RequestParam Long tournamentId) {
        return competitorService.findByTournamentId(tournamentId);
    }
}
