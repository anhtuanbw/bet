'use strict';

export default class TournamentService {
  /* @ngInject */
  constructor($http, CacheService) {
    this.$http = $http;
    this.cacheService = CacheService;
  }
  
  createTournament(tournamentInfo) {
    var token = this.cacheService.get('loginUser');
    return this.$http({
      method: 'POST',
      url: 'api/tournaments/create',
      headers: {'Accept': '*/*', 'x-auth-token': token},
      data: tournamentInfo
    });
  }
  
  getAll() {
    var token = this.cacheService.get('loginUser');
    return this.$http({
      method: 'GET',
      url: 'api/tournaments/findAll',
      headers: {'Accept': '*/*', 'x-auth-token': token}
    });
  }
}