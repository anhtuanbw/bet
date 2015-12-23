'use strict';

export default class roundManController {
  /* @ngInject */
  constructor($scope, RoundService) {
    this.scope = $scope;
    this.RoundService = RoundService;
  }

  addMore(round) {
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

  selectTour(round){
    //Load depend Competitor follow selected tour
    console.log('You select: '+round.tour);
    this.RoundService.getAllTournament()
    .then(response => {
      // var ind = response.data.indexOf(round.tour);
      // console.log('ind: '+ind);
      // var j;
      // for (var i = 0; i < response.data.length; i++) {
      //   console.log(response.data[0].competitors.length);
      // }
    });
  }

  loadTour(round){
    //round.tourlist = ['Tournament 1', 'Tournament 2', 'Tournament 3'];
    this.RoundService.getAllTournament()
    .then(response => {
      //success
      var i;
      for (i = 0; i < response.data.length; i++) {
          round.tourlist.push(response.data[i].name);
      }
      //console.log(response.data[1].competitors[0].name);
    });

  }

  // loadcomp(round){
  //   round.listcompet = ['Competitor 1', 'Competitor 2', 'Competitor 3'];
  // }

}

export default class Round {
  constructor () {
    return {
      controller: roundManController,
      controllerAs: 'round',
      templateUrl: 'app/common/round-management/round-management.html'
    };
  }
}
