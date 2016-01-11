// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.web.dto;

import static vn.kms.ngaythobet.domain.util.Constants.WHITE_SPACE_REGEX;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import vn.kms.ngaythobet.domain.tournament.Competitor;
import vn.kms.ngaythobet.domain.tournament.Tournament;
import vn.kms.ngaythobet.domain.validation.EntityExist;
import vn.kms.ngaythobet.domain.validation.ListEntityExist;
import vn.kms.ngaythobet.domain.validation.ListEntityJoinedValid;
import vn.kms.ngaythobet.domain.validation.ListUnique;

@ListEntityJoinedValid(entityId = "tournamentId", fieldName = "competitors", entities = "competitorIds", type = Tournament.class, message = "{validation.competitor.is.not.joined}")
public class CreateRoundInfo {
    @Size(min = 6, max = 50, message = "{validation.name.notEmpty.size.blankspace}")
    @Pattern(regexp = WHITE_SPACE_REGEX, message = "{validation.name.notEmpty.size.blankspace}")
    private String name;

    @EntityExist(type = Tournament.class, message = "{validation.existTournament.message}")
    @NotNull
    private Long tournamentId;

    @ListUnique(message = "{validation.competitors.unique.message}")
    @ListEntityExist(type = Competitor.class, message = "{validation.competitor.is.not.joined}")
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
