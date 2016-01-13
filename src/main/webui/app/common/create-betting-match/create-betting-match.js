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
    this.data.errorBal1 = {};
    this.data.errorBal2 = {};
    this.data.errorBetAmount = {};
    this.data.errorBal1.message = '';
    this.data.errorBal2.message = '';
    this.data.errorBetAmount.message = '';
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
    var self = this;
    self.popTitle = 'Create Betting Match';
    var successMessage = 'Betting Successfully !';
    // Show alert message
    self.pop = function (type, title, content) {
      this.toaster.pop(type, title, content);
    };
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
    .then(() => {
      self.pop('success', self.popTitle, successMessage);
      this.modalInstance.dismiss();
    }, function (response) {
      self.pop('error', self.popTitle, response.data.message);
      data.errorBal1 = response.data.fieldErrors.balance1;
      data.errorBal2 = response.data.fieldErrors.balance2;
      data.errorBetAmount = response.data.fieldErrors.betAmount;
      data.errorTime = response.data.fieldErrors.expiredTime;
    });
  }

  update(data){
    var self = this;
    self.popTitle = 'Update Betting Match';
    var successMessage = 'Update Successfully !';
    // Show alert message
    self.pop = function (type, title, content) {
      this.toaster.pop(type, title, content);
    };
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
    .then(() => {
      self.pop('success', self.popTitle, successMessage);
      this.modalInstance.dismiss();
      //update balance
      var balance = '('+data.balance1+' - '+data.balance2+')';
      document.getElementById('betScore'+this.matchData.bettingMatchId).innerHTML = balance;
    }, function (response) {
      self.pop('error', self.popTitle, response.data.message);
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

  validate(data, errorMsg){
    if (typeof data === 'number') {
      errorMsg.message = '';
      this.data.hideCreate = false;
      this.data.hideUpdate = false;
    } else {
      errorMsg.message = 'invalid number';
      this.data.hideCreate = true;
      this.data.hideUpdate = true;
    }
  }

}

