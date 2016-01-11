'use strict';

export default class PlayerHistoryController {
  /* @ngInject */
  constructor(PlayerHistoryService, AccountService, $rootScope) {
    this.playerHistoryService = PlayerHistoryService;
    this.rootScope = $rootScope;
    this.accountService = AccountService;

    this.data = [];
    this.player = null;
    this.totalLoss = 0;
    this.sortType = 'match';
    this.sortReverse = false;
    this.getUsername();
    this.rootScope.$on('playerStatistic', (event, groupId) => {
       this.playerStatistic(groupId);
    });

    this.rootScope.$on('selectGroup', (event, group) => {
       this.playerStatistic(group.id);
    });
  }

  getUsername() {
    this.accountService.authen()
    .then(response => {
      if (response.data) {
        this.player = response.data.name;
      } 
    });
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

  convertDateToString() {
    if(this.data !=null) {
      var player;
      for(player in this.data) {
        this.data[player].expiredBetTime = this.getTime(this.data[player].expiredBetTime);
      }
    }
  }

  playerStatistic(groupId) {
    var self = this;
    self.playerHistoryService.playerStatistic(groupId)
    .then(response => {
        self.data = response.data.playerStatistics;
        self.totalLoss = response.data.totalLossAmount;
        self.convertDateToString();
    });
  }

}

export default class PlayerHistory {
  constructor() {
    return {
      replace: false,
      scope: true,
      controller: PlayerHistoryController,
      controllerAs: 'playerHistory',
      templateUrl: 'app/common/player-history/player-history.html'
    };
  }
}
