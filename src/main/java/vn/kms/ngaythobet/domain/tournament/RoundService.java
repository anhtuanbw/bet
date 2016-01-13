package vn.kms.ngaythobet.domain.tournament;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<Competitor> getCompetitorsByCompetitorIds(List<Long> competitorIds) {
        List<Competitor> competitors = new ArrayList<>();
        competitorIds.forEach(competitorId -> {
            competitors.add(competitorRepo.getOne(competitorId));
        });
        return competitors;
    }

    private boolean competitorsIsExistedTournament(List<Competitor> competitors, long tournamentId) {
        return (competitors.stream().filter(competitor -> competitor.getTournament().getId().equals(tournamentId))
                .count() == competitors.size());
    }

    private boolean roundNameIsExistedInTournament(String roundName, long tournamentId) {
        return (getRoundByTournamentId(tournamentId).stream().filter(round -> round.getName().equals(roundName))
                .count() != 0);
    }

    private boolean competitorsIsExistedInRound(List<Long> newCompetitorIds, long roundId) {
        List<Long> currentCompetitorIds = roundRepo.getOne(roundId).getCompetitors().stream()
                .map(competitor -> competitor.getId()).collect(Collectors.toList());
        return (currentCompetitorIds.stream()
                .filter(currentCompetitorId -> newCompetitorIds.contains(currentCompetitorId)).count() != 0);
    }

    public void createRound(CreateRoundInfo createRoundInfo) {
        if (!roundNameIsExistedInTournament(createRoundInfo.getName(), createRoundInfo.getTournamentId())) {
            Round round = new Round();
            round.setName(createRoundInfo.getName());
            Tournament tournament = tournamentRepo.getOne(createRoundInfo.getTournamentId());
            round.setTournament(tournament);
            if (createRoundInfo.getCompetitorIds() != null) {
                List<Competitor> competitors = new ArrayList<>();
                competitors = getCompetitorsByCompetitorIds(createRoundInfo.getCompetitorIds());
                round.setCompetitors(competitors);
                roundRepo.save(round);
            } else {
                roundRepo.save(round);
            }
        } else {
            throw new DataInvalidException("exception.round.existed.in.this.tournament");
        }
    }

    public void updateRound(UpdateRoundInfo updateRoundInfo) {
        if (!competitorsIsExistedInRound(updateRoundInfo.getCompetitorIds(), updateRoundInfo.getRoundId())) {
            Round round = roundRepo.getOne(updateRoundInfo.getRoundId());
            long tournamentId = round.getTournament().getId();
            if (updateRoundInfo.getCompetitorIds() != null) {
                List<Competitor> newCompetitors = new ArrayList<>();
                newCompetitors = getCompetitorsByCompetitorIds(updateRoundInfo.getCompetitorIds());
                if (competitorsIsExistedTournament(newCompetitors, tournamentId)) {
                    List<Competitor> currentCompetitors = round.getCompetitors();
                    currentCompetitors.addAll(newCompetitors);
                    round.setCompetitors(currentCompetitors);
                    roundRepo.save(round);
                } else {
                    throw new DataInvalidException("exception.competitor.not-exist-tournament");
                }
            } else {
                roundRepo.save(round);
            }
        } else {
            throw new DataInvalidException("exception.competitor.existed.in.this.round");
        }
    }

    @Transactional(readOnly = true)
    public List<Round> getRoundByTournamentId(Long tournamentId) {
        return roundRepo.findByTournamentId(tournamentId);
    }

    @Transactional(readOnly = true)
    public Round findByBettingMatchId(Long bettingMatchId) {
        return roundRepo.findByBettingMatchId(bettingMatchId);
    }

}
