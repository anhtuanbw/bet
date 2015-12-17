'use strict';

export default class ChangePasswordController {
  constructor(AccountService, CacheService, $location, $rootScope) {
    this.accountService = AccountService;
    this.cacheService = CacheService;
    this.location = $location;
    this.rootScope = $rootScope;
    this.passwordModel = {};
  }

  changePass(passwordModel) {
    this.accountService.changepassword(passwordModel)
      .then(response => {
        if (response.data) {
          location.path('/app/components/home/home.html');
        } else {
          this.passwordModel = {};
        }
      });
  }
}

export default class ChangePassword {
  constructor() {
    return {
      replace: false,
      scope: true,
      controller: ChangePasswordController,
      controllerAs: 'changePassword',
      templateUrl: 'app/components/change-password/change-password.html'
    };
  }
}

