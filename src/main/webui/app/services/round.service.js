'use strict';

export default class RoundService {
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

}
