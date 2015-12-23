'use strict';

export default class ManagementController {
  constructor(TournamentService, $rootScope) {
    this.rootScope = $rootScope;
    this.tournamentService = TournamentService;
    this.tournaments = [];
    this.getAllTournament();
    this.templateURL = 'app/components/tournament/create-tournament/create-tournament.html';
  }
  
  getAllTournament() {
    this.tournamentService.getAll()
    .then(response => {
      this.tournaments = response.data;
    })
    .catch();
  }
  
  showTournamenDetail(tournamentId) {
    this.templateURL = 'app/components/tournament/edit-tournament/edit-tournament.html';
    for (var i in this.tournaments)
    {
      if (this.tournaments[i].id === tournamentId) {
        this.rootScope.$broadcast('selectTournament', this.tournaments[i]);
        break;
      }
    }
    
  }
}