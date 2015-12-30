'use strict';

export default class BettingMatchController {
  /* @ngInject */
  constructor(RoundService){
    this.RoundService = RoundService;
  }
  loadRound(data){
    console.log('acb');
    data.roundList.push('list');
    data.roundList.push('agh');
    data.roundList.push('lifryjst');
  }
}

export default class betting {
  constructor() {
    return {
      replace: false,
      scope: true,
      controller: BettingMatchController,
      controllerAs: 'betting',
      templateUrl: 'app/common/betting-match-management/betting-match-management.html'
    };
  }
}
