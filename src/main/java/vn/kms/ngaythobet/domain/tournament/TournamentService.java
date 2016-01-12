// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.tournament;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.kms.ngaythobet.domain.core.User;
import vn.kms.ngaythobet.domain.core.UserRepository;
import vn.kms.ngaythobet.domain.util.SecurityUtil;
import vn.kms.ngaythobet.web.dto.CreateTournamentInfo;

@Service
@Transactional
public class TournamentService {
    private final TournamentRepository tournamentRepo;

    private final CompetitorRepository competitorRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    public TournamentService(TournamentRepository tournamentRepo, CompetitorRepository competitorRepo) {
        this.tournamentRepo = tournamentRepo;
        this.competitorRepo = competitorRepo;
    }

    public void createTournament(CreateTournamentInfo tournamentInfo) {
        Tournament tournament = new Tournament();
        tournament.setName(tournamentInfo.getName());
        tournament.setNumOfCompetitor((long) tournamentInfo.getCompetitors().size());
        tournament.setActivated(tournamentInfo.isActive());
        tournamentRepo.save(tournament);
        tournamentInfo.getCompetitors().forEach(competitorName -> {
            Competitor competitor = new Competitor(tournament, competitorName);
            competitorRepo.save(competitor);
        });
    }

    @Transactional(readOnly = true)
    public List<Tournament> findAllTournament() {
        return tournamentRepo.findAllByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public Tournament findById(Long id) {
        return tournamentRepo.findOne(id);
    }

    @Transactional
    public void activateTournament(long tournamentId) {
        Tournament tournament = tournamentRepo.findOne(tournamentId);
        if (tournament != null) {
            tournament.setActivated(true);
            tournamentRepo.save(tournament);
        }
    }

    public List<Tournament> findAllTournamentOfUser() {
        String username = SecurityUtil.getCurrentLogin();
        User user = userRepo.findOneByUsername(username).get();
        List<Group> groups = user.getGroups();
        if (!user.getRole().equals(User.Role.ADMIN)) {
            List<Tournament> tournaments = user.getGroups().stream().map(group -> group.getTournament()).distinct()
                    .filter(tournament -> tournament.isActivated()).collect(Collectors.toList());
            tournaments.forEach(tournament -> tournament.getGroups().retainAll(groups));
            return tournaments;
        }
        return tournamentRepo.findAll();
    }
}
