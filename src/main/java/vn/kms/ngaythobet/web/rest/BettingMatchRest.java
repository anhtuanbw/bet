package vn.kms.ngaythobet.web.rest;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.kms.ngaythobet.domain.betting.BettingMatchService;
import vn.kms.ngaythobet.domain.betting.BettingService;
import vn.kms.ngaythobet.domain.tournament.MatchRepository;
import vn.kms.ngaythobet.web.dto.CreateBettingMatchInfo;
import vn.kms.ngaythobet.web.dto.UpdateBettingMatchInfo;

@RestController
@RequestMapping("/api")
public class BettingMatchRest {

    private final BettingMatchService bettingMatchService;

    @Autowired
    public BettingMatchRest(BettingMatchService bettingMatchService) {
        this.bettingMatchService = bettingMatchService;
    }

    @RequestMapping(value = "/createBettingMatch", method = POST)
    public void createBettingMatch(@Valid @RequestBody CreateBettingMatchInfo createBettingMatchInfo) {

        bettingMatchService.createBettingMatch(createBettingMatchInfo);
    }
    
    @RequestMapping(value = "/updateBettingMatch", method = POST)
    public void updateBettingMatch(@Valid @RequestBody UpdateBettingMatchInfo updateBettingMatchInfo){
        bettingMatchService.updateBettingMatch(updateBettingMatchInfo);
    }
}