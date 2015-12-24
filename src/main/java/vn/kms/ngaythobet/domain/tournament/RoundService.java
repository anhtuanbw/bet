package vn.kms.ngaythobet.domain.tournament;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.kms.ngaythobet.domain.util.DataInvalidException;
import vn.kms.ngaythobet.web.dto.CreateRoundInfo;
import vn.kms.ngaythobet.web.dto.UpdateRoundInfo;

@Service
@Transactional
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

    public void createRound(CreateRoundInfo createRoundInfo) {
        Round round = new Round();
        round.setName(createRoundInfo.getName());
        Tournament tournament = tournamentRepo.getOne(createRoundInfo.getTournamentId());
        round.setTournament(tournament);
        List<Competitor> competitors = new ArrayList<>();
        if (createRoundInfo.getCompetitorIds() != null) {
            createRoundInfo.getCompetitorIds().forEach(competitorIds -> {
                competitors.add(competitorRepo.getOne(competitorIds));
            });
            round.setCompetitors(competitors);
            if (competitors.stream().filter(competitor -> competitor.getTournament().getId().equals(tournament.getId()))
                    .count() == competitors.size()) {
                roundRepo.save(round);
            } else {
                throw new DataInvalidException("exception.competitor.not-exist-tournament");
            }
        } else {
            roundRepo.save(round);
        }
    }

    public void updateRound(UpdateRoundInfo updateRoundInfo) {
        Round round = roundRepo.findOne(updateRoundInfo.getRoundId());
        long tournamentId = round.getTournament().getId();
        List<Competitor> competitors = new ArrayList<>();
        if (updateRoundInfo.getCompetitorIds() != null) {
            updateRoundInfo.getCompetitorIds().forEach(competitor -> {
                competitors.add(competitorRepo.getOne(competitor));
            });
            round.setCompetitors(competitors);
            if (competitors.stream().filter(competitor -> competitor.getTournament().getId().equals(tournamentId))
                    .count() == competitors.size()) {
                roundRepo.save(round);
            } else {
                throw new DataInvalidException("exception.competitor.not-exist-tournament");
            }
        } else {
            roundRepo.save(round);
        }
    }

    @Transactional(readOnly = true)
    public List<Round> getRoundByTournamentId(Long tournamentId) {
        return roundRepo.findByTournamentId(tournamentId);
    }

}
