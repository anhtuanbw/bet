'use strict';

export default class CreateMatchController {
  /* @ngInject */
  constructor(MatchService, CacheService, $location, $modalInstance, toaster, editId) {
    this.matchService = MatchService;
    this.cacheService = CacheService;
    this.location = $location;
    this.modalInstance = $modalInstance;
    this.toaster = toaster;
    this.data = {};
    this.dataRounds = [];
    this.dataCompetitors = [];
    this.tournamentId = editId;
    this.getRounds();
  }

  createMatch() {
    var self = this;
    self.popTitle = 'Create new match';
    var successMessage = 'Create match successfully!';
    // Show alert message
    self.pop = function (type, title, content) {
      this.toaster.pop(type, title, content);
    };
  console.log(self.data);
    this.matchService.createMatch(this.data)
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
    this.matchService.getRounds(this.tournamentId)
      .then(response => {
        
        // Success
        var i;
        for (i = 0; i < response.data.length; i++) {
          this.dataRounds.push(response.data[i]);
        }
      });
  }

  getCompetitors() {
    console.log(this.data.round);
    this.matchService.getCompetitors(this.data.round)
      .then(response => {
        
        // Success
        var i;
        for (i = 0; i < response.data.length; i++) {
          this.dataCompetitors.push(response.data[i]);
        }
      });
  }

  closeModal() {
    this.modalInstance.dismiss();
    this.data = {};
  }
}