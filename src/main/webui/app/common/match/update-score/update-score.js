'use strict';

export default class UpdateScoreController {
  /* @ngInject */
  constructor(MatchService, CacheService, $location, $modalInstance, toaster) {
    this.matchService = MatchService;
    this.cacheService = CacheService;
    this.location = $location;
    this.modalInstance = $modalInstance;
    this.toaster = toaster;
    this.data = {};
  }

  updateScore() {
    var self = this;
    this.popTitle = 'Update score';
    var successMessage = 'Update score successfully!';
    // Show alert message
    this.pop = function (type, title, content) {
      this.toaster.pop(type, title, content);
    };
    
    this.matchService.updateScore(this.data, '1')
      .then(response => {
        
        // Success
        this.closeModal();
        this.pop('success', this.popTitle, successMessage);
        this.data = {};
        successMessage = '';
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
  }
}