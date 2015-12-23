'use strict';

export default class ResetPasswordFinishController {
  /* @ngInject */
  constructor(AccountService, $location) {
    this.accountService = AccountService;
    this.location = $location;
    this.errorMessage = {};
  }

  resetPasswordFinish(resetPasswordInfo) {
    var vm = this;
    this.accountService.resetPasswordFinish(vm.location.search().key, resetPasswordInfo)
    .then(response => {
      // Success
      if (response.status === 200) {
        vm.location.path('/home');
      } else {
        vm.errorMessage = response.data.message;
      }
    }, function(response) {
      // Failed
      if(response.data.message) {
        vm.errorMessage.message = response.data.message;
      } else {
       vm.errorMessage = response.data.fieldErrors;
     }
   });
  }

}