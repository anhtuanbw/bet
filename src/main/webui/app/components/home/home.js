'use strict';

export default class HomeController {
  constructor($rootScope, CacheService) { 
  	this.isAuthorized = CacheService.get('loginUser')!=null;
  	$rootScope.$on('login', (event, cb) => this.login(cb));
  	$rootScope.$on('logout', (event, cb) => this.logout(cb));
  }

  login() {
  	this.isAuthorized = true;
  }

  logout() {
  	this.isAuthorized = false;
  }
}