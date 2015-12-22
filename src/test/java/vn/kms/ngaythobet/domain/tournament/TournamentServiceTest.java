package vn.kms.ngaythobet.domain.tournament;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import vn.kms.ngaythobet.BaseTest;
import vn.kms.ngaythobet.web.dto.CreateTournamentInfo;

public class TournamentServiceTest extends BaseTest {

    @Autowired
    private TournamentRepository tournamentRepo;

    @Autowired
    private CompetitorRepository competitorRepo;

    private TournamentService tournamentService;

    private Tournament temp;

    @Override
    protected void doStartUp() {
        tournamentService = new TournamentService(tournamentRepo, competitorRepo);
        Tournament tournament = new Tournament();
        tournament.setActivated(false);
        tournament.setName("Euro");
        Competitor competitor1 = new Competitor();
        Competitor competitor2 = new Competitor();
        competitor1.setName("England");
        competitor2.setName("France");
        List<Competitor> competitors = new ArrayList<>();
        competitors.add(competitor1);
        competitors.add(competitor2);
        tournament.setCompetitors(competitors);
        temp = tournamentRepo.save(tournament);
    }

    @Test
    public void testFindAllTournament() {
        assertThat(tournamentService.findAllTournament().size(), equalTo(1));
    }

    @Test
    public void testCreateTournament() {
        CreateTournamentInfo tournamentInfo = new CreateTournamentInfo();
        List<String> competitors = new ArrayList<>();
        competitors.add("Germany");
        competitors.add("Netherland");
        tournamentInfo.setActive(true);
        tournamentInfo.setName("Wourld Cup");
        tournamentInfo.setCompetitors(competitors);
        tournamentService.createTournament(tournamentInfo);
        assertThat(tournamentService.findAllTournament().size(), equalTo(2));
    }

    @Test
    public void testFindById() {
        assertThat(tournamentService.findById(temp.getId()).getName(), equalTo("Euro"));
    }

    @Test
    public void testActivateTournament() {
        tournamentService.activateTournament(temp.getId());
        assertThat(tournamentService.findById(temp.getId()).isActivated(), equalTo(true));
    }

    @After
    public void clearData() {
        competitorRepo.deleteAll();
        tournamentRepo.deleteAll();
    }

}
