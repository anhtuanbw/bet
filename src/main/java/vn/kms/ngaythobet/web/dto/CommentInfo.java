package vn.kms.ngaythobet.web.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import vn.kms.ngaythobet.domain.core.ChangeLog.Change;
import vn.kms.ngaythobet.domain.tournament.Competitor;

public class CommentInfo {
    private String username;
    private Competitor betCompetitor;
    private List<Map<String, Change>> competitorChanges;
    private String comment;
    private LocalDateTime timestamp;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Competitor getBetCompetitor() {
        return betCompetitor;
    }

    public void setBetCompetitor(Competitor betCompetitor) {
        this.betCompetitor = betCompetitor;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public List<Map<String, Change>> getCompetitorChanges() {
        return competitorChanges;
    }

    public void setCompetitorChanges(List<Map<String, Change>> competitorChanges) {
        this.competitorChanges = competitorChanges;
    }
}
