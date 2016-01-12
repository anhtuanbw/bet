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
      headers: {
        'Accept': '*/*',
        'x-auth-token': token
      }
    });
  }

  getBettingMatchStatistics(bettingMatchId) {
    var token = this.cacheService.get('loginUser');
    return this.$http({
      method: 'GET',
      url: '/api/betting-match/bettingMatchStatistics/' + bettingMatchId,
      headers: {
        'Accept': '*/*',
        'x-auth-token': token
      }
    });
  }

  getComment(bettingMatchId, paging) {
    var token = this.cacheService.get('loginUser');
    return this.$http({
      method: 'GET',
      url: '/api/player/comments/' + bettingMatchId + '/' + paging,
      headers: {
        'Accept': '*/*',
        'x-auth-token': token
      }
    });
  }

  getBettingPlayer(bettingMatchId) {
    var token = this.cacheService.get('loginUser');
    return this.$http({
      method: 'GET',
      url: '/api/betting-player/getBettingPlayerByBettingMatchId/' + bettingMatchId,
      headers: {
        'Accept': '*/*',
        'x-auth-token': token
      }
    });
  }

  checkExpiredBettingMatch(bettingMatchId) {
    var token = this.cacheService.get('loginUser');
    return this.$http({
      method: 'GET',
      url: '/api/checkBettingMatchNotExpired/' + bettingMatchId,
      headers: {
        'Accept': '*/*',
        'x-auth-token': token
      }
    });
  }

  getLostAmount(bettingMatchId) {
    var token = this.cacheService.get('loginUser');
    return this.$http({
      method: 'GET',
      url: '/api/statistic/player/getLostAmount/' + bettingMatchId,
      headers: {
        'Accept': '*/*',
        'x-auth-token': token
      }
    });
  }
  
    getNumberComments(bettingMatchId) {
    var token = this.cacheService.get('loginUser');
    return this.$http({
      method: 'GET',
      url: '/api/player/getCommentCount/' + bettingMatchId,
      headers: { 'Accept': '*/*', 'x-auth-token': token }
    });
  }

}