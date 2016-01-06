'use strict';

export default class BettingMatchController {
  /* @ngInject */
  constructor(RoundService, BettingService, $rootScope, $modal){
    this.rootScope = $rootScope;
    this.RoundService = RoundService;
    this.BettingService = BettingService;
    this.tourID = 0;
    this.groupID = 0;
    this.data = {};
    this.modal = $modal;
    this.loadRound();
  }

  loadRound(){
    this.rootScope.$on('tourID', (event, tournamentID, groupID) => {
      if (tournamentID) {
        this.tourID = tournamentID;
        this.groupID = groupID;
        this.selectGroup(this.data);
        this.data.hide = false;
      }
    });
  }

  selectGroup(){
    this.data.roundName = [];
    this.data.match = [];
    this.data.matchList = [];
    this.RoundService.getRoundInTournament(this.tourID)
    .then(response => {
      for (var i = 0; i < response.data.length; i++) {
        this.data.roundName.push(response.data[i].name);
        this.data.match.push(response.data[i]);
        this.BettingService.getBettingMatchByRoundAndGroupId(response.data[i].id, this.groupID)
          .then(response => {
            var tempArray = [];
            for (var j = 0; j < response.data.length; j++) {
              if (response.data[j] !== null) {
                tempArray.push(response.data[j]);
              }
            }
            this.data.matchList.push(tempArray);
            response.data = [];
        });
      }
    });
  }


  add(){
    this.data.hide = true;
  }

  parseTime(date){
    var fullDate = new Date(...date);
    var year = fullDate.getFullYear();
    var month = this.longTime(fullDate.getMonth());
    var dates = this.longTime(fullDate.getDate());
    var hour = this.longTime(fullDate.getHours());
    var minute = this.longTime(fullDate.getMinutes());
    var second = this.longTime(fullDate.getSeconds());
    var dateTime = year+'/'+month+'/'+dates+', '+hour+':'+minute+':'+second;
    return dateTime;
  }

  longTime(time){
    if(time < 10){
      return '0'+time;
    } else {
      return time;
    }
  }

  chooseMatch(matchChoosedData){
    matchChoosedData.groupID = this.groupID;
    this.modal.open({
      templateUrl: 'app/common/create-betting-match/create-betting-match.html',
      controller: 'CreateBettingController',
      controllerAs: 'createBet',
      resolve: {
        matchInfo: function () {
          return matchChoosedData;
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

  update(){
   
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
