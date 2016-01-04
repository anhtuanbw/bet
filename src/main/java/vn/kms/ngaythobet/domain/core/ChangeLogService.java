// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.core;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.kms.ngaythobet.domain.betting.BettingMatch;
import vn.kms.ngaythobet.domain.betting.BettingMatchRepository;
import vn.kms.ngaythobet.domain.betting.BettingPlayer;
import vn.kms.ngaythobet.domain.betting.BettingPlayerRepository;
import vn.kms.ngaythobet.domain.core.ChangeLog.Change;
import vn.kms.ngaythobet.web.dto.CommentInfo;

@Service
@Transactional
public class ChangeLogService {

    private final ChangeLogRepository changeLogRepo;
    private final BettingMatchRepository bettingMatchRepo;
    private final BettingPlayerRepository bettingPlayerRepo;
    private final UserRepository userRepo;

    @Autowired
    public ChangeLogService(ChangeLogRepository changeLogRepo,
            BettingMatchRepository bettingMatchRepo,
            BettingPlayerRepository bettingPlayerRepo, UserRepository userRepo) {
        this.changeLogRepo = changeLogRepo;
        this.bettingMatchRepo = bettingMatchRepo;
        this.bettingPlayerRepo = bettingPlayerRepo;
        this.userRepo = userRepo;
    }

    @Transactional(readOnly = true)
    public CommentInfo getRecentComment(Long bettingMatchId) {
        ChangeLog changelog = changeLogRepo.findFirst1ByEntityTypeAndEntityIdOrderByTimestampDesc(
                BettingMatch.class.getCanonicalName(), bettingMatchId);
        CommentInfo commentInfo = null;
        if (changelog != null) {
            commentInfo = new CommentInfo();
            commentInfo.setUsername(changelog.getUsername());
            commentInfo.setTimestamp(changelog.getTimestamp());

            User user = userRepo.findOneByUsername(changelog.getUsername()).get();
            BettingMatch bettingMatch = bettingMatchRepo.findOne(changelog.getEntityId());

            BettingPlayer bettingPlayer = bettingPlayerRepo.findByPlayerAndBettingMatch(user, bettingMatch);
            commentInfo.setBetCompetitor(bettingPlayer.getBetCompetitor());

            Map<String, Change> entityChange = changelog.getEntityChanges();
            if (entityChange != null && entityChange.get("comment") != null) {
                Change change = entityChange.get("comment");
                commentInfo.setComment(change.getNewValue().toString());
            }
        }
        return commentInfo;
    }
}