// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.betting;

import java.util.Set;

public class BettingGroupService {
    public void createGroup(long tournamentId, String name, String moderator) {
        // TODO: Send email to moderator to notify that he is a moderator of a group

        // TODO: Group is not activated by default
    }

    public void updateGroup(long groupId, Set<String> players, String rules) {
        // TODO: players is a set of username OR email

        // TODO: it is allow to update players list after group is activated

        // TODO: invitation email is only sent if group is activated

        // TODO: if the rules changed, the notification email should be sent to all players


    }

    public void activateGroup(long groupId) {
        // TODO: A group should be activated only once

        // TODO: Only send ONE invitation email to player in a group
    }
}
