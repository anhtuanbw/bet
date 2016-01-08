'use strict';

export default class CreateTournamentController {
  /* @ngInject */
  constructor(TournamentService, toaster, $rootScope, $location) {
    this.rootScope = $rootScope;
    this.location = $location;
    this.tournamentService = TournamentService;
    this.competitors = [];
    this.name = '';
    this.errorMessage = {};
    this.active = false;
    this.toaster = toaster;
  }
  
  getNumOfCompetitors() {
    return this.competitors.length;
  }
  
  getData() {
    var data = {};
    data.name = this.name;
    data.competitors = [];
    data.active = this.active;
    for( var i in this.competitors) {
      data.competitors.push(this.competitors[i].text);
    }
    return data;
  }
  
  createTournament() {
    var tournamentInfo = this.getData();
    this.tournamentService.createTournament(tournamentInfo)
    .then(() => {
      this.toaster.pop('success', null, 'app/components/tournament/create-tournament/success.html', null, 'template');
      this.competitors = [];
      this.name = '';
      this.active = false;
      this.errorMessage = {};
      this.rootScope.$broadcast('addTournament');
    })
    .catch(error => {
      if (error.status === 401) {
        this.location.path('/unauthorized');
      }
      if (error.status === 400) {
        this.errorMessage = error.data.fieldErrors;
      }
      if (error.status === 403) {
        this.toaster.pop('error', 'Warning', error.data.message);
      }
    });
  }
}