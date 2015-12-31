package vn.kms.ngaythobet.web.rest;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.kms.ngaythobet.domain.betting.BettingMatch;
import vn.kms.ngaythobet.domain.betting.BettingMatchService;
import vn.kms.ngaythobet.domain.betting.BettingPlayerService;
import vn.kms.ngaythobet.domain.core.User;
import vn.kms.ngaythobet.domain.core.UserService;
import vn.kms.ngaythobet.infras.security.xauth.TokenProvider;
import vn.kms.ngaythobet.web.dto.AddCommentInfo;
import vn.kms.ngaythobet.web.dto.PlayerBettingMatchInfo;

@RestController
public class PlayerBettingMatchRest {

    private final BettingMatchService bettingMatchService;
    private final BettingPlayerService bettingPlayerService;
    private final UserDetailsService userDetailsService;
    private final TokenProvider tokenProvider;
    private final UserService userService;
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;
    @Autowired
    public PlayerBettingMatchRest(BettingMatchService bettingMatchService,
            BettingPlayerService bettingPlayerService,
            UserDetailsService userDetailsService,
            TokenProvider tokenProvider,
            UserService userService) {
        this.bettingMatchService = bettingMatchService;
        this.bettingPlayerService = bettingPlayerService;
        this.userDetailsService = userDetailsService;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @RequestMapping(value = "/api/betting-match/{bettingId}", method = RequestMethod.GET)
    public BettingMatch getBettingMatch(@PathVariable Long bettingId) {
        BettingMatch bettingMatch = bettingMatchService
                .getBettingMatchById(bettingId);
        return bettingMatch;
    }

    @RequestMapping(value = "/api/betting-match/comment/{bettingId}", method = RequestMethod.POST)
    public PlayerBettingMatchInfo getCommentsByBettingMatch(@Valid @RequestBody PlayerBettingMatchInfo info) {
        return info;
    }

    @MessageMapping(value = "/betting-match/playbet/{bettingId}")
    @SendTo("/topic/playbet/{bettingId}")
    public PlayerBettingMatchInfo playBet(@Valid PlayerBettingMatchInfo bettingPlayerInfo,
            SimpMessageHeaderAccessor headerAccessor) {
        MultiValueMap<String, String> nativeHeaders = headerAccessor
                .getMessageHeaders()
                .get(StompHeaderAccessor.NATIVE_HEADERS, MultiValueMap.class);
        String token = null;
        if (nativeHeaders.get("x-auth-token") != null
                && !nativeHeaders.get("x-auth-token").isEmpty()) {
            token = nativeHeaders.get("x-auth-token").get(0);
        }
        //bettingPlayerInfo.setBettingMatchId(bettingPlayerInfo.getBettingMatchId()+1);
        bettingPlayerService.playBet(bettingPlayerInfo,"anhtnguyen");
        return bettingPlayerInfo;
    }

    @MessageMapping(value = "/betting-match/comment/{bettingId}")
    @SendTo("/topic/comment/{bettingId}")
    public AddCommentInfo comment(@Valid AddCommentInfo comment,SimpMessageHeaderAccessor headerAccessor) {       
        MultiValueMap<String, String> nativeHeaders = headerAccessor
                .getMessageHeaders()
                .get(StompHeaderAccessor.NATIVE_HEADERS, MultiValueMap.class);
        String token = null;        
        if (nativeHeaders.get("x-auth-token") != null
                && !nativeHeaders.get("x-auth-token").isEmpty()) {
            token = nativeHeaders.get("x-auth-token").get(0);
            String username = tokenProvider.getUsernameFromToken(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            User user = userService.getUsernameByUsername(username);
            System.out.println(user.getName());
            bettingPlayerService.addComment(comment);
        }        
        // System.out.println("cannot get authentication");
        // Todo: call save comment service and return commentinfo
        return comment;
    }

    @MessageExceptionHandler
    public String handleException(Throwable exception,
            SimpMessageHeaderAccessor headerAccessor) {
        MultiValueMap<String, String> nativeHeaders = headerAccessor
                .getMessageHeaders()
                .get(StompHeaderAccessor.NATIVE_HEADERS, MultiValueMap.class);
        String token = null;
        if (nativeHeaders.get("x-auth-token") != null
                && !nativeHeaders.get("x-auth-token").isEmpty()) {
            token = nativeHeaders.get("x-auth-token").get(0);
            System.out.println(token);
            String username = tokenProvider.getUsernameFromToken(token);
            messagingTemplate.convertAndSend("/topic/errors/"+username,"test");
        }
        return exception.getMessage();
    }
}
