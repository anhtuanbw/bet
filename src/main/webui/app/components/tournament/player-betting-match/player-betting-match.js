/* global Stomp */
/* global SockJS */
'use strict';

export default class PlayerBettingMatchController {
  /* @ngInject */
  constructor($rootScope, CacheService, BettingMatchService) {
    this.cacheService = CacheService;
    this.bettingMatchService = BettingMatchService;
    this.dataBettingMatch = {};
    this.dataBettingStatistics = {};
    this.comment = '';
    this.stompClient = null;
    this.chooseCompetitor1 = false;
    this.chooseCompetitor2 = false;
    this.namePlayerBetCompetitor1 = [];
    this.namePlayerBetCompetitor2 = [];
    this.namePlayerNotBet = [];
    this.getBettingMatchStatistics();
    this.getBettingMatchInfo();
    // this.connect();
  }

  getBettingMatchInfo() {
    this.bettingMatchService.getBettingMatchInfo('1')
      .then(response => {
        this.dataBettingMatch.balance1 = response.data.balance1;
        this.dataBettingMatch.balance2 = response.data.balance2;
        this.dataBettingMatch.expiredTime = this.getTime(response.data.expiredTime);
        this.dataBettingMatch.round = 'Play-off';
        this.dataBettingMatch.startTime = this.getTime(response.data.expiredTime);
        this.dataBettingMatch.comment = 'Hay qua xa';
      });
  }

  getBettingMatchStatistics() {
    this.bettingMatchService.getBettingMatchStatistics('1')
      .then(response => {

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
      });
  }

  assigningData(data1, data2) {
    var i;
    for (i = 0; i < data1.length; i++) {
      data2.push(data1[i].player.username);
    }
  }

  getTime(timeArray) {
    var i;
    for (i = 0; i < timeArray.length; i++) {
      var timeString = this.formatTime(timeArray[1].toString()) + '/' + this.formatTime(timeArray[2].toString()) + '/' + timeArray[0] +
        ', ' + this.formatTime(timeArray[3].toString()) + ':' + this.formatTime(timeArray[4].toString()) + ':00';
    }
    return timeString;
  }

  formatTime(time) {
    return (time.length === 2 ? time : '0' + time[0]);
  }

  isChooseCompetitor1() {
    this.chooseCompetitor1 = true;
    this.chooseCompetitor2 = false;
  }

  isChooseCompetitor2() {
    this.chooseCompetitor2 = true;
    this.chooseCompetitor1 = false;
  }

  connect() {
    var socket = new SockJS('/betting-match');
    this.stompClient = Stomp.over(socket);

    var self = this;
    this.stompClient.connect({}, function (frame) {
      self.stompClient.subscribe('/topic/comment/1', function (comment) {
            console.log(comment);
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
      'bettingMatchId': 1,
      'comment': '12312'
    }
    console.log(token);
    self.stompClient.send('/app/betting-match/comment/1', { 'x-auth-token': token }, JSON.stringify(requestBody));
  }
}