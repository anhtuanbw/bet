package vn.kms.ngaythobet.domain.tournament;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.List;


import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import vn.kms.ngaythobet.BaseTest;
import vn.kms.ngaythobet.domain.tournament.CompetitorRepository;
import vn.kms.ngaythobet.domain.tournament.RoundRepository;
import vn.kms.ngaythobet.domain.tournament.RoundService;
import vn.kms.ngaythobet.domain.tournament.Tournament;
import vn.kms.ngaythobet.domain.tournament.TournamentRepository;
import vn.kms.ngaythobet.web.dto.CreateRoundInfo;
import vn.kms.ngaythobet.web.dto.UpdateRoundInfo;

public class RoundServiceTest extends BaseTest {

    @Autowired
    private TournamentRepository tournamentRepo;

    @Autowired
    private RoundRepository roundRepo;

    @Autowired
    private CompetitorRepository competitorRepo;

    @Autowired
    private RoundService roundService;

    private Tournament tournamentTemp;
    private Round roundTemp;
    private List<Competitor> competitors;
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Override
    protected void doStartUp() {
        Tournament tournament = new Tournament();
        tournament.setActivated(false);
        tournament.setName("Euro");
        tournamentTemp = tournamentRepo.save(tournament);
        Competitor competitor1 = new Competitor();
        Competitor competitor2 = new Competitor();
        competitor1.setName("England");
        competitor1.setTournament(tournamentTemp);
        competitorRepo.save(competitor1);
        competitor2.setName("France");
        competitor2.setTournament(tournamentTemp);
        competitorRepo.save(competitor2);
        competitors = competitorRepo.findAll();
        tournamentTemp = tournamentRepo.save(tournament);

    }

    @Test
    public void testCreateRound() {
        CreateRoundInfo createRoundInfo = new CreateRoundInfo();
        createRoundInfo.setName("final round");
        createRoundInfo.setTournamentId(tournamentTemp.getId());
        List<Long> competitorIds = new ArrayList<>();
        for (Competitor competitor : competitors) {
            competitorIds.add(competitor.getId());
        }
        createRoundInfo.setCompetitorIds(competitorIds);
        roundService.createRound(createRoundInfo);
        assertThat(roundRepo.findAllByOrderByCreatedAtDesc().size(), equalTo(1));
        createRoundInfo.setName("final round 2");
        createRoundInfo.setTournamentId(tournamentTemp.getId());
        createRoundInfo.setCompetitorIds(null);
        roundService.createRound(createRoundInfo);
        assertThat(roundRepo.findAllByOrderByCreatedAtDesc().size(), equalTo(2));
        
        exception.expectMessage("{exception.round.existed.in.this.tournament}");
        roundService.createRound(createRoundInfo);
    }

    @Test
    public void testUpdateRound() {
        Round round = new Round();
        round.setName("final round");
        round.setTournament(tournamentTemp);
        roundTemp = roundRepo.save(round);
        UpdateRoundInfo updateRoundInfo = new UpdateRoundInfo();
        updateRoundInfo.setRoundId(roundTemp.getId());
        List<Long> competitorIds = new ArrayList<>();
        for (Competitor competitor : competitors) {
            competitorIds.add(competitor.getId());
        }
        updateRoundInfo.setCompetitorIds(competitorIds);
        roundService.updateRound(updateRoundInfo);
        assertThat(roundRepo.findAllByOrderByCreatedAtDesc().size(), equalTo(1));
    }

    @Test
    public void testGetRoundByTournamentId() {
        Round round = new Round();
        round.setName("World Cup");
        round.setTournament(tournamentTemp);
        roundRepo.save(round);
        Long tournamentId = tournamentTemp.getId();
        List<Round> rounds = roundService.getRoundByTournamentId(tournamentId);
        assertThat(rounds.size(), equalTo(1));
    }

    @After
    public void clearData() {
        roundRepo.deleteAll();
        competitorRepo.deleteAll();
        tournamentRepo.deleteAll();
    }
}
