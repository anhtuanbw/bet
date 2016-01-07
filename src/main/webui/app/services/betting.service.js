'use strict';

export default class BettingService {
  /* @ngInject */
	  constructor($http, CacheService, AccountService){
	  	this.$http = $http;
	  	this.cacheService = CacheService;
	  	this.accountService = AccountService;
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
	    return this.$http.post('api/activeBettingMatch', data, config);
	  }

	  getBettingMatchByRoundAndGroupId(roundID, groupId){
	  	var token = this.cacheService.get('loginUser');
	    var config = {
	      headers: { 'Accept': '*/*', 'x-auth-token': token }
	    };
	    var dataPost = {
  		'roundId': roundID,
  		'groupId': groupId
		};
		return this.$http.post('api/getBettingMatchesByRoundAndGroupId', dataPost, config);
	  }

  }