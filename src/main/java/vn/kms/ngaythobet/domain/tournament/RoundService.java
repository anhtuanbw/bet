package vn.kms.ngaythobet.domain.tournament;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.kms.ngaythobet.web.dto.CreateRoundInfo;

@Service
public class RoundService {
    private final RoundRepository roundRepo;
    private final TournamentRepository tournamentRepo;
    private final CompetitorRepository competitorRepo;

    @Autowired
    public RoundService(RoundRepository roundRepo, TournamentRepository tournamentRepo,
            CompetitorRepository competitorRepo) {
        this.roundRepo = roundRepo;
        this.tournamentRepo = tournamentRepo;
        this.competitorRepo = competitorRepo;
    }

    @Transactional
    public void createRound(CreateRoundInfo createRoundInfo) {
        Round round = new Round();
        round.setName(createRoundInfo.getName());
        round.setTournament(tournamentRepo.getOne(createRoundInfo.getTournamentId()));
        List<Competitor> competitors = new ArrayList<>();
        createRoundInfo.getCompetitorId().forEach(Long -> {
            competitors.add(competitorRepo.getOne(Long));
        });
        round.setCompetitors(competitors);
        roundRepo.save(round);
    }

    @Transactional
    public List<Round> getRoundByTournamentId(Long tournamentId) {
        return roundRepo.findByTournamentId(tournamentId);
    }
}
