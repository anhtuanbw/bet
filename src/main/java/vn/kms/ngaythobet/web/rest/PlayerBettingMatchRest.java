package vn.kms.ngaythobet.web.rest;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.kms.ngaythobet.domain.betting.BettingMatch;
import vn.kms.ngaythobet.domain.betting.BettingMatchService;
import vn.kms.ngaythobet.domain.betting.BettingPlayer;
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
    private final int SUCCESSFUL_STATUS = 1;

    @Autowired
    public PlayerBettingMatchRest(BettingPlayerService bettingPlayerService, TokenProvider tokenProvider,
            BettingMatchService bettingMatchService) {
        this.bettingPlayerService = bettingPlayerService;
        this.tokenProvider = tokenProvider;
        this.bettingMatchService = bettingMatchService;
    }

    @RequestMapping(value = "/api/betting-match/{bettingMatchId}", method = RequestMethod.GET)
    public BettingMatch getBettingMatch(@PathVariable Long bettingMatchId) {
        return bettingMatchService.findActiveBettingMatchById(bettingMatchId);
    }

    @RequestMapping(value ="/api/betting-player/getBettingPlayerByBettingMatchId/{bettingMatchId}", method = RequestMethod.GET)
    public BettingPlayer getBettingPlayer(@PathVariable Long bettingMatchId) {        
        return bettingPlayerService.getBettingPlayerOfCurrentUserByBettingMatchId(bettingMatchId);
    }

    @RequestMapping(value = "/api/betting-match/bettingMatchStatistics/{bettingMatchId}", method = RequestMethod.GET)
    public BettingMatchStatisticsInfo getbettingMatchStatistics(@PathVariable Long bettingMatchId) {
        return bettingPlayerService.getBettingMatchStatistics(bettingMatchId);
    }

    @MessageMapping(value = "/betting-match/playBet/{bettingMatchId}")
    @SendTo("/topic/playBet/{bettingMatchId}")
    public int playBet(@Valid PlayerBettingMatchInfo bettingPlayerInfo,
            @DestinationVariable Long bettingMatchId, SimpMessageHeaderAccessor headerAccessor) {
        tokenProvider.setAuthenticationFromHeader(headerAccessor);
        bettingPlayerService.playBet(bettingPlayerInfo);
        return SUCCESSFUL_STATUS;
    }

    @MessageMapping(value = "/betting-match/updatePlayBet/{bettingMatchId}")
    @SendTo("/topic/updatePlayBet/{bettingMatchId}")
    public int updateBet(@Valid UpdatePlayerBettingMatchInfo bettingPlayerInfo,
            @DestinationVariable Long bettingMatchId, SimpMessageHeaderAccessor headerAccessor) {
        tokenProvider.setAuthenticationFromHeader(headerAccessor);
        bettingPlayerService.updatePlayBet(bettingPlayerInfo);
        return SUCCESSFUL_STATUS;
    }

    @MessageMapping(value = "/betting-match/comment/{bettingMatchId}")
    @SendTo("/topic/comment/{bettingMatchId}")
    public int comment(@Valid AddCommentInfo comment, @DestinationVariable Long bettingMatchId,
            SimpMessageHeaderAccessor headerAccessor) {
        tokenProvider.setAuthenticationFromHeader(headerAccessor);
        bettingPlayerService.addComment(comment);
        return SUCCESSFUL_STATUS;
    }
}
