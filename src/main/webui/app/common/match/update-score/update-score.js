'use strict';

export default class UpdateScoreController {
  /* @ngInject */
  constructor(MatchService, CacheService, $location, $modalInstance, toaster, getMatchId, $rootScope) {
    this.rootScope = $rootScope;
    this.matchService = MatchService;
    this.cacheService = CacheService;
    this.location = $location;
    this.modalInstance = $modalInstance;
    this.toaster = toaster;
    this.matchId = getMatchId;
    this.data = {};
    this.matchInfo = {};
    this.getMatchInfo();
  }

  updateScore() {
    console.log(this.data);
    var self = this;
    this.popTitle = 'Update score';
    var successMessage = 'Update score successfully!';
    // Show alert message
    this.pop = function (type, title, content) {
      this.toaster.pop(type, title, content);
    };

    this.matchService.updateScore(this.data)
      .then(response => {
        
        // Success
        this.closeModal();
        this.pop('success', this.popTitle, successMessage);
        this.data = {};
        successMessage = '';
        this.rootScope.$broadcast('selectTournament');
      })
      .catch(response => {
        // return error
        if (response.data.message) {
          self.pop('error', self.popTitle, response.data.message);
        }
        self.errorMessage = response.data.fieldErrors;
      });
  }

  getMatchInfo() {
    this.data.matchId = this.matchId;
    this.matchService.getMatchInfo(this.matchId)
      .then(response => {
        
        // Success
        this.matchInfo.competitor1 = response.data.competitor1.name;
        this.matchInfo.competitor2 = response.data.competitor2.name;
      });
  }

  closeModal() {
    this.modalInstance.dismiss();
  }
}