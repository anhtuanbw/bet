package vn.kms.ngaythobet.web.dto;

import java.util.List;

import javax.validation.constraints.Size;

import vn.kms.ngaythobet.domain.tournament.Tournament;
import vn.kms.ngaythobet.domain.validation.FieldUnique;
import vn.kms.ngaythobet.domain.validation.ListUnique;

public class CreateTournamentInfo {
    @Size(min = 6, max = 50, message = "{validation.notEmpty.size}")
    @FieldUnique(entity = Tournament.class, field = "name", message = "{validation.tournament.name.unique.message}")
    private String name;

    @Size(min = 2, message = "{validation.competitors.min.message}")
    @ListUnique(message = "{validation.competitors.unique.message}")
    private List<String> competitors;

    private boolean active;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    public List<String> getCompetitors() {
        return competitors;
    }

    public void setCompetitors(List<String> competitors) {
        this.competitors = competitors;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
