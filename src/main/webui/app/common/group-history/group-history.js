'use strict';

export default class GroupHistoryController {
  /* @ngInject */
  constructor() {
    this.data =  [
          { player: 'User abc', notloss: '0', losscount: 3, lossamount: 3, notbetcount: 0, notbetamount: 0, totalloss: 3},
          { player: 'User temp', notloss: '3', losscount: 3, lossamount: 2, notbetcount: 1, notbetamount: 4, totalloss: 0},
          { player: 'User test', notloss: '2', losscount: 5, lossamount: 1, notbetcount: 2, notbetamount: 5, totalloss: 2},
          { player: 'User abc', notloss: '0', losscount: 5, lossamount: 4, notbetcount: 3, notbetamount: 2, totalloss: 0},
          { player: 'User temp', notloss: '1', losscount: 1, lossamount: 3, notbetcount: 5, notbetamount: 1, totalloss: 2}
        ];
    this.sortType = 'player';
    this.sortReverse = false;
    this.activePlayer = false;
  }
}

export default class GroupHistory {
  constructor() {
    return {
      replace: false,
      scope: true,
      controller: GroupHistoryController,
      controllerAs: 'groupHistory',
      templateUrl: 'app/common/group-history/group-history.html'
    };
  }
}
