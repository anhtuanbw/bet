'use strict';

export default class PlayerHistoryService {
	/* @ngInject */
	constructor($http, CacheService) {
		this.$http = $http;
		this.cacheService = CacheService;
	}

	getHistoryBetting() {
		var token = this.cacheService.get('loginUser');
		return this.$http({
			method: 'GET',
			url: 'api/player/getHistoryBetting',
			headers: {'Accept': '*/*', 'x-auth-token': token}
		});
	}

}