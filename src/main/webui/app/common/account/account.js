'use strict';
/* global angular */

export default class AccountController {
  /* @ngInject */
  constructor(AccountService, CacheService, $location, $rootScope) {
    this.accountService = AccountService;
    this.cacheService = CacheService;
    this.location = $location;
    this.rootScope = $rootScope;
    this.loginUser = {};
    this.authen();
    $rootScope.$on('login', (event, cb) => this.login(cb));
  }

  authen() {
    this.accountService.authen()
    .then(response => {
      if (response.data) {
        this.cacheService.set('loginUser', angular.toJson(response.data));
        this.loginUser = response.data;
      } else {
        this.cacheService.remove('loginUser');
        this.loginUser = {};
      }
    });
  }

  login(cb) {
    this.accountService.login()
    .then(() => {
      this.authen();
      this.rootScope.$broadcast('updateCart');
      if (cb && typeof cb === 'function') { cb(); }
    });
  }

  logout() {
    this.accountService.logout()
    .then(() => {
      this.cacheService.remove('loginUser');
      this.loginUser = {};
      this.cacheService.remove('cartId');
      this.rootScope.$broadcast('updateCart');
      this.location.path('/');
    });
  }
}

export default class Account {
  /* @ngInject */
  constructor() {
    return {
      replace: true,
      scope: true,
      controller: AccountController,
      controllerAs: 'account',
      templateUrl: 'app/common/account/account.html'
    };
  }
}
