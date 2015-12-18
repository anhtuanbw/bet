'use strict';

export default class ChangePasswordController {
  constructor(AccountService, CacheService, $location, $rootScope) {
    this.accountService = AccountService;
    this.cacheService = CacheService;
    this.location = $location;
    this.rootScope = $rootScope;
    this.passwordModel = {};
    this.errorMessage = {};
    this.success = "";
  }

  changePass() {
    this.accountService.changepassword(this.passwordModel)
      .then(response => {
        if (response.data) {
          // get error message from server for 3 field password
          this.errorMessage = response.data.fieldErrors;
        } else {
          //change password success
          this.success = "Your password has been changed.";
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
      templateUrl: 'app/common/change-password/change-password.html'
    };
  }
}

