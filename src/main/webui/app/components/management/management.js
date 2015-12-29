'use strict';

export default class ManagementController {
  /* @ngInject */
  constructor(TournamentService, CacheService, $rootScope, AccountService) {
    this.rootScope = $rootScope;
    this.tournamentService = TournamentService;
    this.accountService = AccountService;
    this.tournaments = [];
    this.getAllTournament();
    this.showDetail = false;
    this.selected = -1;
    this.cacheService = CacheService;
    this.isAdmin = false;
    this.authen();
    this.templateURL = 'app/components/tournament/create-tournament/create-tournament.html';
    $rootScope.$on('addTournament', () => {
      this.getAllTournament();
    });
  }

  select(index) {
    this.selected = index;
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
  
   authen() {
    this.accountService.authen()
    .then(response => {
      if (response.data) {
         this.isAdmin = response.data.role === 'ADMIN' ? true : false;
      }
    });
  }
}