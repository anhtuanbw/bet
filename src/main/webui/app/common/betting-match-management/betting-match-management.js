'use strict';

export default class BettingMatchController {
  /* @ngInject */
  constructor(RoundService, BettingService, AccountService, toaster, GroupService, $rootScope, $modal, $stateParams, $location){
    this.rootScope = $rootScope;
    this.RoundService = RoundService;
    this.BettingService = BettingService;
    this.accountService = AccountService;
    this.groupService = GroupService;
    this.location = $location;
    this.tourID = 0;
    this.groupID = 0;
    this.data = {};
    this.modal = $modal;
    this.params = $stateParams;
    this.getTourAndGroupId();
    this.checkMod();
    this.roundIdAndName = [];
    this.roundAndMatch = {};
    this.toaster = toaster;
    this.isAdmin = false;
    this.isMod = false;
    this.checkAdmin();
  }

  getTourAndGroupId(){
    this.tourID = this.params.tournamentId;
    this.groupID = this.params.groupId;
    this.showMatch();
    this.data.hide = false;
    this.isMember = false;
    this.showBtnAdd = false;
	  this.getRoundIdAndName();
  }

  showMatch(){
    this.data.hide = true;
    this.data.match = [];
    this.BettingService.getMatchNotCreateBettingMatch(this.tourID, this.groupID)
    .then(response => {
      this.data.match = response.data;
      this.showBtnAdd = true;
    });
  }

  getRoundIdAndName(){
    this.roundIdAndName = [];
    this.RoundService.getRoundInTournament(this.tourID)
    .then(response => {
      this.roundAndMatch = response.data;
      for (var i = 0; i < response.data.length; i++) {
        var item = {
          'id': response.data[i].id,
          'name': response.data[i].name
        };
        this.roundIdAndName.push(item);
      }
      this.showRound();
    });
  }

  showRound(){
    this.checkMember();
    this.data.bettingMatch = this.roundIdAndName;
  }

  showBettingMatch(round){
    this.BettingService.getBettingMatchByRoundAndGroupId(round.id, this.groupID)
    .then(response => {
      var tempArray = [];
          for (var j = 0; j < response.data.length; j++) {
            if (response.data[j] !== null) {
              tempArray.push(response.data[j]);
            }
          }
      round.bettingMatch = tempArray;
    });
  }

  goBack(){
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

  checkMod() {
    this.accountService.authen()
    .then(response => {
      if (response.data) {
        this.currentUser = response.data;
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
    var titleToaster = 'Active Betting Match';
    var templateUrl = 'app/common/betting-match-management/activeSuccess.html';
    var activeData = {
      'bettingMatchId': match.id,
      'groupId': this.groupID
    };
    this.BettingService.active(activeData)
    .then(response => {
      if (response.status === 200) {
          this.toaster.pop('success', titleToaster, templateUrl, null, 'template');
      }
      match.activated = true;
    }, function (response) {
      this.toaster.pop('error', titleToaster, response.data.message);
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
    this.location.path('/management/'+ this.params.tournamentId + '/' + this.params.groupId + '/' +match.id);
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
