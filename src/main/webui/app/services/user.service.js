'use strict';

export default class UserService {
	/* @ngInject */
	constructor($http, CacheService) {
		this.$http = $http;
		this.cacheService = CacheService;
	}

	users() {
		var token = this.cacheService.get('loginUser');
		return this.$http({
			method: 'GET',
			url: 'api/users',
			headers: {'Accept': '*/*', 'x-auth-token': token}
		});
	}

}