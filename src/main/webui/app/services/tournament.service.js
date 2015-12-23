'use strict';

export default class TournamentService {
  /* @ngInject */
  constructor($http) {
    this.$http = $http;
  }
  
  createTournament(tournamentInfo) {
	  return this.$http.post('api/tournaments/create', tournamentInfo);
  }
  
  getAll() {
	  return this.$http.get('api/tournaments/findAll');
  }
}