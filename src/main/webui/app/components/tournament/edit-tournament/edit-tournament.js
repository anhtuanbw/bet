'use strict';

export default class EditTournamentController {
  constructor(TournamentService, $rootScope) {
    this.tournamentInfo = {};
	  $rootScope.$on('selectTournament', (event, tournamentInfo) => {
      this.tournamentInfo = tournamentInfo;
    });
  }
}