package vn.kms.ngaythobet.web.dto;

import java.util.ArrayList;
import java.util.List;

import vn.kms.ngaythobet.domain.tournament.Match;

public class MatchNotCreateBetInfo {
    private Long roundId;
    private String roundName;
    private List<Match> matches = new ArrayList<>();

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

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }
}
