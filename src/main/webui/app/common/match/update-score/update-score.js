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
        
        // send ajax request >> data
        document.getElementById(this.data.matchId).innerHTML = this.data.competitor1Score + ' - ' + this.data.competitor2Score;
        this.data = {};

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
    this.matchService.getMatchInfo(this.match.id)
      .then(response => {

        this.data.competitor1Score = response.data.score1 === null ? 0 : response.data.score1;
        this.data.competitor2Score = response.data.score2 === null ? 0 : response.data.score2;
      });
  }

  closeModal() {
    this.modalInstance.dismiss();
  }
}
