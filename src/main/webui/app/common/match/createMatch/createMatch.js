'use strict';

export default class CreateMatchController {
  /* @ngInject */
  constructor(MatchService, CacheService, $location, $modalInstance, toaster) {
    this.matchService = MatchService;
    this.cacheService = CacheService;
    this.location = $location;
    this.modalInstance = $modalInstance;
    this.toaster = toaster;
    this.data = {};
    this.dataRounds = {};
    this.dataCompetitors = {};
  }

  createMatch() {
    var self = this;
    self.popTitle = 'Create new match';
    var successMessage = 'Create match successfully!';
    // Show alert message
    self.pop = function (type, title, content) {
      this.toaster.pop(type, title, content);
    };

    this.this.matchService.createMatch(this.data)
      .then(response => {
        
        // Success
        self.closeModal();
        self.pop('success', self.popTitle, successMessage);
        self.data = {};
        successMessage = '';
        self.location.path('/home');
      })
      .catch(response => {
        
        // return error
        if (response.data.message) {
          self.pop('error', self.popTitle, response.data.message);
        }
        self.errorMessage = response.data.fieldErrors;
      });
  }

  getRounds() {
    var self = this;
    this.this.matchService.createMatch(this.dataRounds)
      .then(response => {
        
        // Success
      })
      .catch(response => {
        
        // return error
        if (response.data.message) {
          self.pop('error', self.popTitle, response.data.message);
        }
      });
  }

  closeModal() {
    this.modalInstance.dismiss();
    this.data = {};
  }
}