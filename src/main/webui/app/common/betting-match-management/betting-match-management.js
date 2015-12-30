'use strict';

export default class BettingMatchController {
  /* @ngInject */
  constructor(RoundService, $rootScope){
    this.rootScope = $rootScope;
    this.RoundService = RoundService;
    this.tourID = 0;
    this.roundData = {};
  }

  loadRound(data){
    this.rootScope.$on('tourID', (event, tournamentID) => {
      if (tournamentID) {
        this.tourID = tournamentID;
        this.selectGroup(data);
        data.hide = false;
      }
    });
  }

  selectGroup(data){
    data.roundList = [];
    data.round = [];
    this.RoundService.getRoundInTournament(this.tourID)
    .then(response => {
      this.roundData = response.data;
      for (var i = 0; i < response.data.length; i++) {
        data.roundList.push(response.data[i].name);
        data.round.push(response.data[i]);
      }
    });
  }

  add(data){
    data.hide = true;
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
