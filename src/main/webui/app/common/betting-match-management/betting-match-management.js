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
    this.getTourAndGroupId();
    this.roundIdAndName = [];
    this.roundAndMatch = {};
  }

  getTourAndGroupId(){
    this.rootScope.$on('tourID', (event, tournamentID, groupID) => {
      if (tournamentID) {
        this.tourID = tournamentID;
        this.groupID = groupID;
        this.showMatch(this.data);
        this.data.hide = false;
      }
    });
  }

  showMatch(){
    this.data.match = [];
    this.roundIdAndName = [];
    this.RoundService.getRoundInTournament(this.tourID)
    .then(response => {
      this.roundAndMatch = response.data;
      for (var i = 0; i < response.data.length; i++) {
        var roundInfo = {
          'id': response.data[i].id,
          'name': response.data[i].name
        };
        this.roundIdAndName.push(roundInfo);
        this.data.match.push(response.data[i]);
      }
      this.showBettingMatch(this.data);
    });
  }

  showBettingMatch(){
    this.data.bettingMatch = [];
    for (var i = 0; i < this.roundIdAndName.length; i++) {
      this.BettingService.getBettingMatchByRoundAndGroupId(this.roundIdAndName[i].id, this.groupID)
        .then(response => {
          //remove null item
          var tempArray = [];
          for (var j = 0; j < response.data.length; j++) {
            if (response.data[j] !== null) {
              tempArray.push(response.data[j]);
            }
          }
          //get round name
          var roundName;
          if (tempArray.length > 0) {
            for (var k = 0; k < this.roundAndMatch.length; k++) {
              for (var l = 0; l < this.roundAndMatch[k].matches.length; l++) {
                if( this.roundAndMatch[k].matches[l].id === tempArray[0].match.id ){
                  roundName = this.roundAndMatch[k].name;
                }
              }
            }
          }
          //make one item in betting Match
          var item = {
            'round': roundName,
            'bettingMatch': tempArray
          };
          // push into betting Match list
          this.data.bettingMatch.push(item);
          //reset data
          response.data = [];
      });
    }
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
    var dateTime = month+'/'+dates+'/'+year+', '+hour+':'+minute+':'+second;
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

  betMatch(round, match){
    var dataSend = {
      'roundName': round.round,
      'bettingMatchId': match.id,
      'competitor1Name': match.match.competitor1.name,
      'competitor2Name': match.match.competitor2.name,
      'competitor1Id': match.match.competitor1.id,
      'competitor2Id': match.match.competitor2.id,
      'score1': match.match.score1,
      'score2': match.match.score2,
      'time': match.match.matchTime
    };
    this.rootScope.$broadcast('playerBettingMatch', dataSend);
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
