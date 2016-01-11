'use strict';

export default class MenuLink {
  /* @ngInject */
  constructor() {
    return {
       replace: false,
       scope: true,
       templateUrl: 'app/common/menu/menu-link/menu-link.html'
    };
  }
}