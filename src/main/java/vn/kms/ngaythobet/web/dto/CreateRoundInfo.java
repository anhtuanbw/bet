// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.web.dto;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class CreateRoundInfo {
    @NotEmpty
    @Size(min=6, max=50)
    private String name;
    
    @NotNull
    private long tournamentId;

    @NotEmpty
    private List<Long> competitorId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(long tournamentId) {
        this.tournamentId = tournamentId;
    }

    public List<Long> getCompetitorId() {
        return competitorId;
    }

    public void setCompetitorId(List<Long> competitorId) {
        this.competitorId = competitorId;
    }
}
