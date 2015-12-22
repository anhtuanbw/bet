// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.web.rest;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.kms.ngaythobet.domain.tournament.MatchService;
import vn.kms.ngaythobet.web.dto.CreateMatchInfo;



@RestController
@RequestMapping("/api")
public class MatchRest {
    private final MatchService matchService;


    @Autowired
    public MatchRest(MatchService matchService) {
        this.matchService = matchService;
    }

    @RequestMapping(value = "/create-match", method = POST)
    public void createMatch(@Valid @RequestBody CreateMatchInfo createMatchInfo) {
        matchService.createMatch(createMatchInfo);
    }
}
