'use strict';

export default class LoginController {
  /* @ngInject */
  constructor(AccountService, $location, $rootScope, $modal) {
    this.accountService = AccountService;
    this.rootScope = $rootScope;
    this.location = $location;
    this.loginModel = {};
    this.modal = $modal;
  }

  signIn(loginModel) {
    var vm = this;
    this.accountService.login(loginModel.username, loginModel.password)
    .then(response => {
      // Success
      if (response.data) {
        vm.location.path('/home');
      }
    });
  }

  openForgotPassword() {
    this.modal.open({
      templateUrl: 'app/common/reset-password/reset-password.html',
      controller: 'ResetPasswordController',
      controllerAs: 'resetPassword'
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