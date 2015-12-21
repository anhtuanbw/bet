'use strict';

export default class RegisterController {
  /* @ngInject */
  constructor(RegisterService, $rootScope) {
    this.registerService = RegisterService;
    this.errorMessage = {};
    this.userInfo = {};
    this.userInfo.languageTag = 'en_US';
    this.success = '';
    $rootScope.$on('changeLang', (event, language) => this.userInfo.languageTag = language);
  }
  
  registerUser() {
    this.registerService.registerUser(this.userInfo)
      .then(response => {
        if (response.data) {
          this.errorMessage = response.data.fieldErrors;
          this.userInfo = {};
          this.success = '';
        } else {
          this.errorMessage = {};
          this.userInfo = {};
          this.success = 'Register Successfully!!!';
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
