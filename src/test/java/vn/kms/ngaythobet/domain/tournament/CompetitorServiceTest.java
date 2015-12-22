package vn.kms.ngaythobet.domain.tournament;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import vn.kms.ngaythobet.BaseTest;

public class CompetitorServiceTest extends BaseTest {
    @Autowired
    private TournamentRepository tournamentRepo;

    @Autowired
    private CompetitorRepository competitorRepo;

    private CompetitorService competitorService;

    private Tournament temp;

    @Override
    protected void doStartUp() {
        competitorService = new CompetitorService(competitorRepo, tournamentRepo);
        Tournament tournament = new Tournament();
        tournament.setActivated(false);
        tournament.setName("Euro");
        temp = tournamentRepo.save(tournament);
        Competitor competitor1 = new Competitor(temp, "England");
        Competitor competitor2 = new Competitor(temp, "France");
        competitorRepo.save(competitor1);
        competitorRepo.save(competitor2);
    }

    @Test
    public void testFindByTournamentId() {
        assertThat(competitorService.findByTournamentId(temp.getId()).size(), equalTo(2));
    }

    @After
    public void clearData() {
        competitorRepo.deleteAll();
        tournamentRepo.deleteAll();
    }
}
