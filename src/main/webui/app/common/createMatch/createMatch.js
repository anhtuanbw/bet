'use strict';

export default class CreateMatchController {
  /* @ngInject */
  constructor(AccountService, CacheService, $location, $modalInstance, toaster) {
    this.accountService = AccountService;
    this.cacheService = CacheService;
    this.location = $location;
    this.modalInstance = $modalInstance;
    this.toaster = toaster;
  }

  closeModal() {
    this.modalInstance.dismiss();
    this.data = {};
  }
}