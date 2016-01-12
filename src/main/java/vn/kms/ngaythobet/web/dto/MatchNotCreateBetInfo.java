package vn.kms.ngaythobet.web.dto;

import vn.kms.ngaythobet.domain.tournament.Match;

public class MatchNotCreateBetInfo {
    private Long roundId;
    private String roundName;
    private Match match;

    public Long getRoundId() {
        return roundId;
    }

    public void setRoundId(Long roundId) {
        this.roundId = roundId;
    }

    public String getRoundName() {
        return roundName;
    }

    public void setRoundName(String roundName) {
        this.roundName = roundName;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

}
