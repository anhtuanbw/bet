'use strict';

export default class UserService {
	/* @ngInject */
	constructor($http, CacheService) {
		this.$http = $http;
		this.cacheService = CacheService;
	}

	users(name) {
		var token = this.cacheService.get('loginUser');
		return this.$http({
			method: 'GET',
			url: 'api/users/search/' + name,
			headers: {
				'Accept': '*/*',
				'x-auth-token': token
			}
		});
	}

}