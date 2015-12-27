'use strict';

export default class UnAuthorized {
  /* @ngInject */
  constructor() {
    return {
      replace: true,
      templateUrl: 'app/common/unAuthorized/unAuthorized.html'
    };
  }
}