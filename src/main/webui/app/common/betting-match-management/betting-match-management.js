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

  chooseMatch(data){
    data.groupID = this.groupID;
    this.modal.open({
      templateUrl: 'app/common/create-betting-match/create-betting-match.html',
      controller: 'CreateBettingController',
      controllerAs: 'createBet',
      resolve: {
        matchInfo: function () {
          return data;
        }
      }
    });
  }

  openUpdate(){
    var data = {
      'competitor1': {name: 'teamA'},
      'competitor2': {name: 'teamB'},
      'activated': true,
      'balance1': 3,
      'balance2': 1,
      'betAmount': 200,
      'decription': 'update emulator',
      'expiredTime': '2015-12-15 10:35',
      'groupId': 3,
      'hide': true,
      'matchId': 5
    };
    this.modal.open({
      templateUrl: 'app/common/create-betting-match/create-betting-match.html',
      controller: 'CreateBettingController',
      controllerAs: 'createBet',
      resolve: {
        matchInfo: function () {
          return data;
        }
      }
    });
  }

  activate(){

  }

  update(data){
    console.log('data: '+data.time);
  }

  betMatch(){

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
