'use strict';

export default class GroupService {
	/* @ngInject */
	constructor($http, CacheService) {
		this.$http = $http;
		this.cacheService = CacheService;
	}

	create(data) {
		var token = this.cacheService.get('loginUser');
		return this.$http({
			method: 'POST',
			url: 'api/group/create',
			data: data,
			headers: {'Accept': '*/*', 'x-auth-token': token}
		});
	}

}