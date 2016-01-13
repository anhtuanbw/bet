// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import vn.kms.ngaythobet.web.dto.CommentInfo;

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
        int pager = 10 * paging + 10;
        int count = 0;
        Pageable pageable = new PageRequest(0, pager, Direction.DESC, "timestamp");

        List<ChangeLog> changeLogs = changeLogRepo.findTop10ByEntityTypeAndEntityId(
                BettingMatch.class.getCanonicalName(), bettingMatchId, pageable);
        List<ChangeLog> changeLogsHistoryBetting = changeLogRepo.findByEntityType(BettingPlayer.class
                .getCanonicalName());

        if (changeLogs != null) {
            for (ChangeLog changelog : changeLogs) {
                CommentInfo commentInfo = new CommentInfo();
                commentInfo.setUsername(changelog.getUsername());
                commentInfo.setTimestamp(changelog.getTimestamp());

                List<ChangeLog> bettingPlayerChangeLogsOfuser = changeLogRepo.findByEntityTypeAndUsername(
                        BettingPlayer.class.getCanonicalName(), changelog.getUsername());

                Collections.sort(bettingPlayerChangeLogsOfuser, new Comparator<ChangeLog>() {

                    @Override
                    public int compare(ChangeLog commentInfo1, ChangeLog commentInfo2) {
                        return commentInfo1.getTimestamp().compareTo(commentInfo2.getTimestamp());
                    }
                });

                Map<String, Change> entityChanges = changelog.getEntityChanges();
                if (entityChanges != null) {
                    Change change = entityChanges.get("comment");
                    if (change != null) {
                        commentInfo.setComment(change.getNewValue().toString());
                        comments.add(commentInfo);
                    }
                }
            }
        }
        if (changeLogsHistoryBetting != null) {
            Collections.sort(changeLogsHistoryBetting, new Comparator<ChangeLog>() {
                @Override
                public int compare(ChangeLog commentInfo1, ChangeLog commentInfo2) {
                    return commentInfo2.getTimestamp().compareTo(commentInfo1.getTimestamp());
                }
            });
            String oldValue = null;
            for (ChangeLog c : changeLogsHistoryBetting) {
                BettingPlayer bp = bettingPlayerRepo.findOne(c.getEntityId());
                CommentInfo commentInfo = new CommentInfo();
                if (bp != null) {
                    if (bp.getBettingMatch().getId().equals(bettingMatchId)) {
                        if (count == pager) {
                            break;
                        }
                        commentInfo.setUsername(c.getUsername());
                        commentInfo.setTimestamp(c.getTimestamp());
                        Map<String, Change> entityChanges = c.getEntityChanges();
                        if (entityChanges != null) {
                            Change change = entityChanges.get("betCompetitor");
                            if (change != null) {
                                Long competitorId = Long.parseLong(change.getNewValue().toString());
                                commentInfo.setBetCompetitor(competitorRepo.findOne(competitorId));
                                oldValue = change.getOldValue().toString();
                            }
                        } else {
                            if (oldValue != null) {
                                Long id = Long.parseLong(oldValue);
                                commentInfo.setBetCompetitor(competitorRepo.findOne(id));
                            } else {
                                commentInfo.setBetCompetitor(bp.getBetCompetitor());
                            }
                        }
                        comments.add(commentInfo);
                        count++;
                    }
                }
            }
        }

        Collections.sort(comments, new Comparator<CommentInfo>() {
            @Override
            public int compare(CommentInfo commentInfo1, CommentInfo commentInfo2) {
                return commentInfo1.getTimestamp().compareTo(commentInfo2.getTimestamp());
            }
        });

        List<User> users = bettingMatchRepo.findOne(bettingMatchId).getGroup().getMembers();

        for (User user : users) {
            Competitor competitor = null;
            for (CommentInfo commentInfo : comments) {
                if (commentInfo.getUsername().equals(user.getUsername())) {
                    if (commentInfo.getBetCompetitor() != null) {
                        competitor = commentInfo.getBetCompetitor();
                    } else {
                        commentInfo.setBetCompetitor(competitor);
                    }
                }
            }
        }

        Collections.sort(comments, new Comparator<CommentInfo>() {
            @Override
            public int compare(CommentInfo commentInfo1, CommentInfo commentInfo2) {
                return commentInfo2.getTimestamp().compareTo(commentInfo1.getTimestamp());
            }
        });
        if (comments.size() > pager) {
            comments = comments.subList(0, pager);
        }
        return comments;
    }

    @Transactional
    public Integer getCommentCount(Long bettingMatchId) {

        List<ChangeLog> changeLogs = changeLogRepo.findByEntityTypeAndEntityId(BettingMatch.class.getCanonicalName(),
                bettingMatchId);
        List<ChangeLog> changeLogsHistoryBetting = changeLogRepo.findByEntityType(BettingPlayer.class
                .getCanonicalName());

        int size1 = 0;
        int size2 = 0;

        if (changeLogs != null) {
            size1 += changeLogs.size();
        }

        if (changeLogsHistoryBetting != null) {
            for (ChangeLog c : changeLogsHistoryBetting) {
                BettingPlayer bp = bettingPlayerRepo.findOne(c.getEntityId());
                if (bp != null) {
                    if (bp.getBettingMatch().getId().equals(bettingMatchId)) {
                        size2++;
                    }
                }
            }
        }

        return size1 + size2;
    }

}