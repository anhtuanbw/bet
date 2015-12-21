'use strict';

export default class ChangePasswordController {
  constructor(AccountService, CacheService, $location, $rootScope) {
    this.accountService = AccountService;
    this.cacheService = CacheService;
    this.location = $location;
    this.rootScope = $rootScope;
    this.passwordModel = {};
    this.errorMessage = {};
    this.status = '';
  }

  changePass() {
    var self = this;
    this.accountService.changepassword(this.passwordModel)
      .then(response => {
        this.status = 'Your password have been saved!';
        this.passwordModel = {};
        this.location.path('/home');
      }, function (response) {
        self.errorMessage = response.data.fieldErrors;
        self.status = '';
      });
  }
}
