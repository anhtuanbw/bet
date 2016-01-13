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
    self.popTitle = 'Update score';
    
    // Show alert message
    self.pop = function (type, title, content) {
      this.toaster.pop(type, title, content);
    };

    this.matchService.updateScore(this.data)
      .then(() => {
        
        // Success
        this.closeModal();
        this.toaster.pop('success', null, 'app/common/match/update-score/success.html', null, 'template');
        this.data = {};
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
  }

  closeModal() {
    this.modalInstance.dismiss();
  }
}
