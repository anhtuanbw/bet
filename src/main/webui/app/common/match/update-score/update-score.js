'use strict';

export default class UpdateScoreController {
  /* @ngInject */
  
  constructor(MatchService, CacheService, $location, $modalInstance, toaster, getMatch, $rootScope) {
    this.rootScope = $rootScope;
    this.matchService = MatchService;
    this.cacheService = CacheService;
    this.location = $location;
    this.modalInstance = $modalInstance;
    this.toaster = toaster;
    this.match = getMatch;
    this.data = {};
    this.getMatchInfo();
  }

  updateScore() {

    var self = this;
    this.popTitle = 'Update score';
    var successMessage = 'Update score successfully!';
    
    // Show alert message
    this.pop = function (type, title, content) {
      this.toaster.pop(type, title, content);
    };

    this.matchService.updateScore(this.data)
      .then(() => {
        
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
    this.data.matchId = this.match.id;
    this.data.competitor1Score = this.match.score1 === '?' ? 0 : this.match.score1;
    this.data.competitor2Score = this.match.score2 === '?' ? 0 : this.match.score2;
    console.log(this.data);
  }

  closeModal() {
    this.modalInstance.dismiss();
  }
}
