/* global Stomp */
/* global SockJS */
'use strict';

export default class PlayerBettingMatchController {
  /* @ngInject */
  constructor($rootScope, CacheService, BettingMatchService, AccountService, MatchService, $stateParams) {
    this.cacheService = CacheService;
    this.bettingMatchService = BettingMatchService;
    this.dataInfoMatch = {};
    this.rootScope = $rootScope;
    this.accountService = AccountService;
    this.matchService = MatchService;
    this.dataInfoMatch.bettingMatchId = $stateParams.matchId;
    this.dataBettingMatch = {};
    this.dataBettingStatistics = {};
    this.getComment = {};
    this.commentArr = [];
    this.comment = '';
    this.numberComments = '';
    this.paging = 0;
    this.notError = false;
    this.stompClient = null;
    this.chooseCompetitor1 = false;
    this.chooseCompetitor2 = false;
    this.checkLengthComments = false;
    this.checkPaging = false;
    this.currentBettingPlayer = {};
    this.namePlayerBetCompetitor1 = [];
    this.namePlayerBetCompetitor2 = [];
    this.namePlayerNotBet = [];
    this.getBettingMatchStatistics();
    this.getRoundNameByBettingMatch();
    this.getBettingMatchInfo();
    this.getLostAmount();
    this.getNumberComments();
    this.getComments();
    this.checkExpiredBettingMatch();
    this.getBettingPlayer();
  }

  getBettingMatchInfo() {
    this.bettingMatchService.getBettingMatchInfo(this.dataInfoMatch.bettingMatchId)
      .then(response => {
        if (response.data) {
          this.dataInfoMatch.competitor1Name = response.data.match.competitor1.name;
          this.dataInfoMatch.competitor2Name = response.data.match.competitor2.name;
          this.dataInfoMatch.competitor1Id = response.data.match.competitor1.id;
          this.dataInfoMatch.competitor2Id = response.data.match.competitor2.id;
          this.dataBettingMatch.balance1 = response.data.balance1;
          this.dataBettingMatch.balance2 = response.data.balance2;
          this.dataBettingMatch.expiredTime = this.getTime(response.data.expiredTime);
          this.dataBettingMatch.startTime = this.getTime(response.data.match.matchTime);
          this.dataBettingMatch.comment = response.data.description;
          this.dataBettingMatch.score1 = this.checkScore(response.data.match.score1);
          this.dataBettingMatch.score2 = this.checkScore(response.data.match.score2);
          this.dataBettingMatch.timeNow = new Date().getTime();
        }
      });
  }

  checkScore(score) {
    if (score === null) {
      score = '?';
    }
    return score;
  }

  getRoundNameByBettingMatch() {
    this.bettingMatchService.getRoundNameByBettingMatch(this.dataInfoMatch.bettingMatchId)
      .then(response => {
          this.dataBettingMatch.round = response.data;
      });
  }

  getBettingMatchStatistics() {
    this.bettingMatchService.getBettingMatchStatistics(this.dataInfoMatch.bettingMatchId)
      .then(response => {
        if (response.data) {
          this.namePlayerBetCompetitor1 = [];
          this.namePlayerBetCompetitor2 = [];
          this.namePlayerNotBet = [];
          this.dataBettingMatch.totalPlayer = response.data.totalBettingPlayers.length;

          this.dataBettingStatistics.numberPlayerChoose1 = response.data.bettingPlayersChooseCompetitor1.length;
          this.dataBettingStatistics.numberPlayerChoose2 = response.data.bettingPlayersChooseCompetitor2.length;

          this.dataBettingStatistics.percentOfChoosingCompetitor1 = response.data.percentOfChoosingCompetitor1;
          this.dataBettingStatistics.percentOfChoosingCompetitor2 = response.data.percentOfChoosingCompetitor2;

          this.assigningData(response.data.bettingPlayersChooseCompetitor1, this.namePlayerBetCompetitor1);
          this.assigningData(response.data.bettingPlayersChooseCompetitor2, this.namePlayerBetCompetitor2);

          var i;
          for (i = 0; i < response.data.userNotBet.length; i++) {
            this.namePlayerNotBet.push(response.data.userNotBet[i].username);
          }

          this.rootScope.percentOfCompetitor1 = this.dataBettingStatistics.percentOfChoosingCompetitor1;
          this.rootScope.percent1 = { width: this.rootScope.percentOfCompetitor1 + '%' };
          this.rootScope.percentOfCompetitor2 = this.dataBettingStatistics.percentOfChoosingCompetitor2;
          this.rootScope.percent2 = { width: this.rootScope.percentOfCompetitor2 + '%' };
        }
      });
  }

  getBettingPlayer() {
    this.bettingMatchService.getBettingPlayer(this.dataInfoMatch.bettingMatchId)
      .then(response => {
        if (response.data) {
          this.currentBettingPlayer.id = response.data.id;
          this.currentBettingPlayer.betCompetitorId = response.data.betCompetitor.id;
          this.isSelectedCompetitor();
        }
      });
  }

  checkExpiredBettingMatch() {
    this.bettingMatchService.checkExpiredBettingMatch(this.dataInfoMatch.bettingMatchId)
      .then(response => {
        this.notError = response.data;
        if (this.notError) {

          this.disconnect();
          this.connect();
        }
      });
  }

  getLostAmount() {
    this.bettingMatchService.getLostAmount(this.dataInfoMatch.bettingMatchId)
      .then(response => {
        this.currentBettingPlayer.loseBettingMatch = response.data;
      });
  }

  assigningData(data1, data2) {
    var i;
    for (i = 0; i < data1.length; i++) {
      data2.push(data1[i].player.username);
    }
  }

  getTime(timeArray) {
    var i, timeString;
    for (i = 0; i < timeArray.length; i++) {
      timeString = this.formatTime(timeArray[1].toString()) + '/' + this.formatTime(timeArray[2].toString()) + '/' + timeArray[0] +
      ', ' + this.formatTime(timeArray[3].toString()) + ':' + this.formatTime(timeArray[4].toString()) + ((timeArray[5]) ? ':' + this.formatTime(timeArray[5].toString()) : '');
    }
    return timeString;
  }

  formatTime(time) {
    return (time.length === 2 ? time : '0' + time[0]);
  }

  isChooseCompetitor1() {
    this.chooseCompetitor1 = true;
    this.chooseCompetitor2 = false;
    this.chooseBet(this.dataInfoMatch.competitor1Id);
  }

  isChooseCompetitor2() {
    this.chooseCompetitor2 = true;
    this.chooseCompetitor1 = false;
    this.chooseBet(this.dataInfoMatch.competitor2Id);
  }

  isSelectedCompetitor() {
    if (this.dataInfoMatch.competitor1Id === this.currentBettingPlayer.betCompetitorId) {
      this.chooseCompetitor1 = true;
    }
    else if (this.dataInfoMatch.competitor2Id === this.currentBettingPlayer.betCompetitorId) {
      this.chooseCompetitor2 = true;
    }
  }

  setPaging() {
    this.paging += 1;
    this.getComments();
  }

  getComments() {
    this.bettingMatchService.getComment(this.dataInfoMatch.bettingMatchId, this.paging)
      .then(response => {
        this.commentArr = [];
        var i;
        if (response.data.length === 0) {
          this.checkLengthComments = true;
        }

        for (i = 0; i < response.data.length; i++) {
          this.commentArr.push(response.data[i]);
          this.commentArr[i].timestamp = this.getTime(response.data[i].timestamp);
        }
      });
  }

  getNumberComments() {
    this.bettingMatchService.getNumberComments(this.dataInfoMatch.bettingMatchId)
      .then(response => {
        this.numberComments = response.data - 1;
        if (this.numberComments > 10) {
          this.checkPaging = true;
        }
      });
  }

  refreshBettingMatch() {
    this.checkLengthComments = false;
    this.getBettingPlayer();
    this.getBettingMatchStatistics();
    this.getNumberComments();
    this.getComments();
  }

  connect() {
    var socket = new SockJS('/betting-match');
    this.stompClient = Stomp.over(socket);

    var self = this;
    self.stompClient.connect({}, function () {
      self.stompClient.subscribe('/topic/comment/' + self.dataInfoMatch.bettingMatchId, function () {
        self.checkLengthComments = false;
        self.getNumberComments();
        self.getComments();
      });

      self.stompClient.subscribe('/topic/playBet/' + self.dataInfoMatch.bettingMatchId, function () {
        self.refreshBettingMatch();
      });

      self.stompClient.subscribe('/topic/updatePlayBet/' + self.dataInfoMatch.bettingMatchId, function () {
        self.refreshBettingMatch();
      });

    });
  }

  disconnect() {
    var self = this;
    if (self.stompClient != null) {
      self.stompClient.disconnect();
    }
  }

  sendComment() {
    var self = this;
    var token = this.cacheService.get('loginUser');
    var requestBody = {
      'bettingMatchId': self.dataInfoMatch.bettingMatchId,
      'comment': self.comment
    };

    self.stompClient.send('/app/betting-match/comment/' + self.dataInfoMatch.bettingMatchId, { 'x-auth-token': token }, JSON.stringify(requestBody));
    self.comment = '';
  }

  chooseBet(competitorId) {
    var self = this;
    var token = this.cacheService.get('loginUser');
    if (self.currentBettingPlayer.betCompetitorId) {
      var requestBodyUpdate = {
        'bettingPlayerId': self.currentBettingPlayer.id,
        'competitorId': competitorId
      };

      self.stompClient.send('/app/betting-match/updatePlayBet/' + self.dataInfoMatch.bettingMatchId, { 'x-auth-token': token }, JSON.stringify(requestBodyUpdate));
    }
    else {

      var requestBody = {
        'bettingMatchId': self.dataInfoMatch.bettingMatchId,
        'competitorId': competitorId,
        'comment': self.comment
      };

      self.stompClient.send('/app/betting-match/playBet/' + self.dataInfoMatch.bettingMatchId, { 'x-auth-token': token }, JSON.stringify(requestBody));
    }

    self.comment = '';

  }
}