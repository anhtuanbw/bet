'use strict';

export default class LoginController {
  /* @ngInject */
  constructor(AccountService, $location, $rootScope) {
    this.accountService = AccountService;
    this.rootScope = $rootScope;
    this.location = $location;
    this.loginModel = {};
  }

  signIn(loginModel) {
    var vm = this;
    this.accountService.login(loginModel.username, loginModel.password)
    .then(response => {
      // Success
      if (response.data) {
        vm.location.path('/home');
      }
    }, function(response) {
      // Failed
    });
  }
}

export default class Login {
  constructor() {
    return {
      replace: false,
      scope: true,
      controller: LoginController,
      controllerAs: 'login',
      templateUrl: 'app/common/login/login.html'
    };
  }
}