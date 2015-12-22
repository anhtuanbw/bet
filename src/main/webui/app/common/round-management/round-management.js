'use strict';

export default class roundmanController {
  /* @ngInject */
  constructor($scope) {
    this.scope = $scope;
  }

  addmore(round) {
    if (round.complist.indexOf(round.compet) == -1
      && typeof round.compet != 'undefined'
      && round.compet !== '') {
          round.error = '';
          round.complist.push(round.compet);
          var ind = round.listcompet.indexOf(round.compet);
          round.listcompet.splice(ind, 1);
          round.compet = '';
    } else if (typeof round.compet == 'undefined' || round.compet === '') {
        round.error = 'Please select Competitor !!!';
    } else {
        round.error = 'Competitor already exist !!!';
      }
  }

  selecttour(round){
    console.log('You select: '+round.tour);
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
