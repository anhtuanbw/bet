'use strict';

export default class PlayerStatisticsController {
  /* @ngInject */
  constructor() {}
}

export default class PlayerStatistics {
  constructor() {
    return {
      replace: false,
      scope: true,
      controller: PlayerStatisticsController,
      controllerAs: 'playerStatistics',
      templateUrl: 'app/common/player-statistics/player-statistics.html'
    };
  }
}