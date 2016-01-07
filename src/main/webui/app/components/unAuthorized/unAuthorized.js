'use strict';

export default class UnAuthorizedController {
  /* @ngInject */
  constructor($timeout, $location) {
    $timeout(function () {
      $location.path('/home');
    }, 3000);
  }
}