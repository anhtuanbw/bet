// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.web.rest;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.kms.ngaythobet.domain.statistic.GroupStatisticService;
import vn.kms.ngaythobet.domain.statistic.GroupStatistic;

@RestController
@RequestMapping("/api/group")
public class GroupStatisticRest {
    private final GroupStatisticService groupStatisticService;

    @Autowired
    public GroupStatisticRest(GroupStatisticService groupStatisticService) {
        this.groupStatisticService = groupStatisticService;
    }

    @RequestMapping(value = "/statistic/{groupId}", method = GET)
    public List<GroupStatistic> getGroupStatistic(@PathVariable Long groupId) {
        return groupStatisticService.getGroupStatistic(groupId);
    }
}