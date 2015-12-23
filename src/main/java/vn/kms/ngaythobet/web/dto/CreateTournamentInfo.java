package vn.kms.ngaythobet.web.dto;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

public class CreateTournamentInfo {
    @NotEmpty
    private String name;

    @NotEmpty
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
