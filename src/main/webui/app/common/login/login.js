'use strict';

export default class LoginController {
  /* @ngInject */
  constructor(AccountService, CacheService, $location, $rootScope, $modal) {
    this.accountService = AccountService;
    this.cacheService = CacheService;
    this.rootScope = $rootScope;
    this.location = $location;
    this.data = {};
    this.modal = $modal;
  }

  login(data) {
    var self = this;
    this.accountService.login(data)
    .then(response => {
      const token = response.data.token;
      if (token) {
        self.cacheService.set('loginUser', token);
        self.rootScope.$broadcast('login', data);
        self.location.path('/management');
      }
    }, function (response) {
      data.error = response.data.message;
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
