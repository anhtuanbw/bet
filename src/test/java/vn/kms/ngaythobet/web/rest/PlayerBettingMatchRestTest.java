package vn.kms.ngaythobet.web.rest;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import vn.kms.ngaythobet.BaseTest;
import vn.kms.ngaythobet.domain.betting.BettingMatch;
import vn.kms.ngaythobet.domain.betting.BettingMatchRepository;
import vn.kms.ngaythobet.domain.betting.BettingMatchService;
import vn.kms.ngaythobet.domain.betting.BettingPlayer;
import vn.kms.ngaythobet.domain.betting.BettingPlayerRepository;
import vn.kms.ngaythobet.domain.betting.BettingPlayerService;
import vn.kms.ngaythobet.domain.core.User;
import vn.kms.ngaythobet.domain.core.User.Role;
import vn.kms.ngaythobet.domain.tournament.Competitor;
import vn.kms.ngaythobet.domain.tournament.CompetitorRepository;
import vn.kms.ngaythobet.domain.tournament.Group;
import vn.kms.ngaythobet.domain.tournament.GroupRepository;
import vn.kms.ngaythobet.domain.tournament.Match;
import vn.kms.ngaythobet.domain.tournament.MatchRepository;
import vn.kms.ngaythobet.domain.tournament.Round;
import vn.kms.ngaythobet.domain.tournament.RoundRepository;
import vn.kms.ngaythobet.domain.tournament.Tournament;
import vn.kms.ngaythobet.domain.tournament.TournamentRepository;
import vn.kms.ngaythobet.domain.util.Constants;
import vn.kms.ngaythobet.infras.security.CustomUserDetails;
import vn.kms.ngaythobet.infras.security.xauth.Contants;
import vn.kms.ngaythobet.infras.security.xauth.TokenProvider;
import vn.kms.ngaythobet.web.dto.AddCommentInfo;

public class PlayerBettingMatchRestTest extends BaseTest {
    @Autowired
    private BettingPlayerService bettingPlayerService;

    private TokenProvider tokenProvider;

    @Autowired
    private MatchRepository matchRepo;
    @Autowired
    private BettingMatchRepository bettingMatchRepo;

    @Autowired
    private BettingPlayerRepository bettingPlayerRepo;

    @Autowired
    private TournamentRepository tournamentRepo;

    @Autowired
    private CompetitorRepository competitorRepo;

    @Autowired
    private RoundRepository roundRepo;

    @Autowired
    private GroupRepository groupRepo;

    @Autowired
    private BettingMatchService bettingMatchService;

    private TestMessageChannel clientOutboundChannel;

    private TestAnnotationMethodHandler annotationMethodHandler;

    private Tournament tournamentTemp;
    private Competitor competitorTemp1;
    private Competitor competitorTemp2;
    private Competitor competitorTemp3;
    private Competitor competitorTemp4;
    private Round roundTemp;
    private Match matchTemp;
    private Match matchTemp2;
    private Match matchTemp3;
    private User userTemp1;
    private User userTemp2;
    private Group groupTemp;
    private BettingMatch bettingMatchTemp;
    private BettingMatch notActiveBettingMatch;
    private BettingMatch expiredBettingMatch;
    private BettingPlayer bettingPlayerTemp;

    @Override
    public void doStartUp() {
        bettingPlayerService = new BettingPlayerService(bettingMatchRepo, bettingPlayerRepo, competitorRepo, userRepo);
        tokenProvider = new TokenProvider(Contants.SECRET_KEY, Contants.TOKEN_VALIDITY);
        PlayerBettingMatchRest controller = new PlayerBettingMatchRest(bettingPlayerService, tokenProvider,
                bettingMatchService);
        clientOutboundChannel = new TestMessageChannel();
        this.annotationMethodHandler = new TestAnnotationMethodHandler(new TestMessageChannel(), clientOutboundChannel,
                new SimpMessagingTemplate(new TestMessageChannel()));
        this.annotationMethodHandler.registerHandler(controller);
        this.annotationMethodHandler.setDestinationPrefixes(Arrays.asList("/app"));
        this.annotationMethodHandler.setMessageConverter(new MappingJackson2MessageConverter());
        this.annotationMethodHandler.setApplicationContext(new StaticApplicationContext());
        this.annotationMethodHandler.afterPropertiesSet();
        User user1 = createUser(true, "email@yahoo.com", "user1", "123467", "user1");
        User user2 = createUser(true, "email2@yahoo.com", "user2", "123467", "user2");
        userTemp1 = userRepo.save(user1);
        userTemp2 = userRepo.save(user2);
        // add tournament
        Tournament tournament = new Tournament();
        tournament.setActivated(false);
        tournament.setName("Euro");
        tournamentTemp = tournamentRepo.save(tournament);
        // add round
        Round round = new Round();
        round.setTournament(tournamentTemp);
        round.setName("round 1");
        roundTemp = roundRepo.save(round);
        // add 4 competitors
        Competitor competitor1 = new Competitor(tournamentTemp, "England");
        Competitor competitor2 = new Competitor(tournamentTemp, "France");
        Competitor competitor3 = new Competitor(tournamentTemp, "Germany");
        Competitor competitor4 = new Competitor(tournamentTemp, "Portulgal");
        List<Round> rounds = new ArrayList<>();
        rounds.add(roundTemp);
        competitor1.setRounds(rounds);
        competitor2.setRounds(rounds);
        competitor3.setRounds(rounds);
        competitor4.setRounds(rounds);
        competitorTemp1 = competitorRepo.save(competitor1);
        competitorTemp2 = competitorRepo.save(competitor2);
        competitorTemp3 = competitorRepo.save(competitor3);
        competitorTemp4 = competitorRepo.save(competitor4);
        // add 3 matches
        Match match = createMatch(competitorTemp1, competitorTemp2, "location test", LocalDateTime.now(), roundTemp);
        matchTemp = matchRepo.save(match);
        Match match2 = createMatch(competitorTemp3, competitorTemp4, "location test 2", LocalDateTime.now(), roundTemp);
        matchTemp2 = matchRepo.save(match2);
        Match match3 = createMatch(competitorTemp1, competitorTemp3, "location test 3", LocalDateTime.now(), roundTemp);
        matchTemp3 = matchRepo.save(match3);
        // add group
        Group group = new Group();
        List<User> members = new ArrayList<>();
        members.add(userTemp1);
        members.add(userTemp2);
        group.setMembers(members);
        group.setModerator(userTemp1);
        group.setName("testGroup");
        group.setTournament(tournamentTemp);
        groupTemp = groupRepo.save(group);
        // add valid betting match
        BettingMatch bettingMatch = createBettingMatch(true, matchTemp, groupTemp, LocalDateTime.now().plusDays(30));
        bettingMatchTemp = bettingMatchRepo.save(bettingMatch);
        // add betting Match is expired
        BettingMatch bettingMatch2 = createBettingMatch(true, matchTemp3, groupTemp, LocalDateTime.now().minusDays(30));
        expiredBettingMatch = bettingMatchRepo.save(bettingMatch2);
        // add betting Match is not active
        BettingMatch bettingMatch3 = createBettingMatch(false, matchTemp2, groupTemp, LocalDateTime.now().plusDays(30));
        notActiveBettingMatch = bettingMatchRepo.save(bettingMatch3);
        // add player betting match
        BettingPlayer bettingPlayer = new BettingPlayer();
        bettingPlayer.setBetCompetitor(competitorTemp2);
        bettingPlayer.setPlayer(userTemp2);
        bettingPlayer.setBettingMatch(bettingMatchTemp);
        bettingMatchTemp.setComment("initalize comment");
        bettingPlayerTemp = bettingPlayerRepo.save(bettingPlayer);
    }

    @Test
    public void testAddComment() throws JsonProcessingException {
        User user = getDefaultUser();
        AddCommentInfo commentInfo = new AddCommentInfo();
        commentInfo.setComment("test comment");
        commentInfo.setBettingMatchId(bettingMatchTemp.getId());
        byte[] payload = new ObjectMapper().writeValueAsBytes(commentInfo);
        String token = generateToken(user);
        StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.SEND);
        headers.setDestination("/app/betting-match/comment/1");
        headers.setSessionId("0");
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add(Constants.XAUTH_TOKEN_HEADER_NAME, token);
        headers.addNativeHeaders(params);
        headers.setSessionAttributes(new HashMap<String, Object>());
        Message<byte[]> message = MessageBuilder.withPayload(payload).setHeaders(headers).build();
        this.annotationMethodHandler.handleMessage(message);
        BettingMatch bettingMatch = bettingMatchRepo.findOne(bettingMatchTemp.getId());
        assertThat(bettingMatch.getComment(), equalTo("test comment"));
    }

    private String generateToken(User user) {

        List<GrantedAuthority> authorities = singletonList(new SimpleGrantedAuthority(user.getRole().getAuthority()));
        CustomUserDetails customUserDetails = new CustomUserDetails(user, authorities);
        SecurityContextHolder.getContext()
                .setAuthentication(new TestingAuthenticationToken(customUserDetails, user.getUsername()));
        String token = tokenProvider.createToken(customUserDetails).getToken();
        return token;
    }

    private User createUser(boolean active, String email, String name, String password, String username) {
        User user = new User();
        user.setActivated(active);
        user.setEmail(email);
        user.setName(name);
        user.setPassword(password);
        user.setRole(Role.USER);
        user.setUsername(username);
        return user;
    }

    private BettingMatch createBettingMatch(boolean active, Match match, Group group, LocalDateTime expiredTime) {
        BettingMatch bettingMatch1 = new BettingMatch();
        bettingMatch1.setActivated(active);
        bettingMatch1.setMatch(match);
        bettingMatch1.setGroup(group);
        bettingMatch1.setExpiredTime(expiredTime);
        return bettingMatch1;
    }

    private Match createMatch(Competitor competitor1, Competitor competitor2, String location, LocalDateTime matchTime,
            Round round) {
        Match match = new Match();
        match.setCompetitor1(competitor1);
        match.setCompetitor2(competitor2);
        match.setLocation(location);
        match.setMatchTime(matchTime);
        match.setRound(round);
        return match;
    }
    
    @After
    public void clearData(){
        bettingPlayerRepo.deleteAll();
        bettingMatchRepo.deleteAll();
        matchRepo.deleteAll();
        roundRepo.deleteAll();
        competitorRepo.deleteAll();
        groupRepo.deleteAll();
        tournamentRepo.deleteAll();
        userRepo.deleteAll();
    }
}
