'use strict';

export default class BettingMatchController {
  /* @ngInject */
  constructor(RoundService, $rootScope, $modal){
    this.rootScope = $rootScope;
    this.RoundService = RoundService;
    this.tourID = 0;
    this.groupID = 0;
    this.roundData = {};
    this.modal = $modal;
  }

  loadRound(data){
    this.rootScope.$on('tourID', (event, tournamentID, groupID) => {
      if (tournamentID) {
        this.tourID = tournamentID;
        this.groupID = groupID;
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

  parseTime(date){
    return String(new Date(...date));
  }

  chooseMatch(){
    this.modal.open({
      templateUrl: 'app/common/create-betting-match/create-betting-match.html',
      controller: 'CreateBettingController',
      controllerAs: 'createBet'
    });
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
