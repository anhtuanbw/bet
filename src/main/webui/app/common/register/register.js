'use strict';

export default class RegisterController {
  /* @ngInject */
  constructor(RegisterService, $rootScope) {
    this.registerService = RegisterService;
    this.errorMessage = {};
    this.userInfo = {};
    this.userInfo.languageTag = 'en_US';
    this.success = false;
    $rootScope.$on('changeLang', (event, language) => this.userInfo.languageTag = language);
  }

  registerUser() {
    var self = this;
    this.registerService.registerUser(this.userInfo)
      .then(() => {
        this.errorMessage = {};
        this.userInfo = {};
        this.success = true;
      },
    error => {
      self.errorMessage = error.data.fieldErrors;
      self.success = false;
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
