'use strict';

export default class roundmanController {
  /* @ngInject */
  constructor($scope) {
    this.scope = $scope;
  }

  addmore(round) {
    if (round.complist.indexOf(round.compet) == -1) {
          round.error = '';
          round.complist.push(round.compet);
          round.compet = '';
    } else {
        round.error = 'Competitor already exist !!!';
      }
  }

  loadtour(round){
    round.tourlist = ['Tournament 1', 'Tournament 2', 'Tournament 3'];
  }

  loadcomp(round){
    round.listcompet = ['Competitor 1', 'Competitor 2', 'Competitor 3'];
  }

}

export default class Round {
  constructor () {
    return {
      controller: roundmanController,
      controllerAs: 'round',
      templateUrl: 'app/common/round-management/round-management.html'
    };
  }
}
