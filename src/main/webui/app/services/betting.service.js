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

  }