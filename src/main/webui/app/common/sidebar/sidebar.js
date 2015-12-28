'use strict';

export default class SideBar {
  /* @ngInject */
  constructor() {
    return {
      replace: false,
      templateUrl: 'app/common/sidebar/sidebar.html'
    };
  }
}