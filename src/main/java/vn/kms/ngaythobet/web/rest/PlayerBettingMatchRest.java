package vn.kms.ngaythobet.web.rest;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.kms.ngaythobet.domain.betting.BettingMatch;
import vn.kms.ngaythobet.domain.betting.BettingMatchService;
import vn.kms.ngaythobet.domain.betting.BettingPlayerService;
import vn.kms.ngaythobet.infras.security.xauth.TokenProvider;
import vn.kms.ngaythobet.web.dto.AddCommentInfo;
import vn.kms.ngaythobet.web.dto.BettingMatchStatisticsInfo;
import vn.kms.ngaythobet.web.dto.PlayerBettingMatchInfo;
import vn.kms.ngaythobet.web.dto.UpdatePlayerBettingMatchInfo;

@RestController
public class PlayerBettingMatchRest {

    private final BettingPlayerService bettingPlayerService;
    private final TokenProvider tokenProvider;
    private final BettingMatchService bettingMatchService;
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    public PlayerBettingMatchRest(BettingPlayerService bettingPlayerService, TokenProvider tokenProvider,
            SimpMessageSendingOperations messagingTemplate, BettingMatchService bettingMatchService
            ) {
        this.bettingPlayerService = bettingPlayerService;
        this.tokenProvider = tokenProvider;
        this.messagingTemplate = messagingTemplate;
        this.bettingMatchService = bettingMatchService;
    }

    @RequestMapping(value = "/api/betting-match/{bettingMatchId}", method = RequestMethod.GET)
    public BettingMatch getBettingMatch(@PathVariable Long bettingMatchId) {
        return bettingMatchService.findActiveBettingMatchById(bettingMatchId);
    }

    @RequestMapping(value = "/api/betting-match/bettingMatchStatistics/{bettingMatchId}", method = RequestMethod.GET)
    public BettingMatchStatisticsInfo getBettingPlayers2(@PathVariable Long bettingMatchId) {
        return bettingPlayerService.getBettingMatchStatistics(bettingMatchId);
    }

    @MessageMapping(value = "/betting-match/playBet/{bettingMatchId}")
    @SendTo("/topic/playBet/{bettingMatchId}")
    public PlayerBettingMatchInfo playBet(@Valid PlayerBettingMatchInfo bettingPlayerInfo,
            SimpMessageHeaderAccessor headerAccessor) {
        tokenProvider.setAuthenticationFromHeader(headerAccessor);
        bettingPlayerService.playBet(bettingPlayerInfo);
        return bettingPlayerInfo;
    }

    @MessageMapping(value = "/betting-match/updatePlayBet/{bettingMatchId}")
    @SendTo("/topic/updatePlayBet/{bettingMatchId}")
    public String updateBet(UpdatePlayerBettingMatchInfo bettingPlayerInfo,
            SimpMessageHeaderAccessor headerAccessor) {
        tokenProvider.setAuthenticationFromHeader(headerAccessor);
        bettingPlayerService.updatePlayBet(bettingPlayerInfo);
        return "successful";
    }

    @MessageMapping(value = "/betting-match/comment/{bettingMatchId}")
    @SendTo("/topic/comment/{bettingMatchId}")
    public AddCommentInfo comment(AddCommentInfo comment, SimpMessageHeaderAccessor headerAccessor) {
        bettingPlayerService.addComment(comment);
        return comment;
    }

    @MessageExceptionHandler
    public void handleException(Throwable exception, SimpMessageHeaderAccessor headerAccessor) {
        tokenProvider.setAuthenticationFromHeader(headerAccessor);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if (username != null) {
            messagingTemplate.convertAndSend("/topic/errors/" + username, exception.getMessage());
        }
    }
}
