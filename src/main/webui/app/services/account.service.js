'use strict';

export default class AccountService {
  /* @ngInject */
  constructor($http) {
    this.$http = $http;
  }

  authen() {
    return this.$http.get('api/auth/user');
  }

  login() {
    return this.$http.get('api/login');
  }

  logout() {
    return this.$http.post('api/logout');
  }

  resetPassword(email) {
    return this.$http.post('api/reset-password/init', {'email': email});
  }
}