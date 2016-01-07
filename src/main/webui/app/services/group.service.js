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
	
	addMember(data) {
		var token = this.cacheService.get('loginUser');
		return this.$http({
			method: 'POST',
			url: 'api/group/addMember',
			data: data,
			headers: {'Accept': '*/*', 'x-auth-token': token}
		});
	}
	
	isModerator(data) {
		var token = this.cacheService.get('loginUser');
		return this.$http({
			method: 'POST',
			url: '/api/group/isModerator',
			data: data,
			headers: {'Accept': '*/*', 'x-auth-token': token}
		});
	}
	
	findById(id) {
		var token = this.cacheService.get('loginUser');
		return this.$http({
			method: 'GET',
			url: '/api/group/' + id,
			headers: {'Accept': '*/*', 'x-auth-token': token}
		});
	}
	
	getStatisticInfo(id) {
		var token = this.cacheService.get('loginUser');
		return this.$http({
			method: 'GET',
			url: '/api/group/statistic' + id,
			headers: {'Accept': '*/*', 'x-auth-token': token}
		});
	}

}