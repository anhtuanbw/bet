// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.web.rest;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.kms.ngaythobet.domain.core.ChangeLogService;
import vn.kms.ngaythobet.web.dto.CommentInfo;
import vn.kms.ngaythobet.web.dto.HistoryBetting;

@RestController
@RequestMapping("/api/player")
public class PlayerHistoryRest {
    private final ChangeLogService changeLogService;

    @Autowired
    public PlayerHistoryRest(ChangeLogService changeLogService) {
        this.changeLogService = changeLogService;
    }

    @RequestMapping(value = "/comments/{bettingMatchId}", method = GET)
    public List<CommentInfo> getComments(@PathVariable Long bettingMatchId) {
        return changeLogService.getComments(bettingMatchId);
    }
    
    @RequestMapping(value = "/getHistoryBetting", method = GET)
    public List<HistoryBetting> getHistoryBetting() {
        return changeLogService.getHistoryBetting();
    }
}
