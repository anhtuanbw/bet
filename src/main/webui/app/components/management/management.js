'use strict';

export default class ManagementController {
  constructor(TournamentService) {
    this.tournamentService = TournamentService;
    this.tournaments = [];
    this.getAllTournament();
    this.templateURL = 'app/components/tournament/tournament.html';
  }
  
  getAllTournament() {
    this.tournamentService.getAll()
    .then(response => {
      console.log(response);
      this.tournaments = response;
    })
    .catch();
  }
}