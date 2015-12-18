'use strict';

export default class RegisterController {
  /* @ngInject */
  constructor(RegisterService, $rootScope) {
    this.registerService = RegisterService;
    this.errorMessage = {};
    this.userInfo = {};
    this.userInfo.languageTag = 'en_US';
    this.status = '';
    this.statusClass = '';
    $rootScope.$on('changeLang', (event, language) => this.userInfo.languageTag = language);
  }
  
  registerUser() {
    this.registerService.registerUser(this.userInfo)
      .then(response => {
        this.errorMessage = {};
        this.userInfo = {};
        this.status = 'Register Successfully!!!';
        this.statusClass = 'alert alert-success';       
      }, function(response) {
         this.errorMessage = response.data.fieldErrors;
          this.userInfo = {};
          this.status = '';
      })
      .catch(error => {
        this.status = error.message;
        this.statusClass = 'alert alert-danger';
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
