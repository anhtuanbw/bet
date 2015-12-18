'use strict';
/* global angular */ 

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
    var self = this;
    this.registerService.registerUser(this.userInfo)
      .then(response => {
        if (!response.data) {
        this.errorMessage = {};
        this.userInfo = {};
        this.status = 'Register Successfully!!!';
        this.statusClass = 'alert alert-success';   }    
      }, function(response) {
         self.errorMessage = response.data.fieldErrors;
         self.userInfo = {};
         self.status = '';
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
