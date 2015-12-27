'use strict';

export default class ManagementController {
  /* @ngInject */
  constructor(TournamentService, CacheService, $rootScope) {
    this.rootScope = $rootScope;
    this.tournamentService = TournamentService;
    this.tournaments = [];
    this.getAllTournament();
    this.showDetail = true;
    this.cacheService = CacheService;
    this.templateURL = 'app/components/tournament/create-tournament/create-tournament.html';
    $rootScope.$on('addTournament', () => {
      this.getAllTournament();
    });
  }

  createTournament() {
    this.showDetail = false;
  }

  isAuthorized() {
    return this.cacheService.get('loginUser')!= null;
  }

  getAllTournament() {
    this.tournamentService.getAll()
    .then(response => {
      this.tournaments = response.data;
    })
    .catch();
  }
  
  showTournamenDetail(tournamentId) {
    this.showDetail = true;
    for (var i in this.tournaments)
    {
      if (this.tournaments[i].id === tournamentId) {
        this.rootScope.$broadcast('selectTournament', this.tournaments[i]);
        break;
      }
    }
    this.templateURL = 'app/components/tournament/edit-tournament/edit-tournament.html';
  }
}