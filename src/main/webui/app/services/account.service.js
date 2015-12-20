'use strict';

export default class AccountService {
  /* @ngInject */
  constructor($http, CacheService) {
    this.$http = $http;
    this.cacheService = CacheService;
  }

  authen() {
    var token = this.cacheService.get('loginUser');
    return this.$http({
      method: 'GET',
      url: 'api/authenticate',
      headers: {'Accept': '*/*', 'x-auth-token': token}
    });
  }


  login(userpass) {
    return this.$http.post(`api/login?username=${userpass.username}&password=${userpass.password}`);
  }

  logout() {
    return this.$http.post('api/logout');
  }
  
  changepassword(passwordModel) {
    return this.$http.post('api/account/change-password', passwordModel);
  }

  resetPassword(email) {
    return this.$http.post('api/reset-password/init', email);
  }
  
}