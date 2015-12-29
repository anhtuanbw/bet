'use strict';

export default class ManagementController {
  /* @ngInject */
  constructor(TournamentService, CacheService, $rootScope) {
    this.rootScope = $rootScope;
    this.tournamentService = TournamentService;
    this.tournaments = [];
    this.getAllTournament();
    this.showView = {
      isCreate: true,
      isEdit: false,
      isGroup: false
    };
    this.selected = -1;
    this.cacheService = CacheService;
    $rootScope.$on('addTournament', () => {
      this.getAllTournament();
    });
  }

  select(index) {
    this.selected = index;
  }

  createTournament() {
    this.showView.isCreate = true;
    this.showView.isEdit = false;
    this.showView.isGroup = false;
  }

  createGroup() {
    this.showView.isCreate = false;
    this.showView.isEdit = false;
    this.showView.isGroup = true;
    console.log('asdfasfd');
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
    this.showView.isEdit = true;
    this.showView.isGroup = false;
    this.showView.isCreate = false;

    for (var i in this.tournaments)
    {
      if (this.tournaments[i].id === tournamentId) {
        this.rootScope.$broadcast('selectTournament', this.tournaments[i]);
        break;
      }
    }
  }
}