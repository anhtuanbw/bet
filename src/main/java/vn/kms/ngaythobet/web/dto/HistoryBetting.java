package vn.kms.ngaythobet.web.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import vn.kms.ngaythobet.domain.core.ChangeLog.Change;
import vn.kms.ngaythobet.domain.tournament.Competitor;

public class HistoryBetting {
    private String username;
    private MatchInfo matchInfo;
    private Competitor currentBetCompetitor;
    private Map<String, Change> competitorChanges;
    private Long competitor1Score;
    private Long competitor2Score;
    private LocalDateTime expiredTime;
    private BigDecimal betAmount;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public MatchInfo getMatchInfo() {
        return matchInfo;
    }

    public void setMatchInfo(MatchInfo matchInfo) {
        this.matchInfo = matchInfo;
    }

    public Competitor getCurrentBetCompetitor() {
        return currentBetCompetitor;
    }

    public void setCurrentBetCompetitor(Competitor currentBetCompetitor) {
        this.currentBetCompetitor = currentBetCompetitor;
    }

    public Map<String, Change> getCompetitorChanges() {
        return competitorChanges;
    }

    public void setCompetitorChanges(Map<String, Change> competitorChanges) {
        this.competitorChanges = competitorChanges;
    }

    public Long getCompetitor1Score() {
        return competitor1Score;
    }

    public void setCompetitor1Score(Long competitor1Score) {
        this.competitor1Score = competitor1Score;
    }

    public Long getCompetitor2Score() {
        return competitor2Score;
    }

    public void setCompetitor2Score(Long competitor2Score) {
        this.competitor2Score = competitor2Score;
    }

    public LocalDateTime getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(LocalDateTime expiredTime) {
        this.expiredTime = expiredTime;
    }

    public BigDecimal getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(BigDecimal betAmount) {
        this.betAmount = betAmount;
    }
}
