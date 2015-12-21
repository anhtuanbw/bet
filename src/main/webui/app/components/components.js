'use strict';

import Login from './login/login';

export const componentsModule = angular.module('ngaythobet.components', [
  'ui.bootstrap.tpls',
  'ui.bootstrap.modal',
  'ui.bootstrap.dropdown'
])

.directive('loginForm', () => new Login() );
