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
  }

  loadData(data){
    data.competitor1 = this.matchData.competitor1.name;
    data.competitor2 = this.matchData.competitor2.name;
    data.balance1 = this.matchData.balance1;
    data.balance2 = this.matchData.balance2;
    data.amount = this.matchData.betAmount;
    data.description = this.matchData.decription;
    data.timeOld = this.matchData.expiredTime;
    data.active = this.matchData.activated;
    data.hide = this.matchData.hide;
  }

  create(data){
    var self = this;
    self.popTitle = 'Create Betting Match';
    var successMessage = 'Betting Successfully !';
    // Show alert message
    self.pop = function (type, title, content) {
      this.toaster.pop(type, title, content);
    };
    var expiredTime = data.time;
    if (data.time) {
      expiredTime = expiredTime.replace(/\//g, '-');
      expiredTime = expiredTime.replace(' ', 'T');
      expiredTime+=':00.000';
    }
    var betData = {
        'activated': data.active,
        'balance1': data.balance1,
        'balance2': data.balance2,
        'betAmount': data.amount,
        'decription': data.description,
        'expiredTime': expiredTime,
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
    
  }

  cancel(){
    this.modalInstance.dismiss();
  }

  closeModal(){
    this.modalInstance.dismiss();
  }
}

