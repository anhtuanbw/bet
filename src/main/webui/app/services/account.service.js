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
      headers: { 'Accept': '*/*', 'x-auth-token': token }
    });
  }

  login(userpass) {
    return this.$http.post(`api/login?username=${userpass.username}&password=${userpass.password}`);
  }

  logout() {
    var token = this.cacheService.get('loginUser');
    return this.$http({
      method: 'POST',
      url: 'api/logout',
      headers: {'Accept': '*/*', 'x-auth-token': token}
    });
  }

  changePassword(data) {
    var token = this.cacheService.get('loginUser');
    return this.$http({
      method: 'POST',
      url: 'api/account/change-password',
      headers: { 'x-auth-token': token },
      data: data
    });
  }

  resetPassword(email) {
    return this.$http.post('api/reset-password/init', email);
  }

  
  register(userInfo) {
	  return this.$http.post('api/register', userInfo);
  }
}