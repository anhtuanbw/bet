// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.web.dto;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import vn.kms.ngaythobet.domain.tournament.Round;
import vn.kms.ngaythobet.domain.tournament.Tournament;
import vn.kms.ngaythobet.domain.validation.EntityExist;
import vn.kms.ngaythobet.domain.validation.FieldUnique;

public class CreateRoundInfo {
    @NotEmpty
    @Size(min=6, max=50)
    @FieldUnique(entity = Round.class, field = "name")
    private String name;
    
    @NotNull
    @EntityExist(type = Tournament.class, message = "{validation.existTournament.message}")
    private long tournamentId;

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
