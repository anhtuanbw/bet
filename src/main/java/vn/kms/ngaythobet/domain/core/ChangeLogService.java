// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.kms.ngaythobet.domain.betting.BettingMatch;
import vn.kms.ngaythobet.domain.betting.BettingMatchRepository;
import vn.kms.ngaythobet.domain.betting.BettingPlayer;
import vn.kms.ngaythobet.domain.betting.BettingPlayerRepository;
import vn.kms.ngaythobet.domain.core.ChangeLog.Change;
import vn.kms.ngaythobet.domain.tournament.Competitor;
import vn.kms.ngaythobet.domain.tournament.Match;
import vn.kms.ngaythobet.web.dto.CommentInfo;
import vn.kms.ngaythobet.web.dto.HistoryBetting;

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
    public List<CommentInfo> getComments(Long bettingMatchId) {
        List<CommentInfo> comments = new ArrayList<CommentInfo>();
        List<ChangeLog> changeLogs = changeLogRepo.findByEntityTypeAndEntityId(
                BettingMatch.class.getCanonicalName(), bettingMatchId);

        Iterator<ChangeLog> iteratorChangeLogs = changeLogs.iterator();

        while (iteratorChangeLogs.hasNext()) {
            ChangeLog changelog = iteratorChangeLogs.next();

            CommentInfo commentInfo = new CommentInfo();
            commentInfo.setUsername(changelog.getUsername());
            commentInfo.setTimestamp(changelog.getTimestamp());

            User user = userRepo.findOneByUsername(changelog.getUsername())
                    .get();
            BettingMatch bettingMatch = bettingMatchRepo.findOne(changelog
                    .getEntityId());

            BettingPlayer bettingPlayer = bettingPlayerRepo
                    .findByPlayerAndBettingMatch(user, bettingMatch);
            commentInfo.setBetCompetitor(bettingPlayer.getBetCompetitor());

            Map<String, Change> entityChange = changelog.getEntityChanges();
            if (entityChange != null) {
                Change change = entityChange.get("comment");
                if (change != null) {
                    commentInfo.setComment(change.getNewValue().toString());
                }
            }
            comments.add(commentInfo);
        }
        return comments;
    }

    @Transactional(readOnly = true)
    public List<HistoryBetting> getHistoryBetting() {
        List<HistoryBetting> historyBettings = new ArrayList<HistoryBetting>();
        List<ChangeLog> changelogs = changeLogRepo
                .findByEntityType(BettingPlayer.class.getCanonicalName());

        Iterator<ChangeLog> iteratorChangeLogs = changelogs.iterator();

        while (iteratorChangeLogs.hasNext()) {
            ChangeLog changelog = iteratorChangeLogs.next();

            HistoryBetting historyBetting = new HistoryBetting();

            String username = changelog.getUsername();
            BettingMatch bettingMatch = bettingMatchRepo.findOne(changelog
                    .getEntityId());

            Match match = bettingMatch.getMatch();

            Competitor competitor1 = match.getCompetitor1();
            Competitor competitor2 = match.getCompetitor2();

            User user = userRepo.findOneByUsername(username).get();

            BettingPlayer bettingPlayer = bettingPlayerRepo
                    .findByPlayerAndBettingMatch(user, bettingMatch);
            List<Competitor> competitors = new ArrayList<Competitor>();
            competitors.add(competitor1);
            competitors.add(competitor2);

            historyBetting.setUsername(username);
            historyBetting.setCompetitors(competitors);
            historyBetting.setCurrentBetCompetitor(bettingPlayer
                    .getBetCompetitor());
            historyBetting.setCompetitorChanges(changelog.getEntityChanges());
            historyBetting.setCompetitor1Score(match.getScore1());
            historyBetting.setCompetitor2Score(match.getScore2());
            historyBetting.setExpiredTime(bettingMatch.getExpiredTime());
            historyBetting.setBetAmount(bettingMatch.getBetAmount());

            historyBettings.add(historyBetting);
        }
        return historyBettings;
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