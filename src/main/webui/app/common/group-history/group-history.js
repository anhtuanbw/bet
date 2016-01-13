'use strict';

export default class GroupHistoryController {
  /* @ngInject */
  constructor(GroupService, $location, $rootScope, $stateParams) {
    this.groupService = GroupService;
    this.location = $location;
    this.statisticData = [];
    this.sortType = 'player';
    this.sortReverse = false;
    this.activePlayer = false;
    this.groupId = $stateParams.groupId;
    this.totalLost = 0;
    this.getStatisticInfo();
  }
  
  getStatisticInfo() {
    this.groupService.getStatisticInfo(this.groupId)
    .then(response => {
      this.statisticData = response.data;
    })
    .catch(error => {
      if (error.status === 401) {
        this.location.path('/unauthorized').search({ lastUrl: this.location.path() });
      }
    });
  }
}

export default class GroupHistory {
  constructor() {
    return {
      replace: false,
      scope: true,
      controller: GroupHistoryController,
      controllerAs: 'groupHistory',
      templateUrl: 'app/common/group-history/group-history.html'
    };
  }
}
