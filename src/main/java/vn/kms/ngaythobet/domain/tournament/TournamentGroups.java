// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.tournament;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

public class TournamentGroups {
    private final long tournamentId;
    private final String tournamentName;
    private final List<BettingGroupInfo> groups = new ArrayList<>();

    public TournamentGroups(long tournamentId, String tournamentName) {
        this.tournamentId = tournamentId;
        this.tournamentName = tournamentName;
    }

    public long getTournamentId() {
        return tournamentId;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public List<BettingGroupInfo> getGroups() {
        return groups;
    }

    public void addGroup(BettingGroupInfo group) {
        Assert.notNull(group);

        groups.add(group);
    }

    public static class BettingGroupInfo {
        private final long id;
        private final String name;
        private final boolean isModerator;
        private final boolean isPlayer;

        public BettingGroupInfo(long id, String name, boolean isModerator, boolean isPlayer) {
            this.id = id;
            this.name = name;
            this.isModerator = isModerator;
            this.isPlayer = isPlayer;
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public boolean isModerator() {
            return isModerator;
        }

        public boolean isPlayer() {
            return isPlayer;
        }
    }
}
