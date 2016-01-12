'use strict';

export default class ChangePasswordController {
  /* @ngInject */
  constructor(AccountService, CacheService, $location, $modalInstance, toaster) {
    this.accountService = AccountService;
    this.cacheService = CacheService;
    this.location = $location;
    this.data = {};
    this.errorMessage = {};
    this.modalInstance = $modalInstance;
    this.toaster = toaster;
  }

  changePass() {
    var self = this;

    this.accountService.changePassword(this.data)
      .then(response => {

        // Success
        self.closeModal();
        this.toaster.pop('success', null, 'app/common/change-password/success.html', null, 'template');
        self.data = {};
        const token = response.data.token;
        if (token) {

          // reset new token
          self.cacheService.set('loginUser', token);
        }
      })
      .catch(response => {

        // return error
        if (response.data.message) {
          self.pop('error', self.popTitle, response.data.message);
        }
        self.errorMessage = response.data.fieldErrors;
      });
  }

  closeModal() {
    this.modalInstance.dismiss();
    this.data = {};
  }
}