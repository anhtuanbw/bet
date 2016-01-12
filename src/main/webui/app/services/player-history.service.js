'use strict';

export default class PlayerHistoryService {
	/* @ngInject */
	constructor($http, CacheService) {
		this.$http = $http;
		this.cacheService = CacheService;
	}

	playerStatistic(groupId) {
		var token = this.cacheService.get('loginUser');
		return this.$http({
			method: 'POST',
			url: 'api/playerStatistic',
			data: {
				'groupId': groupId
			},
			headers: {
				'Accept': '*/*',
				'x-auth-token': token
			}
		});
	}

}