// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.core;

import java.util.ArrayList;
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
import vn.kms.ngaythobet.domain.tournament.Match;
import vn.kms.ngaythobet.web.dto.CommentInfo;
import vn.kms.ngaythobet.web.dto.HistoryBetting;

@Service
@Transactional
public class ChangeLogService {

    private final ChangeLogRepository changeLogRepo;
    private final BettingPlayerRepository bettingPlayerRepo;

    @Autowired
    public ChangeLogService(ChangeLogRepository changeLogRepo,
            BettingPlayerRepository bettingPlayerRepo,
            BettingMatchRepository brepo) {
        this.changeLogRepo = changeLogRepo;
        this.bettingPlayerRepo = bettingPlayerRepo;
    }

    @Transactional(readOnly = true)
    public List<CommentInfo> getComments(Long bettingMatchId, Integer paging) {
        List<CommentInfo> comments = new ArrayList<CommentInfo>();
        Pageable pageable = new PageRequest(paging, 10, Direction.DESC,
                "timestamp");
        List<ChangeLog> changeLogs = changeLogRepo
                .findTop10ByEntityTypeAndEntityId(
                        BettingMatch.class.getCanonicalName(), bettingMatchId,
                        pageable);

        if (changeLogs != null) {
            Iterator<ChangeLog> iteratorChangeLogs = changeLogs.iterator();
            while (iteratorChangeLogs.hasNext()) {
                ChangeLog changelog = iteratorChangeLogs.next();

                CommentInfo commentInfo = new CommentInfo();
                commentInfo.setUsername(changelog.getUsername());
                commentInfo.setTimestamp(changelog.getTimestamp());

                BettingPlayer bettingPlayer = bettingPlayerRepo
                        .findByUsernameAndBettingMatchId(
                                changelog.getUsername(), bettingMatchId);

                if (bettingPlayer != null) {
                    commentInfo.setBetCompetitor(bettingPlayer
                            .getBetCompetitor());
                }
                Map<String, Change> entityChanges = changelog
                        .getEntityChanges();
                if (entityChanges != null) {
                    Change change = entityChanges.get("comment");
                    if (change != null) {
                        commentInfo.setComment(change.getNewValue().toString());
                    }
                }

                List<ChangeLog> changelogs = changeLogRepo
                        .findByEntityTypeAndUsername(
                                BettingPlayer.class.getCanonicalName(),
                                changelog.getUsername());

                List<Map<String, Change>> competitorChanges = new ArrayList<Map<String, Change>>();

                for (ChangeLog c : changelogs) {
                    if (c.getEntityChanges() != null) {
                        competitorChanges.add(c.getEntityChanges());
                    }
                }
                commentInfo.setCompetitorChanges(competitorChanges);
                comments.add(commentInfo);
            }
        }
        return comments;
    }

    @Transactional(readOnly = true)
    public List<HistoryBetting> getHistoryBetting(Long playerId) {
        List<HistoryBetting> historyBettings = new ArrayList<HistoryBetting>();
        List<ChangeLog> changelogs = changeLogRepo
                .findByEntityType(BettingPlayer.class.getCanonicalName());
        if (changelogs != null) {
            Iterator<ChangeLog> iteratorChangeLogs = changelogs.iterator();
            while (iteratorChangeLogs.hasNext()) {
                ChangeLog changelog = iteratorChangeLogs.next();

                BettingPlayer bettingPlayer = bettingPlayerRepo
                        .findByPlayerId(playerId);

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
                historyBetting.setCurrentBetCompetitor(bettingPlayer
                        .getBetCompetitor());
                historyBetting.setCompetitorChanges(changelog
                        .getEntityChanges());
                historyBetting.setCompetitor1Score(match.getScore1());
                historyBetting.setCompetitor2Score(match.getScore2());
                historyBetting.setExpiredTime(bettingMatch.getExpiredTime());
                historyBetting.setBetAmount(bettingMatch.getBetAmount());

                historyBettings.add(historyBetting);
            }
        }
        return historyBettings;
    }
}