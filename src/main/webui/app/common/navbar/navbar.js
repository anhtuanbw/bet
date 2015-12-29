'use strict';

export default class NavBar {
  /* @ngInject */
  constructor() {
    return {
      replace: true,
      templateUrl: 'app/common/navbar/navbar.html'
    };
  }
}