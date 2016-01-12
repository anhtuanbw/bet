package vn.kms.ngaythobet.web.dto;

import static vn.kms.ngaythobet.domain.util.Constants.WHITE_SPACE_REGEX;

import java.util.List;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import vn.kms.ngaythobet.domain.tournament.Tournament;
import vn.kms.ngaythobet.domain.validation.FieldUnique;
import vn.kms.ngaythobet.domain.validation.ListUnique;

public class CreateTournamentInfo {
    @Size(min = 6, max = 50, message = "{validation.name.notEmpty.size.blankspace}")
    @FieldUnique(entity = Tournament.class, field = "name", message = "{validation.tournament.name.unique.message}")
    @Pattern(regexp = WHITE_SPACE_REGEX, message = "{validation.name.notEmpty.size.blankspace}")
    private String name;

    @Size(min = 2, message = "{validation.competitors.min.message}")
    @ListUnique(message = "{validation.competitors.unique.message}")
    private List<String> competitors;

    private boolean active;

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
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
