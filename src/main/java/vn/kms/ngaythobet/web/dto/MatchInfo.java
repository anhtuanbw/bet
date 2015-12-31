package vn.kms.ngaythobet.web.dto;

import java.util.List;

import vn.kms.ngaythobet.domain.tournament.Competitor;

public class MatchInfo {
    private List<Competitor> competitors;

    public List<Competitor> getCompetitors() {
        return competitors;
    }

    public void setCompetitors(List<Competitor> competitors) {
        this.competitors = competitors;
    }
}
