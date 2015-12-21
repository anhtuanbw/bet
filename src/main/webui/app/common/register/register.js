'use strict';

export default class RegisterController {
  /* @ngInject */
  constructor(AccountService, $rootScope) {
    this.accountService = AccountService;
    this.errorMessage = {};
    this.userInfo = {};
    this.userInfo.languageTag = 'en_US';
    this.success = false;
    $rootScope.$on('changeLang', (event, language) => this.userInfo.languageTag = language);
  }
  
  registerUser() {
    var self = this;
    if (!this.userInfo.email)
      this.userInfo.email = '';
    this.accountService.register(this.userInfo)
    .then(() => {
      this.errorMessage = {};
      this.userInfo = {};
      this.success = true;
    })
    .catch(error => {
      if (error.status === 400) {
        self.errorMessage = error.data.fieldErrors;
        self.success = false;
      }
    });
  }
}

export default class Register {
  constructor() {
    return {
      replace: false,
      controller: RegisterController,
      controllerAs: 'register',
      templateUrl: 'app/common/register/register.html'
    };
  }
}
