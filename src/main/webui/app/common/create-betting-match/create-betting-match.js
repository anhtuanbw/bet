'use strict';

export default class CreateBettingController {
  /* @ngInject */
  constructor($rootScope, $modalInstance, matchInfo, BettingService, toaster) {
    this.rootScope = $rootScope;
    this.modalInstance = $modalInstance;
    this.data = {};
    this.matchData = matchInfo;
    this.BettingService = BettingService;
    this.toaster = toaster;
    this.loadData();
    this.validateBal1 = {};
    this.validateBal2 = {};
    this.validateAmount = {};
    this.validateBal1.show = false;
    this.validateBal2.show = false;
    this.validateAmount.show = false;
    this.data.hideCreate = false;
    this.data.hideUpdate = false;
  }

  loadData(){
    if (this.matchData.expiredTime) {
      this.matchData.expiredTime = this.parseTime(this.matchData.expiredTime);
      this.data.oldTime = this.matchData.expiredTime;
    }
    this.data.competitor1 = this.matchData.competitor1.name;
    this.data.competitor2 = this.matchData.competitor2.name;
    this.data.balance1 = this.matchData.balance1;
    this.data.balance2 = this.matchData.balance2;
    this.data.amount = this.matchData.betAmount;
    this.data.description = this.matchData.description;
    this.data.active = this.matchData.activated;
    this.data.hide = this.matchData.hide;
  }

  create(data){
    var titleToaster = 'Create Betting Match';
    var templateUrl = 'app/common/create-betting-match/createSuccess.html';
    var timeFormated;
    if (data.time) {
      timeFormated = this.serverTimeFormat(data.time);
    }
    if (!data.active) {
      data.active = false;
    }
    var betData = {
        'activated': data.active,
        'balance1': data.balance1,
        'balance2': data.balance2,
        'betAmount': data.amount,
        'decription': data.description,
        'expiredTime': timeFormated,
        'groupId': this.matchData.groupID,
        'matchId': this.matchData.id
        };
    this.BettingService.create(betData)
    .then(response => {
      if (response.status === 200) {
          this.toaster.pop('success', titleToaster, templateUrl, null, 'template');
      }
      this.modalInstance.dismiss();
    }, function (response) {
      this.toaster.pop('error', titleToaster, response.data.message);
      data.errorBal1 = response.data.fieldErrors.balance1;
      data.errorBal2 = response.data.fieldErrors.balance2;
      data.errorBetAmount = response.data.fieldErrors.betAmount;
      data.errorTime = response.data.fieldErrors.expiredTime;
    });
  }

  update(data){
    var titleToaster = 'Update Betting Match';
    var templateUrl = 'app/common/create-betting-match/updateSuccess.html';
    var timeFormated;
    if(data.time === ''){
      data.time = data.oldTime;
      timeFormated = this.serverTimeFormat(data.time);
    } else {
      timeFormated = this.serverTimeFormat(data.time);
    }
    
    var dataUpdate = {
      'activated': data.active,
      'balance1': data.balance1,
      'balance2': data.balance2,
      'betAmount': data.amount,
      'bettingMatchId': this.matchData.bettingMatchId,
      'decription': data.description,
      'expiredTime': timeFormated,
      'groupId': this.matchData.groupId,
      'matchId': this.matchData.matchId
    };
    this.BettingService.update(dataUpdate)
    .then(response => {
      if (response.status === 200) {
          this.toaster.pop('success', titleToaster, templateUrl, null, 'template');
      }
      this.modalInstance.dismiss();
      //update balance
      var balance = '('+data.balance1+' - '+data.balance2+')';
      document.getElementById('betScore'+this.matchData.bettingMatchId).innerHTML = balance;
    }, function (response) {
      this.toaster.pop('error', titleToaster, response.data.message);
      data.errorBal1 = response.data.fieldErrors.balance1;
      data.errorBal2 = response.data.fieldErrors.balance2;
      data.errorBetAmount = response.data.fieldErrors.betAmount;
      data.errorTime = response.data.fieldErrors.expiredTime;
    });
  }

  cancel(){
    this.modalInstance.dismiss();
  }

  closeModal(){
    this.modalInstance.dismiss();
  }

  parseTime(date){
    var year = date[0];
    var month = this.longTime(date[1]);
    var dates = this.longTime(date[2]);
    var hour = this.longTime(date[3]);
    var minute = this.longTime(date[4]);
    var dateTime = month+'/'+dates+'/'+year+' '+hour+':'+minute;
    return dateTime;
  }

  longTime(time){
    if(time < 10){
      return '0'+time;
    } else {
      return time;
    }
  }

  serverTimeFormat(time){
    var timeFormated;
    var dateAndTime;
    var monthDayYear;
    var hourMin;
    dateAndTime = time.split(' ');
    monthDayYear = dateAndTime[0].split('/');
    hourMin = dateAndTime[1].split(':');
    timeFormated = monthDayYear[2]+'-'+monthDayYear[0]+'-'+monthDayYear[1]+'T'+hourMin[0]+':'+hourMin[1]+':00.000';
    return timeFormated;
  }

  validate(data, message){
    if (typeof data === 'number' && (data%0.25) == 0) {
      message.show = false;
      this.data.hideCreate = false;
      this.data.hideUpdate = false;
    } else {
      message.show = true;
      this.data.hideCreate = true;
      this.data.hideUpdate = true;
    }
  }

}

