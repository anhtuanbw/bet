package vn.kms.ngaythobet.web.rest;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.kms.ngaythobet.domain.betting.BettingMatch;
import vn.kms.ngaythobet.domain.betting.BettingMatchService;
import vn.kms.ngaythobet.domain.betting.BettingPlayerService;
import vn.kms.ngaythobet.domain.core.UserService;
import vn.kms.ngaythobet.infras.security.xauth.TokenProvider;
import vn.kms.ngaythobet.web.dto.AddCommentInfo;
import vn.kms.ngaythobet.web.dto.PlayerBettingMatchInfo;

@RestController
public class PlayerBettingMatchRest {

    private final BettingMatchService bettingMatchService;
    private final BettingPlayerService bettingPlayerService;
    private final TokenProvider tokenProvider;
    private SimpMessageSendingOperations messagingTemplate;
    
    @Autowired
    public PlayerBettingMatchRest(BettingMatchService bettingMatchService,
            BettingPlayerService bettingPlayerService,
            UserDetailsService userDetailsService,
            TokenProvider tokenProvider,
            UserService userService,
            SimpMessageSendingOperations messagingTemplate) {
        this.bettingMatchService = bettingMatchService;
        this.bettingPlayerService = bettingPlayerService;
        this.tokenProvider = tokenProvider;
        this.messagingTemplate = messagingTemplate;
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
        String username = tokenProvider.getUsernameByHeader(headerAccessor);
        bettingPlayerService.playBet(bettingPlayerInfo, username);
        return bettingPlayerInfo;
    }

    @MessageMapping(value = "/betting-match/comment/{bettingId}")
    @SendTo("/topic/comment/{bettingId}")
    public AddCommentInfo comment(@Valid AddCommentInfo comment,SimpMessageHeaderAccessor headerAccessor) {       
        bettingPlayerService.addComment(comment);
        return comment;
    }

    @MessageExceptionHandler
    public void handleException(Throwable exception,
            SimpMessageHeaderAccessor headerAccessor) {
        String username = null;
        String message = exception.getMessage();
        try {
            username = tokenProvider.getUsernameByHeader(headerAccessor);
        } catch (Exception e) {
            message = e.getMessage();
        } finally {
            messagingTemplate.convertAndSend("/topic/errors/" + username, message);
        }
    }
}
