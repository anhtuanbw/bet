package vn.kms.ngaythobet.web.dto;

import java.time.LocalDateTime;

import vn.kms.ngaythobet.domain.tournament.Competitor;

public class CommentInfo {
    private String username;
    private Competitor betCompetitor;
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
}
