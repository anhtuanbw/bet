// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.betting;

import java.time.LocalDateTime;

import vn.kms.ngaythobet.domain.tournament.GroupRepository;
import vn.kms.ngaythobet.domain.tournament.MatchRepository;
import vn.kms.ngaythobet.web.dto.CreateBettingMatchInfo;

public class BettingService {

    private final MatchRepository matchRepo;
    private final GroupRepository groupRepo;
    private final BettingMatchRepository bettingRepo;

    public BettingService(MatchRepository matchRepo, GroupRepository groupRepo, BettingMatchRepository bettingRepo) {
        this.matchRepo = matchRepo;
        this.groupRepo = groupRepo;
        this.bettingRepo = bettingRepo;
    }

    public void createBettingMatch(CreateBettingMatchInfo createBettingMatchInfo) {

        BettingMatch bettingMatch = new BettingMatch();
        bettingMatch.setBalance1(createBettingMatchInfo.getBalance1());
        bettingMatch.setBalance2(createBettingMatchInfo.getBalance2());
        bettingMatch.setExpiredTime(createBettingMatchInfo.getExpiredTime());
        bettingMatch.setBetAmount(createBettingMatchInfo.getBetAmount());
        bettingMatch.setMatch(matchRepo.getOne(createBettingMatchInfo.getMatchId()));
        bettingMatch.setGroup(groupRepo.getOne(createBettingMatchInfo.getGroupId()));
        bettingMatch.setComment(createBettingMatchInfo.getComment());
        bettingMatch.setActivated(createBettingMatchInfo.isActivated());
        bettingRepo.save(bettingMatch);
    }

    public void updateMatchBetBalance(int matchBetId, double competitor1Balance, double competitor2Balance) {

    }

    public void updateMatchBetAmount(int matchBetId, long amount) {

    }

    public void updateMatchBetExpiryTime(int matchBetId, LocalDateTime expiryTime) {

    }

    public void placeBet(int matchBetId, int betCompetitorId, String comment) {

    }

    public long getBetAmountResult(int playerId, int matchBetId) {
        return 0;
    }
}
