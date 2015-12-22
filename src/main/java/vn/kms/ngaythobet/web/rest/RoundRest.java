package vn.kms.ngaythobet.web.rest;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.kms.ngaythobet.domain.tournament.RoundService;
import vn.kms.ngaythobet.web.dto.CreateRoundInfo;

@RestController
@RequestMapping("/api")
public class RoundRest {
    private final RoundService roundService;

    @Autowired
    public RoundRest(RoundService roundService) {
        this.roundService = roundService;
    }

    @RequestMapping(value = "/createRound", method = POST)
    public void createRound(@Valid @RequestBody CreateRoundInfo createRoundInfo) {
        roundService.createRound(createRoundInfo);
    }
}
