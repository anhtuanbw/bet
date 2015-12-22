'use strict';

export default class RegisterController {
  /* @ngInject */
  constructor(AccountService, $rootScope, toaster) {
    this.accountService = AccountService;
    this.errorMessage = {};
    this.userInfo = {};
    this.toaster = toaster;
    this.userInfo.languageTag = 'en_US';
    $rootScope.$on('changeLang', (event, language) => this.userInfo.languageTag = language);
  }
  
  registerUser() {
    this.accountService.register(this.userInfo)
    .then(() => {
      this.errorMessage = {};
      this.userInfo = {};
      this.toaster.pop('success', null, "app/components/register/success.html", null, 'template');
    })
    .catch(error => {
      if (error.status === 400) {
        this.errorMessage = error.data.fieldErrors;
      }
    });
  }
}

