package vn.kms.ngaythobet.web.rest;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.kms.ngaythobet.domain.statistic.PlayerStatisticService;
import vn.kms.ngaythobet.domain.statistic.TotalPlayerStatistic;
import vn.kms.ngaythobet.web.dto.PlayerStatisticInfo;

@RestController
@RequestMapping("/api/")
public class PlayerStatisticRest {
    private final PlayerStatisticService playerStatisticService;

    @Autowired
    public PlayerStatisticRest(PlayerStatisticService playerStatisticService) {
        this.playerStatisticService = playerStatisticService;
    }

    @RequestMapping(value = "/playerStatistic", method = POST)
    public TotalPlayerStatistic playerStatistic(@Valid @RequestBody PlayerStatisticInfo playerStatisticInfo) {
        return playerStatisticService.playerStatistic(playerStatisticInfo);
    }

    @RequestMapping(value = "/statistic/player/getLostAmount/{bettingMatchId}", method = GET)
    public double getLostAmount(@PathVariable("bettingMatchId") Long bettingMathId) {
        return playerStatisticService.getLostAmountByUser(bettingMathId);
    }

}
