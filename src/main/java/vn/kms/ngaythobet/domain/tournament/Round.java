// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.tournament;

import vn.kms.ngaythobet.domain.core.AuditableEntity;

public class Round extends AuditableEntity {
    private Tournament tournament;
    private String name;
    private int index;

    public Round() {
    }

    public Round(Tournament tournament, String name, int index) {
        this.tournament = tournament;
        this.name = name;
        this.index = index;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
