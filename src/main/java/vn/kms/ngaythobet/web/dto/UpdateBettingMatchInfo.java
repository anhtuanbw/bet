package vn.kms.ngaythobet.web.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import vn.kms.ngaythobet.domain.betting.BettingMatch;
import vn.kms.ngaythobet.domain.tournament.Group;
import vn.kms.ngaythobet.domain.tournament.Match;
import vn.kms.ngaythobet.domain.validation.EntityExist;
import vn.kms.ngaythobet.domain.validation.ExpritedTimeValid;
import vn.kms.ngaythobet.domain.validation.ModeratorAccess;

@ModeratorAccess
@ExpritedTimeValid(entityId = "matchId", targetField = "expiredTime", fieldName = "matchTime", type = Match.class, message = "{validation.bettingMatch.expritedTime.message}")
public class UpdateBettingMatchInfo {

    @EntityExist(type = BettingMatch.class)
    private Long bettingMatchId;
    
    @NotNull
    @Min(value = 0,message = "{validation.balance.invalid}")
    private BigDecimal balance1;

    @NotNull
    @Min(value = 0,message = "{validation.balance.invalid}")
    private BigDecimal balance2;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm a")
    private LocalDateTime expiredTime;

    @NotNull
    @Min(value = 0, message = "{validation.betAmount.invalid}")
    private BigDecimal betAmount;

    @NotNull
    @EntityExist(type = Match.class, message = "{validation.existMatch.message}")
    private long matchId;

    @Size(min = 0, max = 512)
    private String decription;

    @NotNull
    @EntityExist(type = Group.class, message = "{validation.existGroup.message}")
    private Long groupId;
    
    private boolean activated;

    public Long getBettingMatchId() {
        return bettingMatchId;
    }

    public void setBettingMatchId(Long bettingMatchId) {
        this.bettingMatchId = bettingMatchId;
    }

    public BigDecimal getBalance1() {
        return balance1;
    }

    public void setBalance1(BigDecimal balance1) {
        this.balance1 = balance1;
    }

    public BigDecimal getBalance2() {
        return balance2;
    }

    public void setBalance2(BigDecimal balance2) {
        this.balance2 = balance2;
    }

    public LocalDateTime getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(LocalDateTime expiredTime) {
        this.expiredTime = expiredTime;
    }

    public BigDecimal getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(BigDecimal betAmount) {
        this.betAmount = betAmount;
    }

    public long getMatchId() {
        return matchId;
    }

    public void setMatchId(long matchId) {
        this.matchId = matchId;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    
}
