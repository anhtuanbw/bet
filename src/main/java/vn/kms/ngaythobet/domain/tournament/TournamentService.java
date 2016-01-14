// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.tournament;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import vn.kms.ngaythobet.domain.core.User;
import vn.kms.ngaythobet.domain.core.UserRepository;
import vn.kms.ngaythobet.domain.util.Constants;
import vn.kms.ngaythobet.domain.util.DataInvalidException;
import vn.kms.ngaythobet.domain.util.SecurityUtil;
import vn.kms.ngaythobet.web.dto.CreateTournamentInfo;

@Service
@Transactional
public class TournamentService {
    private final TournamentRepository tournamentRepo;
    private final CompetitorRepository competitorRepo;

    @Value("${upload.image.path}")
    private String UPLOAD_FILE_LOCATION;

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

    public void uploadTournmentImage(MultipartFile file, Long tournamentId) {
        Tournament tournament = tournamentRepo.findOne(tournamentId);
        if (!file.isEmpty() && isImage(file) && tournament != null) {
            deleteIfImageExisted(tournament);
            String fileName = createFileName(file);
            String filePath = UPLOAD_FILE_LOCATION + File.separator + fileName;
            try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(filePath))) {
                byte[] bytes = file.getBytes();
                stream.write(bytes);
                tournament.setImagePath(fileName);
                tournamentRepo.save(tournament);
            } catch (Exception exception) {
                throw new DataInvalidException("exception.cannot.upload.file", file.getOriginalFilename());
            }
        } else {
            throw new DataInvalidException("exception.cannot.upload.invalid.file");
        }
    }

    private String createFileName(MultipartFile file) {
        String fileName = RandomStringUtils.randomAlphanumeric(Constants.RANDOM_NAME_LENGTH)
                + Constants.FILE_NAME_DELIMETER + file.getOriginalFilename();
        File imageFilePath = new File(UPLOAD_FILE_LOCATION);
        if (!imageFilePath.exists()) {
            imageFilePath.mkdirs();
        }
        return fileName;
    }

    private boolean isImage(MultipartFile file) {
        String fileType = file.getContentType();
        if (fileType.contains("image")) {
            return true;
        }
        return false;
    }

    private void deleteIfImageExisted(Tournament tournament) {
        if (!tournament.getImagePath().isEmpty()) {
            File file = new File(tournament.getImagePath());
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
