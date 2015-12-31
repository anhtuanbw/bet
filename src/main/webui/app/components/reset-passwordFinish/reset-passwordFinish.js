'use strict';

export default class ResetPasswordFinishController {
  /* @ngInject */
  constructor(AccountService, $location, $modal) {
    this.accountService = AccountService;
    this.location = $location;
    this.modal = $modal;
    this.errorMessage = {};
  }

  resetPasswordFinish(resetPasswordInfo) {
    var self = this;
    self.resetSuccess = function() {
      this.modal.open({
        templateUrl: 'app/common/reset-passwordSuccess/reset-passwordSuccess.html',
        controller: 'ResetPasswordSuccessController',
        controllerAs: 'resetpasswordSuccess'
      });
    };

    this.accountService.resetPasswordFinish(self.location.search().key, resetPasswordInfo)
    .then(response => {
      // Success
      if (response.status === 200) {
        self.resetSuccess();
      } else {
        self.errorMessage = response.data.message;
      }
    }, function(response) {
      // Failed
      if(response.data.message) {
        self.errorMessage.message = response.data.message;
      } else {
       self.errorMessage = response.data.fieldErrors;
     }
   });
  }
 
}