'use strict';

export default class ResetPasswordSuccessController {
  /* @ngInject */
  constructor($location, $modalInstance) {
    this.location = $location;
    this.modalInstance = $modalInstance;
  }

  closeModal() {
    this.modalInstance.dismiss();
    this.location.path('/home');
  }

}