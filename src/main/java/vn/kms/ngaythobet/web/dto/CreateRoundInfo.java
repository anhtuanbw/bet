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
import vn.kms.ngaythobet.domain.validation.ListUnique;

public class CreateRoundInfo {
    @NotEmpty
    @Size(min=6, max=50)
    @FieldUnique(entity = Round.class, field = "name")
    private String name;
    
    @EntityExist(type = Tournament.class, message = "{validation.existTournament.message}")
    @NotNull
    private Long tournamentId;

    @ListUnique(message = "{validation.competitors.unique.message}")
    private List<Long> competitorIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(Long tournamentId) {
        this.tournamentId = tournamentId;
    }

    public List<Long> getCompetitorIds() {
        return competitorIds;
    }

    public void setCompetitorIds(List<Long> competitorIds) {
        this.competitorIds = competitorIds;
    }
}
