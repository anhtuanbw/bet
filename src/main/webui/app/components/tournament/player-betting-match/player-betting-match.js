/* global Stomp */
/* global SockJS */
'use strict';

export default class PlayerBettingMatchController {
  /* @ngInject */
  constructor($rootScope, CacheService, BettingMatchService, AccountService, MatchService, $stateParams) {
    this.cacheService = CacheService;
    this.bettingMatchService = BettingMatchService;
    this.dataInfoMatch = {};
    this.accountService = AccountService;
    this.matchService = MatchService;
    this.dataInfoMatch.bettingMatchId = $stateParams.matchId;
    // $rootScope.$on('playerBettingMatch', (event, data) => {
      this.getMatchById();
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
      this.messageError = '';
      this.currentBettingPlayer = {};
      this.namePlayerBetCompetitor1 = [];
      this.namePlayerBetCompetitor2 = [];
      this.namePlayerNotBet = [];
      this.getBettingMatchStatistics();
      this.getBettingMatchInfo();
      this.getLostAmount();
      this.getComments();
      this.checkExpiredBettingMatch();
      this.getBettingPlayer();
    // });
  }

  getMatchById() {
    this.matchService.getMatchInfo(this.dataInfoMatch.bettingMatchId)
    .then(response => {
      console.log(response.data);
      this.dataInfoMatch.bettingMatchId = response.data.id;
      this.dataInfoMatch.competitor1Name = response.data.competitor1.name;
      this.dataInfoMatch.competitor2Name = response.data.competitor2.name;
      this.dataInfoMatch.competitor1Id = response.data.competitor1.id;
      this.dataInfoMatch.competitor2Id = response.data.competitor2.id;
      this.dataInfoMatch.score1 = response.data.score1;
      this.dataInfoMatch.score2 = response.data.score2;
      this.dataInfoMatch.time = response.data.matchTime;
    });
  }

  getBettingMatchInfo() {
    console.log(this.dataInfoMatch.bettingMatchId);
    this.bettingMatchService.getBettingMatchInfo(this.dataInfoMatch.bettingMatchId)
      .then(response => {
        if (response.data) {
          this.dataBettingMatch.balance1 = response.data.balance1;
          this.dataBettingMatch.balance2 = response.data.balance2;
          this.dataBettingMatch.expiredTime = this.getTime(response.data.expiredTime);
          this.dataBettingMatch.round = this.dataInfoMatch.roundName;
          this.dataBettingMatch.startTime = this.getTime(this.dataInfoMatch.time);
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
        } else {
          this.messageError = 'The betting match is expired';
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
      ', ' + this.formatTime(timeArray[3].toString()) + ':' + this.formatTime(timeArray[4].toString()) + ((timeArray[5]) ? ':' + this.formatTime(timeArray[5].toString()) : ':00');
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
        this.numberComments = response.data.commentCount;
        this.commentArr = [];
        var i;
        if (response.data.comments.length === 0) {
          this.checkLengthComments = true;
        }
        
        if (response.data.comments.length >= 10) {
          this.checkPaging = true;
        }
        
        for (i = 0; i < response.data.comments.length; i++) {
          this.commentArr.push(response.data.comments[i]);
          this.commentArr[i].timestamp = this.getTime(response.data.comments[i].timestamp);
        }
       
      });
  }

  refreshBettingMatch() {
    this.getBettingPlayer();
    this.getBettingMatchStatistics();
    this.getComments();
  }

  connect() {
    var socket = new SockJS('/betting-match');
    this.stompClient = Stomp.over(socket);

    var self = this;
    self.stompClient.connect({}, function () {
      self.stompClient.subscribe('/topic/comment/' + self.dataInfoMatch.bettingMatchId, function () {
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
    this.checkLengthComments = false;
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
    this.checkLengthComments = false;
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