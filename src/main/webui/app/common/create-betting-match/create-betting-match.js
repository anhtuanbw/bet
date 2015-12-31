'use strict';

export default class CreateBettingController {
  /* @ngInject */
  constructor() {

  }

  create(){
    console.log('click create !');
  }

  cancel(){
    console.log('click cancel');
  }
}

export default class createBet {
  constructor() {
    return {
      controller: CreateBettingController,
      controllerAs: 'createBet',
      templateUrl: 'app/common/create-betting-match/create-betting-match.html'
    };
  }
}
