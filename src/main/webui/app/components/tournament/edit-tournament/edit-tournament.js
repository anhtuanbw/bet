'use strict';

export default class EditTournamentController {
  constructor(TournamentService, $rootScope) {
   // this.tournamentInfo = {};
	  $rootScope.$on('selectTournament', (event, tournamentInfo) => {
	  console.log(tournamentInfo);
      this.tournamentInfo = tournamentInfo;
    });
  }
}