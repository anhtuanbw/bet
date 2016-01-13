'use strict';

export default class UnAuthorizedController {
	/* @ngInject */
	constructor($timeout, $location, $rootScope) {
		this.location = $location;
		$rootScope.$broadcast('authen');
		$timeout(function () {
			$location.path('/home');
		}, 3000);
	}
	
	redirect() {
		this.location.path('/home');
	}
}