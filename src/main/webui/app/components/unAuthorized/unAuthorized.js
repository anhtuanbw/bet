'use strict';

export default class UnAuthorizedController {
  /* @ngInject */
  constructor($timeout, $location, $rootScope) {
    $rootScope.$broadcast('authen');
    $timeout(function () {
      $location.path('/home');
    }, 3000);
  }
}