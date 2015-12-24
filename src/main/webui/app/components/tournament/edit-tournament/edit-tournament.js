'use strict';

export default class EditTournamentController {
  constructor(TournamentService, $rootScope) {
    this.tournamentService = TournamentService;
    this.tournamentInfo = {};
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
}