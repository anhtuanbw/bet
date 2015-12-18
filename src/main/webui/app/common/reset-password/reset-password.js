'use strict';

export default class ResetPasswordController {
  /* @ngInject */
  constructor(AccountService, $rootScope, $modalInstance) {
    this.accountService = AccountService;
    this.rootScope = $rootScope;
    this.errorMessage = '';
    this.modalInstance = $modalInstance;
  }

  reset(email) {
    var vm = this;
    this.accountService.resetPassword(email)
    .then(response => {
      // Success
      if (response.data) {
        vm.closeModal();
      }
    }, function(response) {
      // Failed
      vm.errorMessage = response.data.message;
    });
  }

  closeModal() {
    this.modalInstance.dismiss();
  }
}