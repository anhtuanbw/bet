package vn.kms.ngaythobet.domain.tournament;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import vn.kms.ngaythobet.BaseTest;
import vn.kms.ngaythobet.domain.tournament.CompetitorRepository;
import vn.kms.ngaythobet.domain.tournament.RoundRepository;
import vn.kms.ngaythobet.domain.tournament.RoundService;
import vn.kms.ngaythobet.domain.tournament.Tournament;
import vn.kms.ngaythobet.domain.tournament.TournamentRepository;
import vn.kms.ngaythobet.web.dto.CreateRoundInfo;

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

    @Override
    protected void doStartUp() {
        Tournament tournament = new Tournament();
        tournament.setActivated(false);
        tournament.setName("Euro");
        tournamentTemp = tournamentRepo.save(tournament);
    }

    @Test
    public void testCreateRound() {
        CreateRoundInfo createRoundInfo = new CreateRoundInfo();
        createRoundInfo.setName("vong chung ket");
        createRoundInfo.setTournamentId(tournamentTemp.getId());
        roundService.createRound(createRoundInfo);
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
        competitorRepo.deleteAll();
        roundRepo.deleteAll();
        tournamentRepo.deleteAll();
    }
}
