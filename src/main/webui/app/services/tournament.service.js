'use strict';

export default class TournamentService {
  /* @ngInject */
  constructor($http) {
    this.$http = $http;
  }

  findAll() {
    // return this.$http.get('api/tournaments/findAll');
    return this.$http({
      method: 'GET',
      url: 'api/tournaments/findAll',
      headers: {'Accept': '*/*'}
    });
  }

}