'use strict';

export default class BettingMatchService {
  /* @ngInject */
  constructor($http, CacheService) {
    this.$http = $http;
    this.cacheService = CacheService;
  }

  getBettingMatchInfo(bettingMatchId) {
    var token = this.cacheService.get('loginUser');
    return this.$http({
      method: 'GET',
      url: '/api/betting-match/' + bettingMatchId,
      headers: { 'Accept': '*/*', 'x-auth-token': token }
    });
  }

  getBettingMatchStatistics(bettingMatchId) {
    var token = this.cacheService.get('loginUser');
    return this.$http({
      method: 'GET',
      url: '/api/betting-match/bettingMatchStatistics/' + bettingMatchId,
      headers: { 'Accept': '*/*', 'x-auth-token': token }
    });
  }

  getComment(bettingMatchId) {
    var token = this.cacheService.get('loginUser');
    return this.$http({
      method: 'GET',
      url: '/api/player/comments/' + bettingMatchId,
      headers: { 'Accept': '*/*', 'x-auth-token': token }
    });
  }
  
  getBettingPlayer(bettingMatchId) {
    var token = this.cacheService.get('loginUser');
    return this.$http({
      method: 'GET',
      url: '/api/betting-player/getBettingPlayerByBettingMatchId/' + bettingMatchId,
      headers: { 'Accept': '*/*', 'x-auth-token': token }
    });
  }
}