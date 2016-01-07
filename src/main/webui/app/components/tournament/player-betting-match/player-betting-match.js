/* global Stomp */
/* global SockJS */
'use strict';

export default class PlayerBettingMatchController {
  /* @ngInject */
  constructor($rootScope, CacheService, BettingMatchService) {
    this.cacheService = CacheService;
    this.bettingMatchService = BettingMatchService;
    this.dataInfoMatch = {};
    $rootScope.$on('playerBettingMatch', (event, data) => {
      this.dataInfoMatch = data;
      this.dataBettingMatch = {};
      this.dataBettingStatistics = {};
      this.getComment = {};
      this.commentArr = [];
      this.comment = '';
      this.stompClient = null;
      this.chooseCompetitor1 = false;
      this.chooseCompetitor2 = false;
      this.currentBettingPlayer = {};
      this.namePlayerBetCompetitor1 = [];
      this.namePlayerBetCompetitor2 = [];
      this.namePlayerNotBet = [];
      this.getBettingMatchStatistics();
      this.getBettingMatchInfo();
      this.getComments();
      this.disconnect();
      this.connect();
      this.getBettingPlayer();
    });

  }

  getBettingMatchInfo() {
    this.bettingMatchService.getBettingMatchInfo(this.dataInfoMatch.bettingMatchId)
      .then(response => {
        if (response.data) {
          this.dataBettingMatch.balance1 = response.data.balance1;
          this.dataBettingMatch.balance2 = response.data.balance2;
          this.dataBettingMatch.expiredTime = this.getTime(response.data.expiredTime);
          this.dataBettingMatch.round = this.dataInfoMatch.roundName;
          this.dataBettingMatch.startTime = this.getTime(this.dataInfoMatch.time);
          this.dataBettingMatch.comment = response.data.description;
        }
      });
  }

  getBettingMatchStatistics() {
    this.bettingMatchService.getBettingMatchStatistics(this.dataInfoMatch.bettingMatchId)
      .then(response => {
        if (response.data) {
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

  // getCurrentTime() {
  //   var d = new Date();
  //   var n = d.getTime();
  // }

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

  getComments() {
    this.bettingMatchService.getComment(this.dataInfoMatch.bettingMatchId)
      .then(response => {
        if (response.data) {
          var i;
          for (i = 0; i < response.data.length; i++) {
            this.commentArr.push(response.data[i]);
            this.commentArr[i].timestamp = this.getTime(response.data[i].timestamp);
          }
        }
      });
  }

  refreshBettingMatch() {
    this.commentArr = [];
    this.namePlayerBetCompetitor1 = [];
    this.namePlayerBetCompetitor2 = [];
    this.namePlayerNotBet = [];
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
        self.commentArr = [];
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