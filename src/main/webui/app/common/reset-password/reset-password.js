'use strict';

export default class ResetPasswordController {
  /* @ngInject */
  constructor(AccountService, $location, $modalInstance, toaster) {
    this.accountService = AccountService;
    this.location = $location;
    this.modalInstance = $modalInstance;
    this.toaster = toaster;
  }

  reset(email) {
    var vm = this;
    vm.popTitle = 'Reset password';

    // Show alert message
    vm.pop = function(type, title, content) {
      this.toaster.pop(type, title, content);
    };

    this.accountService.resetPassword(email)
      .then(response => {
        // Success
        if (response.status === 200) {
          vm.closeModal();
          vm.pop('success', vm.popTitle, 'Check your email for a link to reset your password.');

          // Redirect to home page
          vm.location.path('/home');
        } else {
          vm.pop('warning', vm.popTitle, response.data.message);
        }
      })
      .catch(response => {
        vm.pop('error', vm.popTitle, response.data.message);
      });
  }

  closeModal() {
    this.modalInstance.dismiss();
  }

}