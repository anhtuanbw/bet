'use strict';
/* global angular */

export default class RegisterController {
  /* @ngInject */
  constructor(RegisterService, CacheService) {
    this.registerService = RegisterService;
    this.errorMessage = {};
    this.username='';
    this.name='';
    this.email='';
    this.password='';
    this.confirmPassword='';
  }
  
  registerUser() {
    var userInfo = {
      username: this.username,
      name: this.name,
      email: this.email,
      password: this.password,
      confirmPassword: this.confirmPassword,
      languageTag: ''
    };
    console.log(userInfo);
    this.registerService.registerUser(userInfo)
    .then(response => {
      if (response.data) {
        this.errorMessage = response.data.fieldErrors;
      } else {
        this.errorMessage = {};
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
      templateUrl: 'app/components/register/register.html'
    };
  }
}
