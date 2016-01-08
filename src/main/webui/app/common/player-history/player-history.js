'use strict';

export default class PlayerHistoryController {
  /* @ngInject */
  constructor(PlayerHistoryService, $rootScope) {
    this.playerHistoryService = PlayerHistoryService;
    this.rootScope = $rootScope;

    this.data = [];
    this.totalLoss = 0;
    this.sortType = 'match';
    this.sortReverse = false;
    this.rootScope.$on('playerStatistic', (event, groupId) => {
       this.playerStatistic(groupId);
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

  sumLoss() {
    this.totalLoss = 0;
    if(this.data !=null) {
      var player;
      for(player in this.data) {
        this.totalLoss+= this.data[player].lossAmount;
        this.data[player].expiredBetTime = this.getTime(this.data[player].expiredBetTime);
      }
    }
  }

  playerStatistic(groupId) {
    var self = this;
    self.playerHistoryService.playerStatistic(groupId)
    .then(response => {
        self.data = response.data;
        self.sumLoss();
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
