package vn.kms.ngaythobet.domain.tournament;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import vn.kms.ngaythobet.domain.core.AuditableEntity;
import vn.kms.ngaythobet.domain.core.User;

@Entity
@Table(name = "groups")
public class Group extends AuditableEntity {
    @Column
    private String name;

    @ManyToOne
    @JoinColumn(name = "moderator")
    private User moderator;

    @ManyToMany(targetEntity = vn.kms.ngaythobet.domain.core.User.class)
    @JoinTable(name = "group_user", joinColumns = { @JoinColumn(name = "group_id") }, inverseJoinColumns = {
            @JoinColumn(name = "user_id") })
    private List<User> members;

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getModerator() {
        return moderator;
    }

    public void setModerator(User moderator) {
        this.moderator = moderator;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

}
