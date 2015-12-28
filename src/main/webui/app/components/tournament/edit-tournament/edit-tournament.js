'use strict';

export default class EditTournamentController {
  /* @ngInject */
  constructor(TournamentService, $rootScope, $modal) {
    this.tournamentService = TournamentService;
    this.tournamentInfo = {};
    this.modal = $modal;
	  $rootScope.$on('selectTournament', (event, tournamentInfo) => {
      this.tournamentInfo = tournamentInfo;
    });
  }
  
  activeTournament() {
    this.tournamentService.active(this.tournamentInfo.id)
    .then(() => {
      this.tournamentInfo.activated = true;
    })
    .catch();
  }
  
  openCreateMatch() {
    var self = this;
    this.modal.open({
      templateUrl: 'app/common/match/create-match/create-match.html',
      controller: 'CreateMatchController',
      controllerAs: 'createMatch',
       resolve: {
         editId: function () {
           return self.tournamentInfo.id;
         }
       }
    });
  }
}