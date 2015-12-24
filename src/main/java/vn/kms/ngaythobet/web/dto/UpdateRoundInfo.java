// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.web.dto;

import java.util.List;
import javax.validation.constraints.NotNull;
import vn.kms.ngaythobet.domain.tournament.Round;
import vn.kms.ngaythobet.domain.validation.EntityExist;
import vn.kms.ngaythobet.domain.validation.ListUnique;

public class UpdateRoundInfo {
    @EntityExist(type = Round.class, message = "{validation.matches.existRoundEntity.message}")
    @NotNull
    private Long roundId;
    
    @ListUnique(message = "{validation.competitors.unique.message}")
    private List<Long> competitorIds;

    public Long getRoundId() {
        return roundId;
    }

    public void setRoundId(Long roundId) {
        this.roundId = roundId;
    }

    public List<Long> getCompetitorIds() {
        return competitorIds;
    }

    public void setCompetitorIds(List<Long> competitorIds) {
        this.competitorIds = competitorIds;
    }
}
