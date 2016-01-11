package vn.kms.ngaythobet;

import java.time.LocalDateTime;
import java.util.List;

import vn.kms.ngaythobet.domain.betting.BettingMatch;
import vn.kms.ngaythobet.domain.betting.BettingPlayer;
import vn.kms.ngaythobet.domain.core.User;
import vn.kms.ngaythobet.domain.core.User.Role;
import vn.kms.ngaythobet.domain.tournament.Competitor;
import vn.kms.ngaythobet.domain.tournament.Group;
import vn.kms.ngaythobet.domain.tournament.Match;
import vn.kms.ngaythobet.domain.tournament.Round;
import vn.kms.ngaythobet.domain.tournament.Tournament;

public class CreateData {
    public static User createUser(boolean active, String email, String name, String password, String username) {
        User user = new User();
        user.setActivated(active);
        user.setEmail(email);
        user.setName(name);
        user.setPassword(password);
        user.setRole(Role.USER);
        user.setUsername(username);
        return user;
    }

    public static Group createGroup(User moderator, String name, Tournament tournament, List<User> members) {
        Group group = new Group();
        group.setModerator(moderator);
        group.setName(name);
        group.setTournament(tournament);
        group.setMembers(members);
        return group;
    }

    public static Match createMatch(Competitor competitor1, Competitor competitor2, String location,
            LocalDateTime matchTime, Round round) {
        Match match = new Match();
        match.setCompetitor1(competitor1);
        match.setCompetitor2(competitor2);
        match.setLocation(location);
        match.setMatchTime(matchTime);
        match.setRound(round);
        return match;
    }

    public static Match createMatch(Competitor competitor1, Competitor competitor2, String location,
            LocalDateTime matchTime, Round round, Long score1, Long score2) {
        Match match = new Match();
        match.setCompetitor1(competitor1);
        match.setCompetitor2(competitor2);
        match.setLocation(location);
        match.setMatchTime(matchTime);
        match.setRound(round);
        match.setScore1(score1);
        match.setScore2(score2);
        return match;
    }

    public static BettingMatch createBettingMatch(boolean active, Match match, Group group, LocalDateTime expiredTime) {
        BettingMatch bettingMatch1 = new BettingMatch();
        bettingMatch1.setActivated(active);
        bettingMatch1.setMatch(match);
        bettingMatch1.setGroup(group);
        bettingMatch1.setExpiredTime(expiredTime);
        return bettingMatch1;
    }

    public static BettingPlayer createBettingPlayer(BettingMatch bettingMatch, User user, Competitor competitor) {
        BettingPlayer bettingPlayer = new BettingPlayer();
        bettingPlayer.setBetCompetitor(competitor);
        bettingPlayer.setPlayer(user);
        bettingPlayer.setBettingMatch(bettingMatch);
        return bettingPlayer;
    }

    public static Tournament createTournament(boolean active, String name) {
        Tournament tournament = new Tournament();
        tournament.setActivated(active);
        tournament.setName(name);
        return tournament;
    }

    public static Round createRound(Tournament tournament, String name) {
        Round round = new Round();
        round.setTournament(tournament);
        round.setName(name);
        return round;
    }
}
