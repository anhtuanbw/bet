'use strict';

export default class GroupService {
	/* @ngInject */
	constructor($http) {
		this.$http = $http;
	}

	create(data) {
		return this.$http({
			method: 'POST',
			url: 'api/group/create',
			data: data,
			headers: {'Accept': '*/*'}
		});
	}

}