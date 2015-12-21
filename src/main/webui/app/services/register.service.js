'use strict';

export default class RegisterService {
  /* @ngInject */
  constructor($http) {
    this.$http = $http;
  }
  
  registerUser (userInfo) {
	  return this.$http.post('api/register', userInfo);
  }
}