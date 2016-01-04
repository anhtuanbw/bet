'use strict';

export default class InfoBettingMatchController {
  /* @ngInject */
  constructor() {}
}

export default class InfoBettingMatch {
  constructor() {
    return {
      replace: false,
      scope: true,
      controller: InfoBettingMatchController,
      controllerAs: 'infoBettingMatch',
      templateUrl: 'app/common/info-betting-match/info-betting-match.html'
    };
  }
}