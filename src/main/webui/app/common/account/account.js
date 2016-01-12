'use strict';

export default class AccountController {
  /* @ngInject */
  constructor(AccountService, CacheService, $location, $rootScope, $modal) {
    this.accountService = AccountService;
    this.cacheService = CacheService;
    this.location = $location;
    this.rootScope = $rootScope;
    this.loginUser = {};
    this.modal = $modal;
    this.authen();
    $rootScope.$on('login', (event, cb) => this.login(cb));
    $rootScope.$on('authen', () => this.authen());
  }

  authen() {
    this.accountService.authen()
      .then(response => {
        if (response.data) {
          this.loginUser.username = response.data.name;
        } else {
          this.loginUser = {};
          this.cacheService.remove('loginUser');
          this.rootScope.$broadcast('logout');
          this.loginUser = {};
        }
      });
  }

  login(cb) {
    this.accountService.login(cb)
      .then(() => {
        this.authen();
        if (cb && typeof cb === 'function') {
          cb();
        }
      });
  }

  logout() {
    this.accountService.logout()
      .then(() => {
        this.cacheService.remove('loginUser');
        this.loginUser = {};
        this.cacheService.remove('cartId');
        this.rootScope.$broadcast('logout');
        this.location.path('/');
      })
      .catch(() => {
        this.loginUser = {};
        this.location.path('/');
      });
  }

  openChangePassword() {
    this.modal.open({
      templateUrl: 'app/common/change-password/change-password.html',
      controller: 'ChangePasswordController',
      controllerAs: 'changePassword'
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