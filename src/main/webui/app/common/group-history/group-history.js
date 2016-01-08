'use strict';

export default class GroupHistoryController {
  /* @ngInject */
  constructor(GroupService, $location, $rootScope) {
    this.groupService = GroupService;
    this.location = $location;
    this.statisticData = [];
    this.sortType = 'player';
    this.sortReverse = false;
    this.activePlayer = false;
    
    $rootScope.$on('selectGroup', (event, groupInfo) => {
      if (groupInfo) {
        this.groupInfo = groupInfo;
      }
      this.getStatisticInfo();
    });
  }
  
  getStatisticInfo() {
    this.groupService.getStatisticInfo(this.groupInfo.id)
    .then(response => {
      this.statisticData = response.data;
    })
    .catch(error => {
      if (error.status === 401) {
        this.location.path('/unauthorized');
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
