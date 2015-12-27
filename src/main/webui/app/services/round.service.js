'use strict';

export default class RoundService {
  /* @ngInject */
  constructor($http, CacheService) {
    this.$http = $http;
    this.cacheService = CacheService;
  }

  getAllTournament(){
    var token = this.cacheService.get('loginUser');
    return this.$http({
      method: 'GET',
      url: 'api/tournaments/findAll',
      headers: { 'Accept': '*/*', 'x-auth-token': token }
    });
  }

  getAllCompetitor(id){
    var token = this.cacheService.get('loginUser');
    return this.$http({
      method: 'GET',
      url: `api/competitors/findByTournamentId?tournamentId=${id}`,
      headers: { 'Accept': '*/*', 'x-auth-token': token }
    });
  }

  create(data){
    var token = this.cacheService.get('loginUser');
    var config = {
      headers: { 'Accept': '*/*', 'x-auth-token': token }
    };
    return this.$http.post('api/createRound', data, config);
  }

  update(data){
    var token = this.cacheService.get('loginUser');
    var config = {
      headers: { 'Accept': '*/*', 'x-auth-token': token }
    };
    return this.$http.post('api/updateRound', data, config);
  }

  getRoundInTournament(id){
    var token = this.cacheService.get('loginUser');
    return this.$http({
      method: 'GET',
      url: `api/getRoundsInTournament/${id}`,
      headers: { 'Accept': '*/*', 'x-auth-token': token }
    });
  }

}
