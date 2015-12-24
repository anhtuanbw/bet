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
    this.dataRounds = [];
    this.dataCompetitors = [];
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
    // var colors = [
    //   {id:1, shade:'dark'},
    //   {id:2, shade:'light'},
    //   {id:3, shade:'dark'},
    //   {id:4, shade:'dark'},
    //   {id:5, shade:'light'}
    // ];
    // var i;
    // for (i = 0; i < colors.length; i++) {
    //        this.dataRounds.push(colors[i]);
    // }
    // console.log(this.dataRounds);
    this.matchService.getRounds('1')
      .then(response => {
        
        // Success
        var i;
        for (i = 0; i < response.data.length; i++) {
          this.dataRounds.push(response.data[i]);
        }
      });
  }

  closeModal() {
    this.modalInstance.dismiss();
    this.data = {};
  }
}