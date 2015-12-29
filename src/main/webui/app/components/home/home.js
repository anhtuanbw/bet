'use strict';

export default class HomeController {
  /* @ngInject */
  constructor($rootScope, CacheService) { 
  	this.isAuthorized = CacheService.get('loginUser')? true : false;
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