'use strict';

export default class Login {
  constructor() {
    return {
      replace: true,
      scope: true,
      controller: Login,
      controllerAs: 'Login',
      templateUrl: 'app/components/login/login.html', controller: 'loginCtrl'
    };
  }
}
