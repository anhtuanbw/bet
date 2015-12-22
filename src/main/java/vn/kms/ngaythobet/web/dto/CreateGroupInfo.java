package vn.kms.ngaythobet.web.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class CreateGroupInfo {
    @Size(min = 6, max = 50)
    @NotEmpty
    private String name;

    @NotNull
    private Long tournamentId;

    @NotEmpty
    private String moderator;

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

    public String getModerator() {
        return moderator;
    }

    public void setModerator(String username) {
        this.moderator = username;
    }

}
