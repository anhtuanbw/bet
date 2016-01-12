'use strict';

export default class BettingMatchController {
  /* @ngInject */
  constructor(RoundService, BettingService, AccountService, toaster, GroupService, $rootScope, $modal){
    this.rootScope = $rootScope;
    this.RoundService = RoundService;
    this.BettingService = BettingService;
    this.accountService = AccountService;
    this.groupService = GroupService;
    this.tourID = 0;
    this.groupID = 0;
    this.data = {};
    this.modal = $modal;
    this.authen();
    this.getTourAndGroupId();
    this.roundIdAndName = [];
    this.roundAndMatch = {};
    this.toaster = toaster;
    this.isAdmin = false;
    this.isMod = false;
    this.checkAdmin();
  }

  getTourAndGroupId(){
    this.rootScope.$on('tourID', (event, tournamentID, groupID) => {
      if (tournamentID) {
        this.tourID = tournamentID;
        this.groupID = groupID;
        this.showMatch();
        this.authen();
        this.data.hide = false;
        this.isMember = false;
        this.data.showBtnAdd = false;
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
        if(response.data[i].matches.length !== 0) {
          this.data.match.push(response.data[i]);
          this.data.showBtnAdd = true;
        }
      }
      this.showBettingMatch(this.data);
    });
  }

  showBettingMatch(){
    this.checkMember();
    this.checkMod();
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
          if (item.bettingMatch.length !== 0){
            this.data.bettingMatch.push(item);
          }
          //reset data
          response.data = [];
      });
    }
  }

  add(){
    this.data.hide = true;
    this.removeMatchBetted();
  }

  goBack(){
    this.showBettingMatch();
    this.data.hide = false;
  }

  parseTime(date){
    var year = date[0];
    var month = this.longTime(date[1]);
    var dates = this.longTime(date[2]);
    var hour = this.longTime(date[3]);
    var minute = this.longTime(date[4]);
    var dateTime = month+'/'+dates+'/'+year+', '+hour+':'+minute;
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
    matchChoosedData.balance1 = 0;
    matchChoosedData.balance2 = 0;
    matchChoosedData.betAmount = 0;
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

  authen() {
    this.accountService.authen()
    .then(response => {
      if (response.data) {
          this.currentUser = response.data;
      }
    });
  }
  
  checkMod() {
    var data = {};
    data.groupId = this.groupID;
    data.userId = this.currentUser.id;
    this.groupService.isModerator(data)
    .then(() => {
      this.isMod = true;
    })
    .catch(() => {
      this.isMod = false;
    });
  }
  

  openUpdate(match){
    var data = {
      'competitor1': match.match.competitor1,
      'competitor2': match.match.competitor2,
      'activated': match.activated,
      'balance1': match.balance1,
      'balance2': match.balance2,
      'betAmount': match.betAmount,
      'description': match.description,
      'expiredTime': match.expiredTime,
      'groupId': this.groupID,
      'hide': true,
      'matchId': match.match.id,
      'bettingMatchId': match.id
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

  activate(match){
    var self = this;
    self.popTitle = 'Activate Betting Match';
    var successMessage = 'Active Successfully !';
    // Show alert message
    self.pop = function (type, title, content) {
      this.toaster.pop(type, title, content);
    };
    var activeData = {
      'bettingMatchId': match.id,
      'groupId': this.groupID
    };
    this.BettingService.active(activeData)
    .then(() => {
      self.pop('success', self.popTitle, successMessage);
      match.activated = true;
    }, function (response) {
      self.pop('error', self.popTitle, response.data.message);
    });
  }

  checkAdmin(){
      this.accountService.authen()
      .then(response => {
        if (response.data.role === 'ADMIN') {
            this.isAdmin = true;
          }
      });
  }

  checkMember(){
    this.groupService.findById(this.groupID)
    .then(response => {
      for (var i = 0; i < response.data.members.length; i++) {
        if (response.data.members[i].id === this.currentUser.id) {
          this.isMember = true;
        }
      }
    });
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

  removeMatchBetted(){

    //use for(...) to make a list betting match id
    var bettingMatchId = [];
    for (var i = 0; i < this.data.bettingMatch.length; i++) {
      for (var j = 0; j < this.data.bettingMatch[i].bettingMatch.length; j++) {
        bettingMatchId.push(this.data.bettingMatch[i].bettingMatch[j].match.id);
      }
    }
    //use for(...) to remove match have the same match id in bettingMatch
    for (var k = 0; k < this.data.match.length; k++) {
      for (var l = 0; l < this.data.match[k].matches.length; l++) {
        var existId = bettingMatchId.indexOf(this.data.match[k].matches[l].id);
        if (existId !== -1) {
          this.data.match[k].matches[l].hide = true;
        }
      }
    }
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
