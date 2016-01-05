package vn.kms.ngaythobet.web.rest;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.kms.ngaythobet.domain.statistic.PlayerStatistic;
import vn.kms.ngaythobet.domain.statistic.PlayerStatisticService;
import vn.kms.ngaythobet.web.dto.GetPlayerBettingMatchesByPlayerAndGroupInfo;

@RestController
@RequestMapping("/api/")
public class PlayerStatisticRest {
    private final PlayerStatisticService playerStatisticService;

    @Autowired
    public PlayerStatisticRest(PlayerStatisticService playerStatisticService) {
        this.playerStatisticService = playerStatisticService;
    }

    @RequestMapping(value = "/playerStatistic", method = POST)
    public List<PlayerStatistic> getPlayerBettingMatchesByPlayerAndGroupInfo(
            @Valid @RequestBody GetPlayerBettingMatchesByPlayerAndGroupInfo getPlayerBettingMatchesByPlayerAndGroupInfo) {
        return playerStatisticService.getPlayerBettingMatchesByPlayerAndGroup(getPlayerBettingMatchesByPlayerAndGroupInfo);
    }

}
