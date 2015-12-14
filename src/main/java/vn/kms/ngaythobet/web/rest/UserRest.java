// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.kms.ngaythobet.domain.core.User;
import vn.kms.ngaythobet.domain.core.UserRepository;
import vn.kms.ngaythobet.domain.util.DataNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRest {
    private final MessageSource messageSource;

    private final UserRepository userRepo;

    @Value("${ngaythobet.paging-size}")
    private int pageSize;

    @Autowired
    public UserRest(MessageSource messageSource, UserRepository userRepo) {
        this.messageSource = messageSource;
        this.userRepo = userRepo;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<User> getUsersPerPage(
        @RequestParam(name = "sortField", required = false, defaultValue = "name") String sortField,
        @RequestParam(name = "sortType", required = false, defaultValue = "ASC") Direction sortType,
        @RequestParam(name = "page", required = false, defaultValue = "0") int page) {

        Pageable pageable = new PageRequest(page, pageSize, new Sort(sortType, sortField));

        return userRepo.findAll(pageable);
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public User getUser(@PathVariable String username) {
        return userRepo
                .findOneByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("exception.userRest.user-not-found", username));
    }
}
