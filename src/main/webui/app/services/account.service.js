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
    var loginData = {
      'username': userpass.username,
      'password': userpass.password
    };
    return this.$http.post('api/login', loginData);
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
      headers: {'Accept': '*/*', 'x-auth-token': token},
      data: data
    });
  }

  resetPassword(email) {
    return this.$http.post('api/reset-password/init', email);
  }

  resetPasswordFinish(key, resetPasswordInfo) {
    return this.$http.post('api/reset-password/finish?key='+key, resetPasswordInfo);
  }
  
  
  register(userInfo) {
	  return this.$http.post('api/register', userInfo);
  }
  
  activate(key) {
	  return this.$http.get('api/activate?key=' + key);
  }
}