package vn.kms.ngaythobet.web.rest;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.kms.ngaythobet.domain.tournament.GroupService;
import vn.kms.ngaythobet.web.dto.CreateGroupInfo;

/**
 * 
 * @author thangpham
 *
 */
@RestController
@RequestMapping("/api")
public class GroupRest {
    @Autowired
    private GroupService groupService;

    @RequestMapping(value = "/group/create", method = POST)
    public void createGroup(@Valid @RequestBody CreateGroupInfo createGroupInfo) {
        groupService.createGroup(createGroupInfo);
    }
}
