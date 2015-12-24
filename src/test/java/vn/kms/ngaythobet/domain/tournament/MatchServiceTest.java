// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.tournament;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.security.crypto.password.PasswordEncoder;

import vn.kms.ngaythobet.BaseTest;
import vn.kms.ngaythobet.domain.core.MailService;
import vn.kms.ngaythobet.domain.tournament.CompetitorRepository;
import vn.kms.ngaythobet.domain.tournament.MatchRepository;
import vn.kms.ngaythobet.domain.tournament.MatchService;
import vn.kms.ngaythobet.domain.tournament.RoundRepository;
import vn.kms.ngaythobet.domain.tournament.Tournament;
import vn.kms.ngaythobet.domain.tournament.TournamentRepository;

public class MatchServiceTest extends BaseTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Autowired
    private TournamentRepository tournamentRepo;
    
    @Autowired
    private CompetitorRepository competitorRepo;
    
    @Autowired
    private RoundRepository roundRepo;
    
    @Autowired
    private MatchRepository matchRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private MatchService matchService;

    @Override
    protected void doStartUp() {
        MailService mailService = mock(MailService.class);
        when(mailService.sendEmailAsync(anyString(), anyString(), anyString(), anyBoolean(), anyBoolean()))
                .thenReturn(new AsyncResult<>(true));

        matchService = new MatchService(tournamentRepo, competitorRepo, roundRepo, matchRepo);
    }
    
    @Test
    public void testCreateMatch(){
        //TODO
    }
}
