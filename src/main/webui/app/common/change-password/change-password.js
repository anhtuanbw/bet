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
    self.popTitle = 'Change password';

    // Show alert message
    self.pop = function (type, title, content) {
      this.toaster.pop(type, title, content);
    };

    this.accountService.changePassword(this.data)
      .then(response => {
        
        // Success
        self.closeModal();
        self.pop('success', self.popTitle, 'Your password have been changed!');
        self.data = {};
        self.location.path('/home');
        const token = response.data.token;
        if (token) {
          
          // reset new token
          self.cacheService.set('loginUser', token);
          self.location.path('/home');
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