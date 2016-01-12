'use strict';

export default class MatchService {
  /* @ngInject */
  constructor($http, CacheService) {
    this.$http = $http;
    this.cacheService = CacheService;
  }

  createMatch(data) {
    var token = this.cacheService.get('loginUser');
    return this.$http({
      method: 'POST',
      url: '/api/matches/create-match',
      headers: {
        'Accept': '*/*',
        'x-auth-token': token
      },
      data: data
    });
  }

  getRounds(tournamentId) {
    var token = this.cacheService.get('loginUser');
    return this.$http({
      method: 'GET',
      url: '/api/matches/rounds/' + tournamentId,
      headers: {
        'Accept': '*/*',
        'x-auth-token': token
      }
    });
  }

  getCompetitors(roundId) {
    var token = this.cacheService.get('loginUser');
    return this.$http({
      method: 'GET',
      url: '/api/matches/competitors/' + roundId,
      headers: {
        'Accept': '*/*',
        'x-auth-token': token
      }
    });
  }

  updateScore(data) {
    var token = this.cacheService.get('loginUser');
    return this.$http({
      method: 'POST',
      url: '/api/matches/update-score',
      headers: {
        'Accept': '*/*',
        'x-auth-token': token
      },
      data: data
    });
  }

  getMatchInfo(matchId) {
    var token = this.cacheService.get('loginUser');
    return this.$http({
      method: 'GET',
      url: '/api/matches/getMatch/' + matchId,
      headers: {
        'Accept': '*/*',
        'x-auth-token': token
      }
    });
  }
}