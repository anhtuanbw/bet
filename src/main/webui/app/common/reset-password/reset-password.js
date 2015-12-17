'use strict';

export default class ResetPasswordController {
  /* @ngInject */
  constructor(AccountService, $location, $rootScope) {
    this.accountService = AccountService;
    this.location = $location;
    this.rootScope = $rootScope;
    this.emailError = 'Email is not valid';
  }

  reset(email) {
    this.accountService.resetPassword(email)
    .then(response => {
      if (response.data) {
        this.emailError = response.data.message;
      } else {
      }
    });
  }
}

export default class ResetPassword {
  constructor() {
    return {
      replace: false,
      scope: true,
      controller: ResetPasswordController,
      controllerAs: 'resetPassword',
      templateUrl: 'app/common/reset-password/reset-password.html',
    };
  }
}