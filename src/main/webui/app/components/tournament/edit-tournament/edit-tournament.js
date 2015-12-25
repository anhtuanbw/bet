'use strict';

export default class EditTournamentController {
  /* @ngInject */
  constructor(TournamentService, $rootScope, $modal, $mdDialog) {
    this.tournamentService = TournamentService;
    this.tournamentInfo = {};
    this.modal = $modal;
    this.mdDialog = $mdDialog;
	  $rootScope.$on('selectTournament', (event, tournamentInfo) => {
      this.tournamentInfo = tournamentInfo;
    });
  }

  createGroup($event) {
    this.mdDialog.show({
      controller: 'GroupController',
      controllerAs: 'groupCtrl',
      templateUrl: 'app/common/group/group.html',
      parent: angular.element(document.body),
      targetEvent: $event,
      clickOutsideToClose:true
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