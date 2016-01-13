'use strict';

export default class MenuToggle {
	/* @ngInject */
	constructor() {
		return {
			replace: false,
			scope: true,
			templateUrl: 'app/common/menu/menu-toggle/menu-toggle.html'
		};
	}
}