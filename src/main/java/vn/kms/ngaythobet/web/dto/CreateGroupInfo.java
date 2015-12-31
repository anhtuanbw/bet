package vn.kms.ngaythobet.web.dto;

import static vn.kms.ngaythobet.domain.util.Constants.WHITE_SPACE_REGEX;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import vn.kms.ngaythobet.domain.core.User;
import vn.kms.ngaythobet.domain.tournament.Group;
import vn.kms.ngaythobet.domain.tournament.Tournament;
import vn.kms.ngaythobet.domain.validation.EntityActivated;
import vn.kms.ngaythobet.domain.validation.EntityExist;
import vn.kms.ngaythobet.domain.validation.FieldUnique;

public class CreateGroupInfo {
    @Size(min = 6, max = 50)
    @NotEmpty
    @FieldUnique(field = "name", entity = Group.class, message = "{validation.group.existName.message}")
    @Pattern(regexp = WHITE_SPACE_REGEX, message = "{validation.pattern.blankspace}")
    private String name;

    @NotNull
    @EntityExist(type = Tournament.class)
    @EntityActivated(type = Tournament.class, message = "{validation.tournament.inactivated}")
    private Long tournamentId;

    @NotNull
    @EntityExist(type = User.class)
    private Long moderator;

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

    public Long getModerator() {
        return moderator;
    }

    public void setModerator(Long moderator) {
        this.moderator = moderator;
    }

}
