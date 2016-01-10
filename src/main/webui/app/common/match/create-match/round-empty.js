export default class RoundEmptyController {
  /* @ngInject */
  constructor() { }
}

export default class RoundEmpty {
  constructor() {
    return {
      replace: false,
      scope: true,
      controller: RoundEmptyController,
      controllerAs: 'roundEmpty',
      templateUrl: 'app/common/match/create-match/round-empty.html'
    };
  }
}