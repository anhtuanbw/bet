package vn.kms.ngaythobet.web.dto;

import java.util.List;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import vn.kms.ngaythobet.domain.tournament.Tournament;
import vn.kms.ngaythobet.domain.validation.FieldUnique;

public class CreateTournamentInfo {
    @NotEmpty
    @Size(min = 6, max = 50)
    @FieldUnique(entity = Tournament.class, field = "name", message = "{validation.tournament.name.unique}")
    private String name;

    @NotEmpty
    @Size(min = 2, message = "{validation.min.message}")
    private List<String> competitors;

    private boolean isActive;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCompetitors() {
        return competitors;
    }

    public void setCompetitors(List<String> competitors) {
        this.competitors = competitors;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

}
