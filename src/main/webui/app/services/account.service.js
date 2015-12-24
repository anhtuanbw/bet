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
    var token = this.cacheService.get('loginUser');
    return this.$http({
      method: 'POST',
      url: 'api/logout',
      headers: {'Accept': '*/*', 'x-auth-token': token}
    });
  }
  
  changepassword(passwordModel) {
    return this.$http.post('api/account/change-password', passwordModel);
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

  users() {
    return this.$http({
      method: 'GET',
      url: 'api/users',
      headers: {'Accept': '*/*'}
    });
  }
}