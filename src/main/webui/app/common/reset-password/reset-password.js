'use strict';

export default class ResetPasswordController {
  /* @ngInject */
  constructor(AccountService, $rootScope) {
    this.accountService = AccountService;
    this.rootScope = $rootScope;
    this.errorMessage = '';
  }

  reset(email) {
    var vm = this;
    this.accountService.resetPassword(email)
    .then(response => {
      // Success
      if (response.data) {
      }
    }, function(response) {
      // Failed
      vm.errorMessage = response.data.message;
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