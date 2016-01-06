'use strict';

export default class BettingService {
  /* @ngInject */
	  constructor($http, CacheService){
	  	this.$http = $http;
	  	this.cacheService = CacheService;
	  }

	  create(data){
	  	var token = this.cacheService.get('loginUser');
	    var config = {
	      headers: { 'Accept': '*/*', 'x-auth-token': token }
	    };
	    return this.$http.post('api/createBettingMatch', data, config);
	  }

	  update(data){
	  	var token = this.cacheService.get('loginUser');
	    var config = {
	      headers: { 'Accept': '*/*', 'x-auth-token': token }
	    };
	    return this.$http.post('api/updateBettingMatch', data, config);
	  }

	  active(data){
	  	var token = this.cacheService.get('loginUser');
	    var config = {
	      headers: { 'Accept': '*/*', 'x-auth-token': token }
	    };
	    var id = {
  		'bettingMatchId': data
		};
	    return this.$http.post('api/activeBettingMatch', id, config);
	  }

	  getBettingMatchByRoundAndGroupId(roundId, groupId){
	  	var token = this.cacheService.get('loginUser');
	    var config = {
	      headers: { 'Accept': '*/*', 'x-auth-token': token }
	    };
	    var dataPost = {
  		'roundId': roundId,
  		'groupId': groupId
		};
	    return this.$http.post('api/getBettingMatchesByRoundAndGroupId', dataPost, config);
	  }

  }