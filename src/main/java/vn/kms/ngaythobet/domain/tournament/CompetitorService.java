package vn.kms.ngaythobet.domain.tournament;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CompetitorService {
    private final CompetitorRepository competitorRepo;
    private final TournamentRepository tournamentRepo;

    @Autowired
    public CompetitorService(CompetitorRepository competitorRepo, TournamentRepository tournamentRepo) {
        this.competitorRepo = competitorRepo;
        this.tournamentRepo = tournamentRepo;
    }

    @Transactional(readOnly = true)
    public List<Competitor> findByTournamentId(Long tournamentId) {
        Tournament tournament = tournamentRepo.findOne(tournamentId);
        return competitorRepo.findByTournament(tournament);
    }
}
