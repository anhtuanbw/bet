// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.core;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.kms.ngaythobet.domain.betting.BettingMatch;
import vn.kms.ngaythobet.domain.betting.BettingMatchRepository;
import vn.kms.ngaythobet.domain.betting.BettingPlayer;
import vn.kms.ngaythobet.domain.betting.BettingPlayerRepository;
import vn.kms.ngaythobet.domain.core.ChangeLog.Change;
import vn.kms.ngaythobet.domain.tournament.Competitor;
import vn.kms.ngaythobet.domain.tournament.CompetitorRepository;
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
    private final CompetitorRepository competitorRepo;

    @Autowired
    public ChangeLogService(ChangeLogRepository changeLogRepo, BettingMatchRepository bettingMatchRepo,
            BettingPlayerRepository bettingPlayerRepo, UserRepository userRepo, CompetitorRepository competitorRepo) {
        this.changeLogRepo = changeLogRepo;
        this.bettingMatchRepo = bettingMatchRepo;
        this.bettingPlayerRepo = bettingPlayerRepo;
        this.userRepo = userRepo;
        this.competitorRepo = competitorRepo;
    }

    @Transactional(readOnly = true)
    public List<CommentInfo> getComments(Long bettingMatchId, Integer paging) {
        List<CommentInfo> comments = new ArrayList<CommentInfo>();
        Pageable pageable = new PageRequest(0, 10 * paging + 10, Direction.DESC, "timestamp");
        List<ChangeLog> changeLogs = changeLogRepo.findTop10ByEntityTypeAndEntityId(
                BettingMatch.class.getCanonicalName(), bettingMatchId, pageable);
        List<ChangeLog> changeLogsHistoryBetting = changeLogRepo.findTop10ByEntityTypeAndEntityId(
                BettingPlayer.class.getCanonicalName(), bettingMatchId, pageable);

        if (changeLogs != null) {
            Iterator<ChangeLog> iteratorChangeLogs = changeLogs.iterator();
            while (iteratorChangeLogs.hasNext()) {
                ChangeLog changelog = iteratorChangeLogs.next();

                CommentInfo commentInfo = new CommentInfo();
                commentInfo.setUsername(changelog.getUsername());
                commentInfo.setTimestamp(changelog.getTimestamp());

                BettingPlayer bettingPlayer = bettingPlayerRepo.findByUsernameAndBettingMatchId(
                        changelog.getUsername(), bettingMatchId);

                if (bettingPlayer != null) {
                    commentInfo.setBetCompetitor(bettingPlayer.getBetCompetitor());
                }
                Map<String, Change> entityChanges = changelog.getEntityChanges();
                if (entityChanges != null) {
                    Change change = entityChanges.get("comment");
                    if (change != null) {
                        commentInfo.setComment(change.getNewValue().toString());
                    }
                }
                comments.add(commentInfo);
            }
        }

        for (ChangeLog c : changeLogsHistoryBetting) {
            CommentInfo commentInfo = new CommentInfo();
            commentInfo.setUsername(c.getUsername());
            commentInfo.setTimestamp(c.getTimestamp());
            Map<String, Change> entityChanges = c.getEntityChanges();
            if (entityChanges != null) {
                Change change = entityChanges.get("betCompetitor");
                if (change != null) {
                    Long competitorId = Long.parseLong(change.getNewValue().toString());
                    commentInfo.setBetCompetitor(competitorRepo.findOne(competitorId));
                }
            }
            comments.add(commentInfo);
        }

        Collections.sort(comments, new Comparator<CommentInfo>() {

            @Override
            public int compare(CommentInfo commentInfo1, CommentInfo commentInfo2) {
                return commentInfo2.getTimestamp().toString().compareTo(commentInfo1.getTimestamp().toString());
            }
        });

        return comments;
    }

    @Transactional(readOnly = true)
    public List<HistoryBetting> getHistoryBetting(Long playerId) {
        List<HistoryBetting> historyBettings = new ArrayList<HistoryBetting>();
        List<ChangeLog> changelogs = changeLogRepo.findByEntityType(BettingPlayer.class.getCanonicalName());
        if (changelogs != null) {
            Iterator<ChangeLog> iteratorChangeLogs = changelogs.iterator();
            while (iteratorChangeLogs.hasNext()) {
                ChangeLog changelog = iteratorChangeLogs.next();

                BettingPlayer bettingPlayer = bettingPlayerRepo.findByPlayer(userRepo.getOne(playerId));

                if (bettingPlayer == null)
                    continue;

                HistoryBetting historyBetting = new HistoryBetting();

                String username = changelog.getUsername();

                Match match = bettingPlayer.getBettingMatch().getMatch();

                Competitor competitor1 = match.getCompetitor1();
                Competitor competitor2 = match.getCompetitor1();

                List<Competitor> competitors = new ArrayList<Competitor>();
                competitors.add(competitor1);
                competitors.add(competitor2);

                BettingMatch bettingMatch = bettingPlayer.getBettingMatch();

                historyBetting.setUsername(username);
                historyBetting.setCompetitors(competitors);
                historyBetting.setCurrentBetCompetitor(bettingPlayer.getBetCompetitor());
                historyBetting.setCompetitorChanges(changelog.getEntityChanges());
                historyBetting.setCompetitor1Score(match.getScore1());
                historyBetting.setCompetitor2Score(match.getScore2());
                historyBetting.setExpiredTime(bettingMatch.getExpiredTime());
                historyBetting.setBetAmount(bettingMatch.getBetAmount());

                historyBettings.add(historyBetting);
            }
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